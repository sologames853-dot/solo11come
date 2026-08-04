const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');
const bodyParser = require('body-parser');
const axios = require('axios'); // Added axios
require('dotenv').config();

const app = express();
const PORT = process.env.PORT || 10000; // Render uses 10000 often, but process.env.PORT is priority

console.log("Server starting on Port:", PORT);

// Middleware
app.use(cors());
app.use(bodyParser.json());

// MongoDB Connection
const mongoURI = process.env.MONGO_URI;
if (!mongoURI) {
    console.error("CRITICAL ERROR: MONGO_URI is not defined in Environment Variables!");
} else {
    console.log("Connecting to MongoDB...");
    mongoose.connect(mongoURI)
        .then(() => console.log('MongoDB Connected Successfully'))
        .catch(err => console.error('MongoDB connection error:', err));
}

// Models (Added Contest Schema for Admin)
const UserSchema = new mongoose.Schema({
    phone: String,
    name: { type: String, default: "User" },
    email: String,
    balance: { type: Number, default: 0 },
    isAdmin: { type: Boolean, default: false },
    kycStatus: { type: String, default: "PENDING" }, // PENDING, APPROVED, REJECTED
    kycDetails: {
        pan: String,
        aadhar: String
    },
    createdAt: { type: Date, default: Date.now }
});
const User = mongoose.model('User', UserSchema);

const ContestSchema = new mongoose.Schema({
    name: String,
    prizePool: String,
    entryFee: Number,
    totalSpots: Number,
    joinedSpots: { type: Number, default: 0 },
    type: String, // Mega, Hot, Practice
    matchId: String
});
const Contest = mongoose.model('Contest', ContestSchema);

const UserTeamSchema = new mongoose.Schema({
    userId: String,
    matchId: String,
    playerIds: [String],
    captainId: String,
    viceCaptainId: String,
    totalPoints: { type: Number, default: 0 },
    timestamp: { type: Date, default: Date.now }
});
const UserTeam = mongoose.model('UserTeam', UserTeamSchema);

const JoinedContestSchema = new mongoose.Schema({
    userId: String,
    contestId: { type: mongoose.Schema.Types.ObjectId, ref: 'Contest' },
    teamId: { type: mongoose.Schema.Types.ObjectId, ref: 'UserTeam' },
    matchId: String,
    timestamp: { type: Date, default: Date.now }
});
const JoinedContest = mongoose.model('JoinedContest', JoinedContestSchema);

const TransactionSchema = new mongoose.Schema({
    userId: String,
    amount: Number,
    type: String, // DEPOSIT, WITHDRAW, CONTEST_ENTRY, WINNING
    status: { type: String, default: "PENDING" }, // PENDING, COMPLETED, REJECTED
    utr: String,
    timestamp: { type: Date, default: Date.now }
});
const Transaction = mongoose.model('Transaction', TransactionSchema);

const MatchSchema = new mongoose.Schema({
    id: String,
    name: String,
    status: String,
    venue: String,
    date: String,
    teamInfo: Array,
    active: { type: Boolean, default: true }
});
const Match = mongoose.model('Match', MatchSchema);

// Serve Static Admin Panel
app.use('/admin-panel', express.static('public/admin'));

// --- Admin Routes ---

// 1. Get Dashboard Stats
app.get('/admin/stats', async (req, res) => {
    try {
        const totalUsers = await User.countDocuments();
        const totalTeams = await UserTeam.countDocuments();
        const totalBalance = await User.aggregate([{ $group: { _id: null, total: { $sum: "$balance" } } }]);
        const contests = await Contest.countDocuments();

        res.json({
            users: totalUsers,
            teams: totalTeams,
            money: totalBalance[0] ? totalBalance[0].total : 0,
            contests: contests
        });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// 2. Get All Users
app.get('/admin/users', async (req, res) => {
    const users = await User.find().sort({ createdAt: -1 });
    res.json(users);
});

// Get Contests List for Admin
app.get('/admin/contests-list', async (req, res) => {
    const contests = await Contest.find().sort({ _id: -1 });
    res.json(contests);
});

// Sync Matches from External API
app.post('/admin/sync-matches', async (req, res) => {
    try {
        const response = await axios.get(`https://api.cricapi.com/v1/currentMatches?apikey=${process.env.CRICKET_API_KEY || '5f93b276-c5ab-455c-8dd2-198904909842'}&offset=0`);
        const matches = response.data.data;

        for (let m of matches) {
            await Match.findOneAndUpdate(
                { id: m.id },
                {
                    name: m.name,
                    status: m.status,
                    venue: m.venue,
                    date: m.date,
                    teamInfo: m.teamInfo,
                },
                { upsert: true }
            );
        }
        res.json({ status: 'success', message: 'Matches Synced' });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// Update Match Visibility
app.post('/admin/matches/update', async (req, res) => {
    const { matchId, active } = req.body;
    try {
        await Match.findOneAndUpdate({ id: matchId }, { active });
        res.json({ status: 'success' });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// Distribute Winnings (Logic Placeholder)
app.post('/admin/distribute-winning', async (req, res) => {
    const { matchId } = req.body;
    try {
        // 1. Calculate final points
        await calculateRealPoints(matchId);

        // 2. Find all contests for this match
        const contests = await Contest.find({ matchId });

        for (let contest of contests) {
            // 3. Get leaderboard
            const entries = await JoinedContest.find({ contestId: contest._id })
                .populate('teamId');

            // Sort by points (assuming teamId population works and has totalPoints)
            entries.sort((a, b) => b.teamId.totalPoints - a.teamId.totalPoints);

            // 4. Distribute prizes to top ranks (Logic depends on prize structure)
            if (entries.length > 0) {
                const winner = entries[0];
                const prize = parseFloat(contest.prizePool.replace(/[^0-9.]/g, '')) || 0;

                await User.findOneAndUpdate(
                    { phone: winner.userId },
                    { $inc: { balance: prize } }
                );

                const txn = new Transaction({
                    userId: winner.userId,
                    amount: prize,
                    type: 'WINNING',
                    status: 'COMPLETED',
                    utr: `WIN_${contest._id}_${winner.userId}`
                });
                await txn.save();
            }
        }
        res.json({ status: 'success', message: 'Winnings distributed to rank 1' });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// 3. Create a Contest
app.post('/admin/contests', async (req, res) => {
    try {
        const contest = new Contest(req.body);
        await contest.save();
        res.json({ status: 'success', message: 'Contest Created' });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// 4. Get All Teams
app.get('/admin/teams', async (req, res) => {
    const teams = await UserTeam.find().sort({ timestamp: -1 }).limit(50);
    res.json(teams);
});

// 5. Admin: Approve/Reject KYC
app.post('/admin/kyc/update', async (req, res) => {
    const { userId, status } = req.body;
    try {
        await User.findOneAndUpdate({ phone: userId }, { kycStatus: status });
        res.json({ status: 'success', message: `KYC ${status}` });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// ... (previous routes)

// 1. User Login / Registration
app.post('/login', async (req, res) => {
    const { phone } = req.body;
    try {
        let user = await User.findOne({ phone });
        if (!user) {
            user = new User({ phone });
            await user.save();
        }
        res.status(200).json({ status: 'success', user });
    } catch (err) {
        res.status(500).json({ status: 'error', message: err.message });
    }
});

// 2. Save User Team
app.post('/saveTeam', async (req, res) => {
    const { userId, matchId, playerIds, captainId, viceCaptainId } = req.body;
    try {
        const team = new UserTeam({ userId, matchId, playerIds, captainId, viceCaptainId });
        await team.save();
        res.status(200).json({ status: 'success', message: 'Team saved successfully' });
    } catch (err) {
        res.status(500).json({ status: 'error', message: err.message });
    }
});

// 3. Get User Profile
app.get('/user/:id', async (req, res) => {
    try {
        const user = await User.findOne({ phone: req.params.id });
        if (user) {
            res.status(200).json(user);
        } else {
            res.status(404).json({ message: 'User not found' });
        }
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

// 4. Update Wallet
app.post('/updateWallet', async (req, res) => {
    const { userId, amount } = req.query;
    try {
        const user = await User.findOneAndUpdate(
            { phone: userId },
            { $inc: { balance: parseFloat(amount) } },
            { new: true }
        );
        res.status(200).json(user);
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

// 5. Fetch Matches from CricAPI and Sync
app.get('/api/matches', async (req, res) => {
    try {
        // Here you would normally fetch from CricAPI using https or axios
        // For now, let's return matches from our DB
        const matches = await Match.find({ active: true });
        res.json({ status: 'success', data: matches });
    } catch (err) {
        res.status(500).json({ status: 'error', message: err.message });
    }
});

// 6. Join Contest Logic (Security: Check balance, Update spots)
app.post('/joinContest', async (req, res) => {
    const { userId, contestId, teamId } = req.body;
    try {
        const user = await User.findOne({ phone: userId });
        const contest = await Contest.findById(contestId);

        if (!user || !contest) return res.status(404).json({ message: 'User or Contest not found' });

        if (user.balance < contest.entryFee) {
            return res.status(400).json({ message: 'Insufficient balance' });
        }

        if (contest.joinedSpots >= contest.totalSpots) {
            return res.status(400).json({ message: 'Contest is full' });
        }

        // Deduct balance and update contest
        user.balance -= contest.entryFee;
        contest.joinedSpots += 1;

        // Create transaction record
        const txn = new Transaction({
            userId,
            amount: contest.entryFee,
            type: 'CONTEST_ENTRY',
            status: 'COMPLETED',
            utr: `ENTRY_${contestId}_${Date.now()}`
        });

        await user.save();
        await contest.save();
        await txn.save();

        // Save record of joined contest
        const joined = new JoinedContest({ userId, contestId, teamId, matchId: contest.matchId });
        await joined.save();

        res.json({ status: 'success', message: 'Contest joined successfully', newBalance: user.balance });
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

// 7. Get Contests for a Match
app.get('/api/contests/:matchId', async (req, res) => {
    try {
        const contests = await Contest.find({ matchId: req.params.matchId });
        res.json({ status: 'success', data: contests });
    } catch (err) {
        res.status(500).json({ status: 'error', message: err.message });
    }
});

// 8. Get Leaderboard for a Contest
app.get('/api/leaderboard/:contestId', async (req, res) => {
    try {
        const contest = await Contest.findById(req.params.contestId);
        if (!contest) return res.status(404).json({ message: 'Contest not found' });

        const teams = await UserTeam.find({ matchId: contest.matchId })
            .sort({ totalPoints: -1 })
            .limit(100);

        res.json({ status: 'success', data: teams });
    } catch (err) {
        res.status(500).json({ status: 'error', message: err.message });
    }
});

// 9. Deposit Request
app.post('/api/deposit', async (req, res) => {
    const { userId, amount, utr } = req.body;
    try {
        const txn = new Transaction({
            userId,
            amount: parseFloat(amount),
            type: 'DEPOSIT',
            utr
        });
        await txn.save();
        res.json({ status: 'success', message: 'Deposit request submitted' });
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

// 10. Submit KYC
app.post('/api/kyc/submit', async (req, res) => {
    const { userId, pan, aadhar } = req.body;
    try {
        await User.findOneAndUpdate(
            { phone: userId },
            {
                kycDetails: { pan, aadhar },
                kycStatus: 'PENDING'
            }
        );
        res.json({ status: 'success', message: 'KYC submitted' });
    } catch (err) {
        res.status(500).json({ message: err.message });
    }
});

// 11. Get User's Joined Contests
app.get('/api/myContests/:userId', async (req, res) => {
    try {
        const joined = await JoinedContest.find({ userId: req.params.userId })
            .populate('contestId')
            .sort({ timestamp: -1 });

        const data = joined.map(j => ({
            contestName: j.contestId ? j.contestId.name : "Contest",
            matchName: j.matchId,
            prizePool: j.contestId ? j.contestId.prizePool : "₹0",
            totalPoints: 0,
            rank: 0
        }));

        res.json({ status: 'success', data });
    } catch (err) {
        res.status(500).json({ status: 'error', message: err.message });
    }
});

// 12. Admin: Get Pending Transactions
app.get('/admin/transactions/pending', async (req, res) => {
    try {
        const txns = await Transaction.find({ status: 'PENDING', type: 'DEPOSIT' });
        res.json(txns);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// 13. Admin: Update Transaction Status
app.post('/admin/transactions/update', async (req, res) => {
    const { txnId, status } = req.body;
    try {
        const txn = await Transaction.findById(txnId);
        if (!txn) return res.status(404).json({ message: 'Transaction not found' });

        if (status === 'COMPLETED' && txn.status !== 'COMPLETED') {
            await User.findOneAndUpdate(
                { phone: txn.userId },
                { $inc: { balance: txn.amount } }
            );
        }

        txn.status = status;
        await txn.save();
        res.json({ status: 'success', message: `Transaction ${status}` });
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// 14. Admin: Get Pending KYC
app.get('/admin/kyc/pending', async (req, res) => {
    try {
        const users = await User.find({ kycStatus: 'PENDING' });
        res.json(users);
    } catch (err) {
        res.status(500).json({ error: err.message });
    }
});

// 15. Get User's Recent Transactions
app.get('/api/transactions/:userId', async (req, res) => {
    try {
        const txns = await Transaction.find({ userId: req.params.userId })
            .sort({ timestamp: -1 })
            .limit(10);
        res.json({ status: 'success', data: txns });
    } catch (err) {
        res.status(500).json({ status: 'error', message: err.message });
    }
});

// --- Points Calculation Logic (Server-Side) ---

async function calculateRealPoints(matchId) {
    try {
        console.log(`Calculating points for match: ${matchId}`);
        const apiKey = process.env.CRICKET_API_KEY || '5f93b276-c5ab-455c-8dd2-198904909842';
        // Fetch match scorecard/points from API
        const response = await axios.get(`https://api.cricapi.com/v1/match_points?apikey=${apiKey}&id=${matchId}`);
        const liveData = response.data.data; // This depends on API response structure

        if (!liveData || !liveData.points) return;

        // Map of Player ID -> Points
        const playerPointsMap = {};
        liveData.points.forEach(p => {
            playerPointsMap[p.id] = p.totalPoints; // Assuming API gives totalPoints
        });

        // Update all teams joined in this match
        const teams = await UserTeam.find({ matchId });
        for (let team of teams) {
            let total = 0;
            team.playerIds.forEach(pId => {
                let pPoints = playerPointsMap[pId] || 0;
                if (pId === team.captainId) pPoints *= 2;
                if (pId === team.viceCaptainId) pPoints *= 1.5;
                total += pPoints;
            });
            team.totalPoints = total;
            await team.save();
        }
        console.log(`Points updated for match ${matchId}`);
    } catch (e) {
        console.error("Points calculation error:", e.message);
    }
}

// Run points update every 10 minutes for active matches
setInterval(async () => {
    const activeMatches = await Match.find({ active: true, status: 'started' }); // Logic to find started matches
    for (let m of activeMatches) {
        await calculateRealPoints(m.id);
    }
}, 600000);

console.log("Setting up listener on port", PORT, "...");
app.listen(PORT, '0.0.0.0', () => {
    console.log(`SUCCESS: Server is actually running and listening on port ${PORT}`);
}).on('error', (err) => {
    console.error("SERVER LISTEN ERROR:", err);
});

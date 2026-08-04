const path = require('path');
console.log("--- STARTING SOLO11 ROOT SERVER ---");

try {
    const backendPath = path.join(__dirname, 'backend-node');
    console.log("Working Directory:", backendPath);
    process.chdir(backendPath);

    // Check if required Env Vars exist
    if (!process.env.MONGO_URI) {
        console.error("FATAL ERROR: MONGO_URI is not defined in Environment Variables!");
    }

    console.log("Attempting to load backend-node/server.js...");
    require('./server.js');
} catch (err) {
    console.error("!!! SERVER CRASHED AT STARTUP !!!");
    console.error("Error Message:", err.message);
    console.error("Stack Trace:", err.stack);
    process.exit(1);
}

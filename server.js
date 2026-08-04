const path = require('path');
console.log("--- STARTING SOLO11 ROOT SERVER ---");

// Catch all errors in the entire process
process.on('uncaughtException', (err) => {
    console.error("!!! UNCAUGHT EXCEPTION !!!");
    console.error(err.stack);
    process.exit(1);
});

process.on('unhandledRejection', (reason, promise) => {
    console.error("!!! UNHANDLED REJECTION !!!");
    console.error(reason);
    process.exit(1);
});

try {
    const backendPath = path.join(__dirname, 'backend-node');
    console.log("Current Working Directory:", process.cwd());
    console.log("Target Backend Path:", backendPath);

    process.chdir(backendPath);
    console.log("New Working Directory:", process.cwd());

    if (!process.env.MONGO_URI) {
        console.log("WARNING: MONGO_URI is missing from process.env");
    }

    console.log("Attempting to load backend-node/server.js...");
    require('./server.js');
    console.log("backend-node/server.js loaded successfully. Waiting for server to start...");
} catch (err) {
    console.error("!!! FAILED TO REQUIRE SERVER.JS !!!");
    console.error(err.stack);
    process.exit(1);
}

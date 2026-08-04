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

    // Change directory so that the backend can find its 'public' folder etc.
    process.chdir(backendPath);
    console.log("New Working Directory:", process.cwd());

    console.log("Attempting to load backend-node/server.js...");
    // IMPORTANT: require uses paths relative to this file, not process.cwd()
    require('./backend-node/server.js');
    console.log("backend-node/server.js execution finished.");
} catch (err) {
    console.error("!!! FAILED TO REQUIRE BACKEND SERVER.JS !!!");
    console.error("Error Message:", err.message);
    console.error("Stack Trace:", err.stack);
    process.exit(1);
}

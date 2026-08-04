const path = require('path');
console.log("Starting Root Server...");

try {
    const backendPath = path.join(__dirname, 'backend-node');
    console.log("Changing directory to:", backendPath);
    process.chdir(backendPath);

    console.log("Loading backend-node/server.js...");
    require('./server.js');
} catch (err) {
    console.error("SERVER CRASH ERROR:", err);
    process.exit(1);
}

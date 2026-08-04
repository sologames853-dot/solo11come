const path = require('path');

// Change the current working directory to 'backend-node'
// This ensures that relative paths like 'public/admin' inside backend-node/server.js work correctly.
process.chdir(path.join(__dirname, 'backend-node'));

// Load the actual server file
require('./server.js');

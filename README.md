Purchase API Backend
A Clojure REST API for the Purchase mobile application.

Project Structure


1
2
3
4
5
6
7
be_purchase/
├── src/
│   └── purchase/
│       └── core.clj          # Main application entry point
├── deps.edn                  # Project dependencies and aliases
├── README.md                 # This file
└── .gitignore                # Git ignore file
Prerequisites
Clojure CLI
Java JDK 8 or later
Development Setup
1. Clone the repository
bash


1
2
git clone <repository-url>
cd be_purchase
2. Start the development REPL
bash


1
clojure -M:dev
This will start an nREPL server (note the port number displayed).

3. Connect your editor
In your Clojure editor (Neovim with Conjure, Emacs, etc.), connect to the nREPL server:

vim


1
:ConjureConnect localhost <port_number>
Starting the Server
Method 1: From the REPL (Development)
In your REPL or editor:

clojure


1
(purchase.core/start-server)
Method 2: Direct execution
bash


1
clojure -M:run
The server will start on port 3000.

API Endpoints
Health Check
bash


1
GET http://localhost:3000/health
Response:

json


1
{"status":"healthy"}
Main Endpoint
bash


1
GET http://localhost:3000/
Response:

json


1
{"message":"Opa deu certo"}
Development Workflow
In Neovim with Conjure:
Reload file changes: :ConjureEvalBuf
Start server: :ConjureEval (purchase.core/start-server)
Stop server: :ConjureEval (purchase.core/stop-server)
Restart server: :ConjureEval (purchase.core/restart-server)
Auto-reload on save:
The buffer automatically evaluates when you save .clj files.

Project Commands
clojure -M:dev
Start development REPL with nREPL
clojure -M:run
Run the application directly
:ConjureEvalBuf
Reload current buffer in REPL
:ConjureEval (purchase.core/start-server)
Start the web server
:ConjureEval (purchase.core/stop-server)
Stop the web server
Testing Endpoints
bash


1
2
3
4
5
# Health check
curl http://localhost:3000/health

# Main endpoint
curl http://localhost:3000/
Dependencies
ring/ring: Web application library
compojure/compojure: Routing library
ring/ring-json: JSON middleware
cheshire/cheshire: JSON parsing
Environment Variables
PORT: Server port (defaults to 3000)
Example:

bash


1
PORT=8080 clojure -M:run

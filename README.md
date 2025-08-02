# Purchase API Backend
A Clojure REST API for the Purchase mobile application.

## Prerequisites
 - Clojure CLI
 - Java JDK 8 or later

## Development Setup
1. Clone the repository

```bash
git clone <repository-url>
cd be_purchase
```

2. Start the development REPL
```bash
clojure -M:dev
```
This will start an nREPL server (note the port number displayed).

3. Start the server
```bash
(require '[purchase.core :as core]) ; Load the namespace into the REPL session
(core/start-server) ; Actually start the server process
```

3. Connect your editor
In your Clojure editor (Neovim with Conjure, Emacs, etc.), connect to the nREPL server:
```vim
:ConjureConnect localhost <port_number>
```

## Starting the Server
Method 1: From the REPL (Development)

In your REPL or editor:
```Clojure
(purchase.core/start-server)
```

Method 2: Direct execution
```bash
clojure -M:run
```
The server will start on port 3000.

## API Endpoints

Health Check
```bash
GET http://localhost:3000/health
```

Response:
```json
{"status":"healthy"}
```

Main Endpoint
```bash
GET http://localhost:3000/
```

Response:
```json
{"message":"Opa deu certo"}
```

## Development Workflow
In Neovim with Conjure:
1. Reload file changes: :ConjureEvalBuf
2. Start server: :ConjureEval (purchase.core/start-server)
3. Stop server: :ConjureEval (purchase.core/stop-server)
4. Restart server: :ConjureEval (purchase.core/restart-server)

Auto-reload on save:

The buffer automatically evaluates when you save .clj files.

## Project Commands
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

## Testing Endpoints
```bash
# Health check
curl http://localhost:3000/health

# Main endpoint
curl http://localhost:3000/
```

## Dependencies
 - ring/ring: Web application library
 - compojure/compojure: Routing library
 - ring/ring-json: JSON middleware
 - cheshire/cheshire: JSON parsing

## Environment Variables
```bash
PORT: Server port (defaults to 3000)
```

## Example:
```bash
PORT=8080 clojure -M:run
```

(ns purchase.config
  (:require
   [lambdaisland.dotenv :as dotenv]
   [purchase.utils :as utils]))
; --- Load and parse the .env file ---
(def dotenv-vars
  (try
    (let [env-file-content (slurp ".env")] ; Read the .env file as a string
      (dotenv/parse-dotenv env-file-content)) ; Parse the string content
    (catch java.io.FileNotFoundException e
      (println "Warning: .env file not found. Using defaults and system environment.")
      {}) ; Return an empty map if .env is not found
    (catch Exception e
      (println "Warning: Error parsing .env file:" (.getMessage e))
      {})))

; --- Default configuration map ---
(def default-app-config
  {:db {:dbtype "postgresql"
        :dbname "purchase_dev"
        :dbhost "localhost"
        :dbport 5432
        :dbuser "purchase_user"
        :dbpass "nosecret"}
   :server {:svport 3000}})

; --- Function to load configuration ---
(defn load-app-config-from-env []
  "Loads configuration.
   Prioritizes values from the parsed .env file (dotenv-vars),
   then System environment variables,
   then defaults."
  (let [; --- Database Configuration ---
        db-name     (or (get dotenv-vars "DB_NAME") ; Check parsed .env map FIRST
                        (System/getenv "DB_NAME")   ; Fallback to system env
                        (:dbname (:db default-app-config))) ; Final fallback
        db-user     (or (get dotenv-vars "DB_USER")
                        (System/getenv "DB_USER")
                        (:dbuser (:db default-app-config)))
        db-pass     (or (get dotenv-vars "DB_PASSWORD") ; Check common keys
                        (get dotenv-vars "DB_PASS")
                        (System/getenv "DB_PASSWORD")
                        (System/getenv "DB_PASS")
                        (:dbpass (:db default-app-config)))
        db-host     (or (get dotenv-vars "DB_HOST")
                        (System/getenv "DB_HOST")
                        (:dbhost (:db default-app-config)))
        db-port-str (or (get dotenv-vars "DB_PORT")
                        (System/getenv "DB_PORT"))
        db-port     (or (utils/parse-int db-port-str) ; Parse string to int
                        (:dbport (:db default-app-config)))
        ; --- Server Configuration ---
        server-port-str (or (get dotenv-vars "PORT")
                            (System/getenv "PORT"))
        server-port     (or (utils/parse-int server-port-str)
                            (:svport (:server default-app-config)))]
    ; --- Construct and return the configuration map ---
    {:db {:dbtype "postgresql"
          :dbname db-name
          :dbuser db-user  ; <-- Fixed typo: was :dbuer
          :dbpass db-pass
          :dbhost db-host
          :dbport db-port}
     :server {:svport server-port}}))

; --- Load configuration once when namespace is loaded ---
(def app-config (load-app-config-from-env))

; --- Public function to access configuration ---
(defn get-config [] app-config) ; <-- Fixed typo: was wqapp-config
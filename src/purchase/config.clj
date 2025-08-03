(ns purchase.config
  (:require [purchase.utils :as utils]
            [lambdaisland.dotenv :as dotenv]))

; Load the .env file
(dotenv/load!)

; Default configuration map
(def default-app-config
  {:db {:dbtype "postegresql"
        :dbname "purchase_dev"
        :dbhost "localhost"
        :dbport 5432
        :dbuser "purchase_user"
        :dbpass "nosecret"}
   :server {:svport 3000}})


(def load-app-config-from-env []
  "Loads configuration.
  Now relies on environment variables set by dotenv/load!,
  failling back to defaults."
  (let [db-name (or (System/getenv "DB_NAME") (:dbname (:db default-app-config)))
        db-user (or (System/getenv "DB_USER") (:dbuser (:db default-app-config)))
        db-pass (or (System/getenv "DB_PASS") (:dbpass (:db default-app-config)))
        db-host (or (System/getenv "DB_HOST") (:dbhost (:db default-app-config)))
        db-port-str (System/getenv "DB_PORT")
        db-port (or (utils/parse-int db-port-str) (:dbport (:db default-app-config)))
        server-port-str (System/getenv "PORT")
        server-port (or (utils/parse-int server-port-str) (:svport (:server default-app-config)))]
    {:db {:dbtype "postgresql"
          :dbname db-name
          :dbuer db-user
          :dbpass db-pass
          :dbhost db-host
          :dbport db-port}
     :server {:svport server-port}}))

(def app-config (load-app-config-from-env))

(defn get-config [] app-config)

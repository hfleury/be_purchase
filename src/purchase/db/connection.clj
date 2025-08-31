(ns purchase.db.connection
  (:require [next.jdbc :as jdbc]
            [next.jdbc.connection :as connection]
            [purchase.config :as config])) ; Require your config

; Get the database specification map from your config
(def db-spec (:db (config/get-config)))

(defonce db (atom nil))

(defn create-connection []
  (println "Creating database connection pool with spec:" db-spec)
  (connection/->pool
    {:factory-class com.zaxxer.hikari.HikariDataSource}
    db-spec))

(defn get-connection []
  (when (nil? @db)
    (println "Initializing database connection pool...")
    (reset! db (create-connection))
    (println "Database connection pool initialized."))
  @db)

(defn close-connection []
  (when-not (nil? @db)
    (println "Closing database connection pool...")
    (.close @db)
    (reset! db nil)
    (println "Database connection pool closed.")))

; Consider if you need this pattern or if direct jdbc/execute! calls are simpler
; For now, let's keep it but use it correctly with the connection
(defn with-transaction [f]
  (jdbc/with-transaction [tx (get-connection)]
    (f tx)))

; Export the db-spec if needed by other parts directly
(defn get-db-spec [] db-spec)

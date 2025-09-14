(ns purchase.db.migration
  (:require [migratus.core :as migratus]
            [purchase.config :as config] ; To get DB config
            [clojure.tools.logging :as log]))

(defn migratus-config []
  "Generates the configuration map for migratus based on application config."
  (let [app-config (config/get-config)
        db-spec (:db app-config)]
    {:store :database
     :migration-dir "migrations"
     :migration-table-name "migratus_migrations"
     :db db-spec}))

(defn migrate-up []
  "Applies all pending migrations."
  (log/info "Applying pending database migrations...")
  (migratus/migrate (migratus-config))
  (log/info "Database migrations applied."))

(defn migrate-down []
  "Rolls back the last applied migration."
  (log/info "Rolling back the last database migration...")
  (migratus/rollback (migratus-config))
  (log/info "Last database migration rolled back."))

(defn create-migration [name]
  "Creates a new pair of up/down migration files.
   Example: (create-migration \"add-soft-delete-to-products\")"
  (log/info "Creating new migration:" name)
  (migratus/create (migratus-config) name)
  (log/info "New migration files created in resources/migrations/"))

; Optional: Function to run during app startup to ensure DB is up-to-date
(defn run-pending-migrations []
  "Checks for and applies any pending migrations."
  (log/info "Checking for pending database migrations...")
  (try
    (migrate-up)
    (catch Exception e
      (log/error "Failed to run pending migrations:" (.getMessage e))
      (throw e))))

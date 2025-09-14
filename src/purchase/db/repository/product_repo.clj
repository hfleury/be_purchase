(ns purchase.db.repository.product-repo
  (:require [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql]
            [purchase.db.connection :as db]
            [clojure.tools.logging :as log]))

; --- CRUD Operations ---

(defn find-all []
  (log/debug "Fetching all products from database")
  (let [conn (db/get-connection)]
    (try
      (jdbc/execute! conn ["SELECT * FROM products WHERE deleted_at IS NULL ORDER BY created_at DESC"])
      (catch Exception e
        (log/error "Error executing query find-all:" (.getMessage e) (ex-data e))
        (.printStackTrace e)
        (throw e)))))

(defn find-by-id [id]
  (log/debug "Fetching product by ID:" id)
  (let [conn (db/get-connection)]
    (try
      (first (jdbc/execute! conn ["SELECT * FROM products WHERE id = ?::uuid" id]))
      (catch Exception e
        (log/error "Error executing query in find-by-id" (.getMessage e) (ex-data e))
        (.printStackTrace e)
        (throw e)))))

(defn create [product-data]
  (log/debug "Creating new product with data:" product-data)
  ; next.jdbc.sql/insert! expects column names as keywords matching the map keys
  ; It also automatically converts snake_case keys to snake_case column names
  ; The returned map from insert! often includes generated keys and metadata
  (let [conn (db/get-connection)
        ; Prepare data for insertion, ensuring keys match column names
        insert-data (select-keys product-data [:name :price :description])]
    (first ; insert! returns a vector, get the first (and usually only) result
     (sql/insert! conn :products insert-data))))

(defn update-product [id product-data]
  (log/debug "Updating product ID:" id "with data:" product-data)
  (let [conn (db/get-connection)
        allowed-fields #{:name :price :description}
        ; Prepare data for update, excluding the ID
        update-data (select-keys product-data allowed-fields)
        filtered-update-data (into {} (filter (fn [[_ v]] (some? v)) update-data))
        fields-to-update (keys filtered-update-data)]
    (if (empty? fields-to-update)
      (do
        (log/info "No valid fields to update product ID:" id)
        nil)
      (let [set-clauses (map #(str (name %) " = ?") fields-to-update)
            set-clause-string (clojure.string/join ", " set-clauses)
            values (map filtered-update-data fields-to-update)
            sql-params (vec (concat [(str "UPDATE products SET" set-clause-string " WHERE id = ?::uuid")] values [id]))]
        (try
          (let [result (jdbc/execute-one! conn sql-params {:return-keys true})]
            (find-by-id id))
          (catch Exception e
            (log/error "Error updating ID: " id " Error: " (.getMessage e) (ex-data e))
            (.printStackTrace e)
            (throw e)))))))



(defn delete [id]
  (log/debug "Deleting product ID:" id)
  (let [conn (db/get-connection)]
    (try
      (let [sql-params ["UPDATE products SET deleted_at = CURRENT_TIMESTAMP WHERE id = ?::uuid" id]
            result (jdbc/execute-one! conn sql-params {:return-keys true})]
        (log/debug "Soft delete execute for ID: " id "Result: " result)
        true)
      (catch Exception e
        (log/error "Error soft delete product ID: " id " Error: " (.getMessage e) (ex-data e))
        (.printStackTrace e)
        (throw e)))))

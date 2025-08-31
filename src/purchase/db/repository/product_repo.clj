(ns purchase.db.repository.product-repo
  (:require [next.jdbc :as jdbc]
            [next.jdbc.sql :as sql] ; For convenient insert!, query, etc.
            [purchase.db.connection :as db]
            [clojure.tools.logging :as log]))

; --- CRUD Operations ---

(defn find-all []
  (log/debug "Fetching all products from database")
  (let [conn (db/get-connection)]
    (sql/query conn ["SELECT * FROM products ORDER BY created_at DESC"])))

(defn find-by-id [id]
  (log/debug "Fetching product by ID:" id)
  (let [conn (db/get-connection)]
    (first (sql/query conn ["SELECT * FROM products WHERE id = ?" id]))))

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
        ; Prepare data for update, excluding the ID
        update-data (select-keys product-data [:name :price :description])]
    ; next.jdbc.sql/update! updates rows matching the WHERE condition
    ; It returns the number of rows affected
    (let [rows-affected (sql/update! conn :products update-data {:id id})]
      (if (> rows-affected 0)
        ; If updated, fetch the updated record to return it
        (find-by-id id)
        ; If no rows affected, indicate not found
        nil))))

(defn delete [id]
  (log/debug "Deleting product ID:" id)
  (let [conn (db/get-connection)]
    ; next.jdbc.sql/delete! deletes rows matching the WHERE condition
    ; It returns the number of rows affected
    (let [rows-affected (sql/delete! conn :products {:id id})]
      (> rows-affected 0)))) ; Return true if something was deleted

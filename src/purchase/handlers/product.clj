(ns purchase.handlers.product
  (:require
   [clojure.tools.logging :as log]
   [purchase.db.repository.product-repo :as product-repo] ; Use DB repo
   [ring.util.response :as response])) ; If you still want to log config

(defn list-products [request]
  (log/info "Handling list-products request")
  ; Optional: Log config if needed for debugging
  ; (log/debug "Current config:" (config/get-config))
  (try
    (let [products (product-repo/find-all)]
      (log/info "Found" (count products) "products")
      (response/response {:products products}))
    (catch Exception e
      (log/error "Failed to fetch products:" (.getMessage e) (ex-data e))
      (-> (response/response {:error "Failed to fetch products" :message (.getMessage e)})
          (response/status 500)))))

(defn get-product [request]
  (log/info "Handling get-product request")
  (try
    (let [id-str (get-in request [:params :id])
          ; Basic validation - UUID format check could be added
          _ (when-not id-str
              (throw (ex-info "Product ID is required" {:type :validation})))
          product (product-repo/find-by-id id-str)] ; Pass ID string directly
      (if product
        (do
          (log/info "Found product with id" id-str)
          (response/response {:product product}))
        (do
          (log/warn "Product not found with id" id-str)
          (response/not-found {:error "Product not found"}))))
    (catch clojure.lang.ExceptionInfo ei
      (if (= (:type (ex-data ei)) :validation)
        (do
          (log/warn "Validation error in get-product:" (.getMessage ei))
          (response/bad-request {:error "Invalid request" :message (.getMessage ei)}))
        (do
          (log/error "Failed to fetch product:" (.getMessage ei) (ex-data ei))
          (-> (response/response {:error "Failed to fetch product" :message (.getMessage ei)})
              (response/status 500)))))
    (catch Exception e
      (log/error "Failed to fetch product:" (.getMessage e) (ex-data e))
      (-> (response/response {:error "Failed to fetch product" :message (.getMessage e)})
          (response/status 500)))))

(defn create-product [request]
  (log/info "Handling create-product request")
  (try
    (let [product-data (-> request :body)
          required-fields [:name :price]
          missing-fields (remove #(contains? product-data %) required-fields)]
      (if (seq missing-fields)
        (do
          (log/warn "Missing required fields:" missing-fields)
          (response/bad-request {:error "Missing required fields" :fields missing-fields}))
        (let [new-product (product-repo/create product-data)]
          (log/info "Created new product with id" (:id new-product))
          ; Use 201 Created status and Location header
          (response/created (str "/api/products/" (:id new-product)) ; Location header
                            {:product new-product}))))
    (catch Exception e
      (log/error "Failed to create product:" (.getMessage e) (ex-data e))
      (-> (response/response {:error "Failed to fetch products" :message (.getMessage e)})
          (response/status 500)))))

(defn update-product [request]
  (log/info "Handling update-product request")
  (try
    (let [id-str (get-in request [:params :id])
          _ (when-not id-str
              (throw (ex-info "Product ID is required" {:type :validation})))
          product-data (-> request :body)
          updated-product (product-repo/update-product id-str product-data)] ; Pass ID and data
      (if updated-product
        (do
          (log/info "Updated product with id" id-str)
          (response/response {:product updated-product}))
        (do
          (log/warn "Product not found for update with id" id-str)
          (response/not-found {:error "Product not found"}))))
    (catch clojure.lang.ExceptionInfo ei
      (if (= (:type (ex-data ei)) :validation)
        (do
          (log/warn "Validation error in update-product:" (.getMessage ei))
          (response/bad-request {:error "Invalid request" :message (.getMessage ei)}))
        (do
          (log/error "Failed to update product:" (.getMessage ei) (ex-data ei))
          (-> (response/response {:error "Failed to update product" :message (.getMessage ei)})
              (response/status 500)))))
    (catch Exception e
      (log/error "Failed to update product:" (.getMessage e) (ex-data e))
      (-> (response/response {:error "Failed to update product" :message (.getMessage e)})
          (response/status 500)))))

(defn delete-product [request]
  (log/info "Handling delete-product request")
  (try
    (let [id-str (get-in request [:params :id])
          _ (when-not id-str
              (throw (ex-info "Product ID is required" {:type :validation})))
          deleted? (product-repo/delete id-str)] ; Pass ID string
      (if deleted?
        (do
          (log/info "Deleted product with id" id-str)
          (response/response {:message "Product deleted"}))
        (do
          (log/warn "Product not found for deletion with id" id-str)
          (response/not-found {:error "Product not found"}))))
    (catch clojure.lang.ExceptionInfo ei
      (if (= (:type (ex-data ei)) :validation)
        (do
          (log/warn "Validation error in delete-product:" (.getMessage ei))
          (response/bad-request {:error "Invalid request" :message (.getMessage ei)}))
        (do
          (log/error "Failed to delete product:" (.getMessage ei) (ex-data ei))
          (-> (response/response {:error "Failed to delete product" :message (.getMessage ei)})
              (response/status 500)))))
    (catch Exception e
      (log/error "Failed to delete product:" (.getMessage e) (ex-data e))
      (-> (response/response {:error "Failed to delete product" :message (.getMessage e)})
          (response/status 500)))))

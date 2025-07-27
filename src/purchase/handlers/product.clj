(ns purchase.handlers.product
  (:require [ring.util.response :as response]))

;; For now, use in-memory storage (replace with database later)
(def products (atom []))
(def product-id-counter (atom 0))

(defn list-products [request]
  (response/response {:products @products}))

(defn get-product [request]
  (let [id (Integer/parseInt (get-in request [:params :id]))
        product (first (filter #(= (:id %) id) @products))]
    (if product
      (response/response {:product product})
      (response/not-found {:error "Product not found"}))))

(defn create-product [request]
  (let [product-data (-> request :body)
        new-id (swap! product-id-counter inc)
        new-product (assoc product-data :id new-id)]
    (swap! products conj new-product)
    (response/response {:product new-product})))

(defn update-product [request]
  (let [id (Integer/parseInt (get-in request [:params :id]))
        product-data (-> request :body)
        updated-product (assoc product-data :id id)]
    (swap! products
           (fn [products]
             (map #(if (= (:id %) id) updated-product %) products)))
    (response/response {:product updated-product})))

(defn delete-product [request]
  (let [id (Integer/parseInt (get-in request [:params :id]))]
    (swap! products
           (fn [products]
             (remove #(= (:id %) id) products)))
    (response/response {:message "Product deleted"})))

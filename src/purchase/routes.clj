(ns purchase.routes
  (:require [compojure.core :refer [defroutes GET POST PUT DELETE]]
            [compojure.route :as route]
            [ring.util.response :as response]
            [purchase.handlers.product :as product-handler]))

(defroutes health-routes
  ;; Health routes
  (GET "/" [] (response/response {:message "Tudo certo!"}))
  (GET "/health" [] (response/response {:status "Healthy"})))

(defroutes product-routes
  ;; Product routes
  (GET "/api/products" [] product-handler/list-products)
  (GET "/api/products/:id" [id] product-handler/get-product)
  (POST "/api/products" [product-data] product-handler/create-product)
  (PUT "/api/products/:id" [id product-data] product-handler/update-product)
  (DELETE "/api/products/:id" [id] product-handler/delete-product))

(defroutes app-routes
  health-routes
  product-routes
  (route/not-found {:error "Not Found"}))

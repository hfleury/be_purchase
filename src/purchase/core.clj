(ns purchase.core
  (:require [ring.adapter.jetty :as jetty]
           [ring.middleware.json :as json]
           [compojure.core :refer [defroutes GET]]
           [compojure.route :as route]
           [ring.util.response :as response])
  (:gen-class))

(defroutes app-routes
  (GET "/" [] (response/response {:message "Opa deu certo"}))
  (GET "/health" [] (response/response {:status "healthy"}))
  (route/not-found {:error "Not Found"}))

(def app
  (-> app-routes
      json/wrap-json-response
      (json/wrap-json-body {:keywords? true})))

(defn -main [& args]
  (let [port (Integer/parseInt (or (System/getenv "PORT") "3000"))]
    (println "Iniciando o servidor na porta" port)
    (jetty/run-jetty app {:port port :join? true})))

(def server (atom nil))

(defn start-server []
  (reset! server (jetty/run-jetty #'app {:port 3000 :join? false}))
  (println "Servidor iniciado na porta 3000"))

(defn stop-server []
  (when @server
    (.stop @server)
    (reset! server nil)
    (println "Server parou")))

(defn restart-server []
   (stop-server)
   (start-server))

(ns purchase.core
  (:require [ring.adapter.jetty :as jetty]
           [ring.middleware.json :as json]
           [purchase.routes :refer [app-routes]])
  (:gen-class))


(def app
  (-> app-routes
      json/wrap-json-response
      (json/wrap-json-body {:keywords? true})))

(defn -main [& _args]
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

(ns gr-hw.system
  (:require [org.httpkit.server :as httpkit]
            [system.core :refer [defsystem]]
            (system.components
             [http-kit :refer [new-web-server]]
             [h2 :refer [new-h2-database]])
            [compojure.api.middleware :refer [wrap-components]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer [wrap-json-response]]
            [com.stuartsierra.component :as component]
            [reloaded.repl :refer [go set-init!]]
            [gr-hw.handler :refer [app]]
            [gr-hw.data :as d :refer [load-csv-data csv-data]]
            [environ.core :refer [env]]))

(defrecord DataSet [data-sources]
  component/Lifecycle
  (start [this]
    (load-csv-data data-sources csv-data)
    (assoc this :data @csv-data))
  (stop [this]
    (reset! (:data this) [])))

(defn init-data [data-sources]
  (map->DataSet {:data-sources data-sources}))


(defsystem dev-system
  [:data (init-data d/data-files)
   :web (new-web-server (Integer/parseInt (env :http-port)) app)])

(ns gr-hw.system
  (:require [org.httpkit.server :as httpkit]
            [system.core :refer [defsystem]]
            (system.components
             [http-kit :refer [new-web-server]]
             [middleware :refer [new-middleware]]
             [repl-server :refer [new-repl-server]])
            [compojure.api.middleware :refer [wrap-components]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.json :refer [wrap-json-response]]
            [com.stuartsierra.component :as component]
            [gr-hw.handler :refer [app]]
            [gr-hw.data :as d :refer [load-csv-data csv-data]]
            [environ.core :refer [env]]))


(defn init-data [data-sources]
  (load-csv-data data-sources csv-data))


(defsystem dev-system
  [:data (init-data d/data-files)
   :middleware (new-middleware {:middleware [[wrap-components]
                                             [wrap-reload]
                                             [wrap-json-response]]})
   :web (new-web-server 3000 app)
   :repl-server (new-repl-server 31337)])

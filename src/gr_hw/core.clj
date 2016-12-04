(ns gr-hw.core
  (:require [gr-hw.system :refer [dev-system]]
            [com.stuartsierra.component :as component])
  (:gen-class))


(defn -main [& args]
  "Entry point"
  (component/start (dev-system)))

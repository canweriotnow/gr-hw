(ns gr-hw.core
  (:require [gr-hw.system :refer [dev-system]]
            [com.stuartsierra.component :as component]
            [system.repl :refer [set-init! start]])
  (:gen-class))


(defn -main [& args]
  (set-init! #'dev-system)
  (start))

(defn test-ns-hook []
  (set-init! #'dev-system)
  (start))

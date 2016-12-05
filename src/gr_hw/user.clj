(ns gr-hw.user
  (:require [system.repl :refer [system set-init! start stop reset]]
            [gr-hw.system :refer [dev-system]]))

(set-init! #'dev-system)

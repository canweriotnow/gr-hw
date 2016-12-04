(ns gr-hw.user
  (:require [reloaded.repl :refer [system init start stop go reset reset-all]]
            [system.repl :refer [system set-init! start stop reset]]
            [gr-hw.system :refer [new-system]]))

(set-init! #'new-system)

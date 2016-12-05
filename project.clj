(defproject gr-hw "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [metosin/compojure-api "1.1.8"]
                 [ring "1.5.0"]
                 [ring/ring-json "0.4.0"]
                 [org.clojure/data.json "0.2.6"]
                 [org.clojure/data.csv "0.1.3"]
                 [semantic-csv "0.1.0"]
                 [org.clojure/java.jdbc "0.7.0-alpha1"]
                 [com.stuartsierra/component "0.3.1"]
                 [org.danielsz/system "0.3.1"]
                 [http-kit "2.2.0"]
                 [reloaded.repl "0.2.3"]
                 [environ "1.1.0"]]
  :ring {:handler gr-hw.handler/app}
  :main gr-hw.core
  :uberjar-name "server.jar"
  :profiles {:dev {:dependencies [[javax.servlet/javax.servlet-api "3.1.0"]]
                   :plugins [[lein-ring "0.10.0"]]}}
  :plugins [[lein-environ "1.1.0"]])

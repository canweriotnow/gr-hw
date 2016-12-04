(ns gr-hw.data
  (:require [clojure.data.csv :as csv]
            [semantic-csv.core :as sc :refer :all]
            [clojure.java.io :as io]
            [clojure.string :as str])
  (:import (java.util Date)))

(def data-files
  [["resources/data/data1.csv" \|]
   ["resources/data/data2.csv" \,]
   ["resources/data/data3.csv" \space]])

(def csv-data (atom []))

(defn split-on-delimiter [line]
  (cond
    (str/includes? line "|")
    (str/split line #"\|")
    (str/includes? line " ")
    (str/split line #"\s")
    (str/includes? line ",")
    (str/split line #",")))

(defn map-data [d]
  (let [ks [:LastName :FirstName :Gender :FavoriteColor :DateOfBirth]
        vs (split-on-delimiter d)]
    (zipmap ks vs)))

(defn read-data [file separator]
  (with-open [in-file (io/reader file)]
    (->>
     (csv/read-csv in-file :separator separator)
     mappify
     doall)))

(defn load-csv-data [data-files state-atom]
  (for [fh data-files]
    (let [f (first fh)
          s (last fh)
          data (read-data f s)]
      (swap! state-atom concat data))))

;; Sorting Functions

(defn gender-sort [ms]
  (sort-by (juxt :Gender :LastName) ms))

(defn dob-sort [ms]
  (sort-by
   (fn [m]
     (Date/parse (:DateOfBirth m)))
           ms))

(defn lastn-sort [ms]
  (->>
   ms
   (sort-by :LastName)
   reverse))

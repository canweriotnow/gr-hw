(ns gr-hw.data-test
  (:require [clojure.test :refer :all]
            [gr-hw.data :as data]
            [clojure.java.io :as io]
            [clojure.string :as str])
  (:import (java.util Date)))


(def data-atom (atom []))

(defn data-setup [test]
  (reset! data-atom [])
  (test))

(deftest data-files
  (testing "Data files actually exist"
    (let [files (map first data/data-files)]
      (is (map #(.exists io/as-file %) files))))
  (testing "Files have expected delimiters"
    (for [file data/data-files]
      (let [fh (first file)
            del (last file)]
        (with-open [rdr (io/reader fh)]
          (doseq [line (line-seq rdr)]
            (is (str/includes? line del))))))))

(deftest read-data
  (testing "Read CSV file"
    (for [files data/data-files]
      (doseq [data (data/read-data (first files) (last files))]
        (is (instance? clojure.lang.PersistentArrayMap data))))))

(deftest sorting-fns
  (let [test-data (first (data/load-csv-data data/data-files (atom [])))]
    (testing "Gender Sort"
      (let [sorted (data/gender-sort test-data)]
        (is (= "Female" (:Gender (first sorted))))
        (is (= "Male" (:Gender (last sorted))))))
    (testing "DOB Sort (ASC)"
        (let [sorted (data/dob-sort test-data)]
          (is (< (Date/parse (:DateOfBirth (first sorted)))
                 (Date/parse (:DateOfBirth (second sorted)))))))
    (testing "LastName Sort (DESC)"
      (for [pair (partition 2 (data/lastn-sort test-data))]
        (is (> 1 (compare (:LastName (first pair)) (:LastName (last pair)))))))))

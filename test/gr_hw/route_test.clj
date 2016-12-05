(ns gr-hw.route-test
  (:require [clojure.test :refer :all]
            [clojure.string :as str]
            [gr-hw.core]
            [org.httpkit.client :as http]
            [clojure.data.json :as json])
  (:import [java.util Date]))

(test-ns 'gr-hw.core)

(defn post-record [delimiter]
  (let [fields ["Bar" "Foo" "Male" "Cerulean" "01/01/1970"]
        data-str (str/join delimiter fields)]
    (http/post "http://localhost:3000/records"
               {:body (json/write-str {"record" data-str})
                :headers {"Content-Type" "application/json"}})))

(def result {:LastName "Bar"
             :FirstName "Foo"
             :Gender "Male"
             :FavoriteColor "Cerulean"
             :DateOfBirth "01/01/1970"})


(deftest post-data
  (testing "Posting pipe-delimited data"
    (let [request (post-record \|)]
      (is (= 200 (:status @request)))
      (is (= result (json/read-json (:body @request) true)))))
  (testing "Posting comma-delimited data"
    (let [request (post-record \,)]
      (is (= 200 (:status @request)))
      (is (= result (json/read-json (:body @request) true)))))
  (testing "Posting space-delimited data"
    (let [request (post-record \space)]
      (is (= 200 (:status @request)))
      (is (= result (json/read-json (:body @request) true))))))

(defn req-gender []
  (http/get "http://localhost:3000/records/gender"
            {:headers {"Accept" "application/json"}}))

(defn req-dob []
  (http/get "http://localhost:3000/records/birthdate"
            {:headers {"Accept" "application/json"}}))

(defn req-name []
  (http/get "http://localhost:3000/records/name"
            {:headers {"Accept" "application/json"}}))

(deftest data-routes
  (testing "Results ordered by gender"
    (let [req @(req-gender)
          data (json/read-json (:body req) true)]
      (is (= 200 (:status req)))
      (is (= "Female" (:Gender (first data))))
      (is (= "Male" (:Gender (last data))))))
  (testing "Results ordered by birthdate"
    (let [req @(req-dob)
          data (json/read-json (:body req) true)]
      (is (= 200 (:status req)))
      (is (<= (Date/parse (:DateOfBirth (first data)))
             (Date/parse (:DateOfBirth (last data)))))))
  (testing "Results ordered by name"
    (let [req @(req-name)
          data (json/read-json (:body req) true)]
      (is (= 200 (:status req)))
      (for [pair (partition 2 data)]
        (is (>= 1 (compare (:LastName (first pair)) (:LastName (last pair)))))))))

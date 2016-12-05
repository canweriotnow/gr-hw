(ns gr-hw.route-test
  (:require [clojure.test :refer :all]
            [clojure.string :as str]
            [gr-hw.core]
            [org.httpkit.client :as http]
            [clojure.data.json :as json]))

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

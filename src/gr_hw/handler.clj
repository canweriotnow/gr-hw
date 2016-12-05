(ns gr-hw.handler
  (:require [compojure.api.sweet :refer :all]
            [ring.util.http-response :refer :all]
            [schema.core :as s]
            [schema.coerce :as coerce]
            [gr-hw.data :as d]))


(s/defschema Person
  {:FirstName s/Str
   :LastName  s/Str
   :Gender    (s/enum "Male" "Female")
   :FavoriteColor s/Str
   :DateOfBirth s/Str}) ;; TODO: Validate date format MM/DD/YYYY

(def app
  (api
    {:swagger
     {:ui "/"
      :spec "/swagger.json"
      :data {:info {:title "Gr-hw"
                    :description "Guaranteed Rate Homework"}
             :tags [{:name "api", :description "some apis"}]}}}

    (context "/records" []
      :tags ["api"]

      (POST "/" [record]
            :return Person
            :body-params [record :- s/Str]
            :summary "Adds a record!"
            (try
              (let [dmap (d/map-data record)]
                (s/validate Person dmap)
                (swap! d/csv-data conj dmap)
                (ok dmap))
              (catch Exception e
                (internal-server-error {:error (str e)}))))

      (GET "/gender" []
           :return [Person]
           ;:body [persons [Person]]
           (ok (d/gender-sort @d/csv-data)))

      (GET "/birthdate" []
           :return [Person]
           ;:body [persons [Person]]
           (ok (d/dob-sort @d/csv-data)))

      (GET "/name" []
           :return [Person]
           ;:body [persons [Person]]
           (ok (d/lastn-sort @d/csv-data))))))

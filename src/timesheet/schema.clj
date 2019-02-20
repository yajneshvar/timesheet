(ns timesheet.schema
    "Contains custom resolvers and a function to provide the full schema."
    (:require
      [clojure.java.io :as io]
      [com.walmartlabs.lacinia.util :as util]
      [com.walmartlabs.lacinia.schema :as schema]
      [clojure.edn :as edn]))


      (defn user-by-id [user-map context args value]
        (let[{:keys [id]} args]
          (get user-map id)))

      (defn entity-map
        [data k]
        (reduce #(assoc %1 (:id %2) %2)
                {}
                (get data k)))

      (defn resolver-map
        []
        (let[ data (-> (io/resource "data.edn")
                      slurp
                      edn/read-string)
              user-map (entity-map data :users)
              address-map (entity-map data :addresses)]
                             {:query/user-by-id (partial user-by-id user-map)}))
      
      (defn load-schema
        []
        (-> (io/resource "schema.edn")
            slurp
            edn/read-string
            (util/attach-resolvers (resolver-map))
            schema/compile))
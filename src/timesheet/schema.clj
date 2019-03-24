(ns timesheet.schema
    "Contains custom resolvers and a function to provide the full schema."
    (:require
      [clojure.java.io :as io]
      [com.walmartlabs.lacinia.util :as util]
      [com.walmartlabs.lacinia.schema :as schema]
      [com.stuartsierra.component :as component]
      [clojure.edn :as edn]
      [timesheet.db :as db]
      [timesheet.records.record :as record]
      [clojure.tools.logging :as log]))


      (defn user-by-id 
        [db]
        (fn [_ args _] (db/find-user-by-id db (:id args))))

      (defn resolve-address-by-user
        [db]
        (fn [_ _ user] (db/find-address-by-user db (:id user))))

      (defn resolve-role-by-user
        [db]
        (fn [_ _ user] (
          db/list-roles-by-user db (:id user))))

      (defn entity-map
        [data k]
        (reduce #(assoc %1 (:id %2) %2)
                {}
                (get data k)))
     
      (defn create-user
        [context args val]
        (let [{name :name email :email roles :roles phone :phone address :address} args
              data (record/map->User {:name name :email email :roles roles :phone phone :address address})]
          (->> data
            pr-str
            (spit "new_data.json"))
          data))


      (defn resolver-map
        [component]
        (let[ db (:db component)]
                             {:query/user-by-id (user-by-id db)
                              :User/address (resolve-address-by-user db)
                              :User/role (resolve-role-by-user db)
                              :mutate/user create-user}))
      
      (defn load-schema
        [component]
        (-> (io/resource "schema.edn")
            slurp
            edn/read-string
            (util/attach-resolvers (resolver-map component))
            schema/compile))


(defrecord SchemaProvider [schema]
  component/Lifecycle
  
  (start [this]
    (assoc this :schema (load-schema this)))


  (stop [this]
    (assoc this :schema nil)))
  
  (defn new-schema-provider
    []
    {:schema-provider (-> {}
                        map->SchemaProvider
                        (component/using [:db]))})


          
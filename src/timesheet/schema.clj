(ns timesheet.schema
    "Contains custom resolvers and a function to provide the full schema."
    (:require
      [clojure.java.io :as io]
      [com.walmartlabs.lacinia.util :as util]
      [com.walmartlabs.lacinia.schema :as schema]
      [com.stuartsierra.component :as component]
      [clojure.edn :as edn]))


      (defn user-by-id [user-map context args value]
        (let[{:keys [id]} args]
          (get user-map id)))

      (defn resolve-address-by-user
        [address-map context args user]
        (-> user
            :address
            address-map))

      (defn resolve-role-by-user
        [role-map context args user]
        (->> user
            :roles
            (map role-map)))

      (defn entity-map
        [data k]
        (reduce #(assoc %1 (:id %2) %2)
                {}
                (get data k)))

      
      (defn create-user
        [context args val]
        (spit "new_data.json" )
      )

      (defn resolver-map
        [component]
        (let[ data (-> (io/resource "data.edn")
                      slurp
                      edn/read-string)
              user-map (entity-map data :users)
              address-map (entity-map data :addresses)
              role-map (entity-map data :roles)]
                             {:query/user-by-id (partial user-by-id user-map)
                              :User/address (partial resolve-address-by-user address-map)
                              :User/role (partial resolve-role-by-user role-map)
                              :mutate/user (partial resolve-role-by-user role-map)}))
      
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
    {:schema-provider (map->SchemaProvider {})})


          
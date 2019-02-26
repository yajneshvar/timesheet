(ns timesheet.db
    (:require
        [clojure.edn :as edn]
        [clojure.java.io :as io]
        [com.stuartsierra.component :as component]))

(defrecord Db [data]
    component/LifeCycle
    (start [this]
        (assoc this :data (-> (io/resource "data.edn")
                        slurp
                        edn/read-string
                        atom)))
    (stop [this]
        (assoc this :data nil))
    )
(defn new-db []
    {:db (map->Db {})})

(defn find-user-by-id
    [db user-id]
    (->> db
        :data
        deref
        :users
        (filter #(= user-id (:id %)))
        first))

(defn find-address-by-id 
    [db address-id]
    (->> db
        :data
        deref
        :addresses
        (filter #(= address-id (:id %)))
        first))

(defn list-role-by-user
    [db user-id]
    (let [roles (:roles find-user-by-id user-id)]
        (->> db
            :data
            deref
            :roles
            (filter #(contains? roles (:id %))))))

(defn find-address-by-user
    [db user-id]
    (let [address-id (:address find-user-by-id user-id)]
        (->> db
            :data
            deref
            :addresses
            (filter #(= address-id (:id %))
            first))))
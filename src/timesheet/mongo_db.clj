(ns timesheet.mongo-db
    (:require
        [clojure.java.io :as io]
        [com.stuartsierra.component :as component]
        [clojure.tools.logging :as log]
        [monger.core :as mg]
        [monger.credentials :as mcred]) 
        (:import [com.mongodb MongoOptions ServerAddress]))


(defrecord Mongo [conn conf] 
    component/Lifecycle
    (start [this] (let [
        {url :url port :port user :user password :password db :db} (:conf this)
        cred (mcred/create user db password)]
        (assoc this :conn (mg/connect-with-credentials url port cred))))
    (stop [this] 
        (mg/disconnect conn)
        (assoc this :conn nil)))

(defn new-mongo [db-opts]
    {:mongo (map->Mongo {:conf db-opts})})

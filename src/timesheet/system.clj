(ns timesheet.system
    (:require     
        [com.stuartsierra.component :as component]
        [timesheet.schema :as schema]
        [timesheet.server :as server]
        [timesheet.db :as db]
        [timesheet.mongo-db :as mongo]))


        (defn new-system
            []
            (merge (component/system-map)
                   (server/new-server)
                   (schema/new-schema-provider)
                   (db/new-db)
                   (mongo/new-mongo {:url "127.0.0.1" :port 27017 :user "root" :password "root" :db "admin"})))
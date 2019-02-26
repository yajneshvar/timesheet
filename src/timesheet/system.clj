(ns timesheet.system
    (:require     
        [com.stuartsierra.component :as component]
        [timesheet.schema :as schema]
        [timesheet.server :as server]))


        (defn new-system
            []
            (merge (component/system-map)
                   (server/new-server)
                   (schema/new-schema-provider)))
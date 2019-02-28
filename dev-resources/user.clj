(ns user
    (:require
      [clojure.java.browse :refer [browse-url]]
      [com.stuartsierra.component :as component]
      [clojure.walk :as walk]
      [com.walmartlabs.lacinia :as lacinia]
      [timesheet.system :as system])
      (:import (clojure.lang IPersistentMap)))
 
  (defonce system (system/new-system))
  
  (defn simplify
    "Converts all ordered maps nested within the map into standard hash maps, and
     sequences into vectors, which makes for easier constants in the tests, and eliminates ordering problems."
    [m]
    (walk/postwalk
      (fn [node]
        (cond
          (instance? IPersistentMap node)
          (into {} node)
  
          (seq? node)
          (vec node)
  
          :else
          node))
      m))

  (defn q
    [query-string]
    (-> system
        :schema-provider
        :schema
        (lacinia/execute query-string nil nil)
        simplify)
    )

    (defn start
      []
      (alter-var-root #'system component/start-system)
      (browse-url "http://localhost:8888/")
      :started)
    
    (defn stop
      []
      (alter-var-root #'system component/stop-system)
      :stopped)
  
  
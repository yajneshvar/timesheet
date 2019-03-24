(ns timesheet.records.record)


(defrecord User [id name email roles phone address])

(defrecord Address [id street city post_code country])

(defrecord Role [id name])
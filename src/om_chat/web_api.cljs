(ns om-chat.web-api
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [cljs.core.async :refer [chan >! <!]]
            [om-chat.app-state :as state]))

(defn get-initial-data [channel]
  (go
    (>! channel state/state))
  channel)

(ns om-chat.core
  (:require-macros [cljs.core.async.macros :refer [go]])
  (:require [goog.dom :as gdom]
            [om.next :as om]
            [cljs.core.async :refer [chan <!]]
            [om-chat.parser :as p]
            [om-chat.components.root :as root]
            [om-chat.web-api :as api]))

(enable-console-print!)

(go
  (let [out (chan)
        state (<! (api/get-initial-data out))]
    (def reconciler
            (om/reconciler {:state state
                            :parser (om/parser {:read p/read :mutate p/mutate})})))

  (om/add-root! reconciler
                root/ChatApp
                (gdom/getElement "app"))

  (om/transact! reconciler `[(thread/select {:thread/id "t_3"}) :threads])

  )

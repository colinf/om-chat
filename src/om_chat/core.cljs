(ns om-chat.core
    (:require [goog.dom :as gdom]
              [om.next :as om]
              [om-chat.parser :as p]
              [om-chat.app-state :as state]
              [om-chat.components.root :as root]))

(enable-console-print!)

(def reconciler
  (om/reconciler {:state  state/state
                  :parser (om/parser {:read p/read :mutate p/mutate})}))

(om/add-root! reconciler
              root/ChatApp
              (gdom/getElement "app"))

(om/transact! reconciler `[(thread/select {:thread/id "t_3"}) :threads])

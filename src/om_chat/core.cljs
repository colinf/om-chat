(ns ^:figwheel-always om-chat.core
    (:require [goog.dom :as gdom]
              [om.next :as om]
              [om-chat.parser :as p]
              [om-chat.app-state :as state]
              [om-chat.components.root :as root]))

(enable-console-print!)

(def reconciler
  (om/reconciler {:state  (atom state/state)
                  :parser (om/parser {:read p/read :mutate p/mutate})}))

(om/add-root! reconciler
              root/ChatApp
              (gdom/getElement "app"))

(om/transact! reconciler `[(thread/select {:thread/id "t_3"})])

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)

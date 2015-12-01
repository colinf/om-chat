(ns om-chat.components.thread-item
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [om-chat.components.message-section :as msg]))


(defui ThreadItem
  static om/Ident
  (ident [this {:keys [thread/id]}]
         [:threads/by-id id])

  static om/IQuery
  (query [this]
         [:thread/id :thread/name :thread/read :thread/selected
          (om/get-query msg/MessageSection)])

  Object
  (render [this]
          (let [{:keys [thread/id thread/name thread/selected thread/messages]} (om/props this)
                last-message (last messages)
                {:keys [on-click-thread]} (om/get-computed this)
                class (cond-> "thread-list-item"
                        selected (str " active"))]
            (dom/li #js{:className class
                        :onClick
                        (fn [e] (on-click-thread id))}
                    (dom/h5 #js{:className "thread-name"}
                            name)
                    (dom/div #js{:className "thread-time"}
                             (if (nil? last-message) "" (.toLocaleTimeString (:message/date last-message))))
                    (dom/div #js{:className "thread-last-message"}
                             (:message/text last-message))
                    ))))

(def thread-item (om/factory ThreadItem))

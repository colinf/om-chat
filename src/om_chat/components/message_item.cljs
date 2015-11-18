(ns om-chat.components.message-item
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]))

(defui MessageItem
  static om/Ident
  (ident [this {:keys [message/id]}]
         [:messages/by-id id])

  static om/IQuery
  (query [this]
         [:message/id :message/author-name :message/date :message/text])

  Object
  (render [this]
;;          (println "RENDER MessageItem: " (om/props this))
          (let [{:keys [message/author-name message/date message/text]} (om/props this)]
            (dom/li #js{:className "message-list-item"}
                    (dom/h5 #js{:className "message-author-name"} author-name)
                    (dom/div #js{:className "message-time"} (.toLocaleTimeString date))
                    (dom/div #js{:className "message-text"} text)))))

(def message-item (om/factory MessageItem))

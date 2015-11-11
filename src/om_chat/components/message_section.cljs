(ns om-chat.components.message-section
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            ))


(defui MessageSection
  static om/IQuery
  (query [this]
         [:message/id :message/author-name :message/date :message/text])

  Object
  (render [this]
          (dom/div #js{:className "message-section"}
                   (dom/h3 #js{:className "message-thread-heading"}))
          ))

(def message-section (om/factory MessageSection))

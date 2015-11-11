(ns om-chat.components.root
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [om-chat.components.thread-section :as thread-section]
            [om-chat.components.thread-item :as thread-item]
            [om-chat.components.message-section :as msg]
            ))

(defui ChatApp
  static om/IQuery
  (query [this]
         [:unread-count :selected-thread {:threads (om/get-query thread-item/ThreadItem)}])
  Object
  (render [this]
          (dom/div #js{:className "chatapp"}
                   (thread-section/thread-section
                    (om/computed (om/props this)
                                 {:on-click-thread
                                  (fn [id] (om/transact! this
                                                        `[(thread/select {:thread/id ~id})]))}))
                   (msg/message-section (om/props this))
                   )
          )
  )

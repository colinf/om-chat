(ns om-chat.components.root
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [om-chat.components.thread-section :as thread-section]
            [om-chat.components.thread-item :as item]
            [om-chat.components.message-section :as msg]
            ))

(defui ChatApp
  static om/IQuery
  (query [this]
         [:selected-thread (om/get-query thread-section/ThreadSection)
          {:current-thread (om/get-query item/ThreadItem)}])
  Object
  (render [this]
          (let [{:keys [selected-thread threads current-thread]} (om/props this)]
            (println "ROOT: " (om/props this))
            (dom/div #js{:className "chatapp"}
                     (thread-section/thread-section
                      (om/computed (om/props this)
                                   {:on-click-thread
                                    (fn [id]
                                      (om/transact! this `[(thread/select {:thread/id ~id})]))}))
                     (if selected-thread
                       (msg/message-section (om/computed current-thread
                                             {:on-new-msg
                                              (fn [thread-id msg-text]
                                                (om/transact! this
                                                  `[(message/new {:thread/id ~(:thread/id current-thread) :message/text ~msg-text})])
                                                )})))
                     ))
          )
  )

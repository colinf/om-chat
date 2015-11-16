(ns om-chat.components.thread-section
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [om-chat.components.thread-item :as item]
            ))

(defn unread [threads]
  (let [unread-count (count (remove :thread/read threads))]
   (cond
     (= unread-count 0) nil
     :else (dom/span nil "Unread threads: " unread-count)))
  )

(defui ThreadSection
  Object
  (render [this]
          (let [{:keys [threads]} (om/props this)
                {:keys [on-click-thread]} (om/get-computed this)]
            (dom/div #js{:className "thread-section"}
                     (dom/div #js{:className "thread-count"} (unread threads))
                     (apply dom/ul #js{:className "thread-list"}
                            (map #(item/thread-item
                                   (om/computed % (om/get-computed this)))
                                 threads))
                     ))
          ))

(def thread-section (om/factory ThreadSection))

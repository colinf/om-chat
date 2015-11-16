(ns om-chat.components.message-section
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            [om-chat.components.message-item :as item]
            [om-chat.components.message-composer :as composer]
            ))

(defui MessageSection
  Object
  (render [this]
          (let [{:keys [thread/id thread/name thread/messages] :as props} (om/props this)
                {:keys [on-new-msg]} (om/get-computed props)]
            (println om/path)
            (dom/div #js{:className "message-section"}
                     (dom/h3 #js{:className "message-thread-heading"} name)
                     (apply dom/ul #js{:className "message-list"}
                            (map item/message-item messages))
                     (composer/message-composer
                      (om/computed
                       props
                       {:on-new-msg on-new-msg}
                       ))))))

(def message-section (om/factory MessageSection))

(ns om-chat.components.message-composer
  (:require [om.next :as om :refer-macros [defui]]
            [om.dom :as dom]
            ))

(def ESCAPE_KEY 27)
(def ENTER_KEY 13)

(defn change [c e]
  (om/update-state! c assoc
                    :edit-text (.. e -target -value)))

(defn key-down [c id e on-new-msg]
  (condp == (.-keyCode e)
    ENTER_KEY
    (let [msg-text (or (om/get-state c :edit-text) "")]
      (if-not (= msg-text "") (on-new-msg id msg-text))
      (om/update-state! c assoc :edit-text "")
      (doto e (.preventDefault) (.stopPropagation)))
    nil))

(defui MessageComposer
  ;; static om/IQuery
  ;; (query [this]
  ;;        :thread/id)
  Object
  (render [this]
          (let [{:keys [thread/id] :as props} (om/props this)
                {:keys [on-new-msg]} (om/get-computed props)]
;;            (println "RENDER composer: " props)
            (dom/textarea #js{:className "message-composer"
                              :name      "message"
                              :value     (om/get-state this :edit-text)
                              :onChange  #(change this %)
                              :onKeyDown #(key-down this id % on-new-msg)})
            )))

(def message-composer (om/factory MessageComposer))

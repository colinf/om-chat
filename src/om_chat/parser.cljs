(ns om-chat.parser
  (:require [om.next :as om]
            [cljs-time.core :as dt]
            [cljs-time.coerce :as dt2]))

(defmulti read om/dispatch)

(defn thread-with-derived
  [{:keys [thread/id] :as thread} st]
  (let [selected-id (:selected/thread st)]
    (if (= selected-id id)
            (assoc thread :thread/selected true)
            thread))
  )

(defmethod read :default
  [{:keys [state] :as env} k _]
  (let [st @state] ;; CACHING!!!
;;    (println "READ :default" k env)
    (if (contains? st k)
      {:value (get st k)}
      {:remote true})))

(defmethod read :threads
  [{:keys [state query]} k _]
  (let [st   @state
        tree (om/db->tree query (get st k) st)]
;;    (println tree)
    {:value (into [] (map #(thread-with-derived % st) tree))}))

(defmulti mutate om/dispatch)

(defmethod mutate 'thread/select
  [{:keys [state]} _ {:keys [thread/id]}]
;;  (println "MUTATE thread/select: " @state)
  (let [swapper (fn [st]
                  (assoc-in
                   (assoc st :selected/thread id)
                   [:threads/by-id id :thread/read] true))]
    {:action #(swap! state swapper)}))

(defmethod mutate 'message/new
  [{:keys [state]} _ {:keys [thread/id message/text]}]
  (let [now     (dt/now)
        msg-id  (str "m_" now)
        new-msg {:message/id msg-id
                 :message/author-name "Bill"
                 :message/date (dt2/to-date now)
                 :message/text text
                 :message/read true}
        swapper (fn [st]
                  (update-in
                   (assoc-in st [:messages/by-id msg-id] new-msg)
                   [:threads/by-id id :thread/messages]
                   conj [:messages/by-id msg-id]))]
;;    (println id new-msg)
    {:action #(swap! state swapper)}))

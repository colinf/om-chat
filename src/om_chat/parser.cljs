(ns om-chat.parser
  (:require [om.next :as om]))

(defmulti read om/dispatch)

(defn read-thread
  [{:keys [thread]} k _]
  {:value (get thread k)})

(defmethod read :default
  [{:keys [state]} k _]
  (let [st @state] ;; CACHING!!!
    (if (contains? st k)
      {:value (get st k)}
      {:remote true})))


(defmethod read :threads
  [{:keys [state parser selector] :as env} _ _]
  (let [threads (:threads @state)
        get-thread (fn [thread]
                     (let [env2 (dissoc env :selector)
                           env-with-thread (assoc env2 :thread thread)]
                       (parser env-with-thread selector)))
        new-threads (into [] (map get-thread (vals threads)))]
    {:value new-threads})
  )

(defmethod read :thread/id
  [env k param]
  (read-thread env k param)
  )
(defmethod read :thread/name
  [env k param]
  (read-thread env k param)
  )
(defmethod read :thread/read
  [env k param]
  (read-thread env k param)
)
(defmethod read :thread/selected
  [{:keys [state thread]} k param]
  (let [st @state
        {:keys [selected-thread]} st
        this-thread (:thread/id thread)
        ]
    (if (= selected-thread this-thread)
     {:value true}
     {:value false}))
  )

(defmethod read :thread/last-message
  [{:keys [state parser selector thread] :as env} _ _]
  (let [messages (vals (:thread/messages thread))
        last-msg (fn [{:keys [:message/date] :as coll} msg]
                   (cond
                     (empty? coll) msg
                     (> (:message/date msg) date) msg
                     :else coll))
        last-message (reduce last-msg {} messages)]
    (cond
      selector {:value (select-keys last-message selector)}
      :else {:value last-message}))
  )

(defmethod read :thread/messages
  [{:keys [state parser selector thread] :as env} _ _]
  (let [messages (vec (vals (:thread/messages thread)))]
    (cond
      selector {:value [(select-keys messages selector)]}
      :else {:value messages}))
  )

(defmulti mutate om/dispatch)

(defmethod mutate 'thread/select
  [{:keys [state]} _ {:keys [thread/id]}]
  (let [swapper (fn [st] (assoc-in
                         (assoc st :selected-thread id)
                         [:threads id :thread/read] true))]
   {:action #(swap! state swapper)}))

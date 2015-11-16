(ns om-chat.parser
  (:require [om.next :as om]
            [cljs-time.core :as dt]
            [cljs-time.coerce :as dt2]))

(defmulti read om/dispatch)

(defn get-from-thread
  [{:keys [state thread]} k]
  (let [thr (get-in @state thread)]
    {:value (get thr k)}))

(defmethod read :default
  [{:keys [state] :as env} k _]
  (let [st @state] ;; CACHING!!!
    (println "READ :default" k env)
    (if (contains? st k)
      {:value (get st k)}
      {:remote true})))

(defmethod read :threads
  [{:keys [state parser query] :as env} k _]
  (let [st         @state
        parse-t    #(parser (assoc env :thread %) query)
        threads    (get st k)
        value      (into [] (map parse-t threads))]
    {:value value}))

(defmethod read :threadsXXX
  [{:keys [state parser query] :as env} _ _]
  (let [threads (:threads @state)
        get-thread (fn [thread]
                     (let [env2 (dissoc env :query)
                           env-with-thread (assoc env2 :thread (val thread))]
                       {(key thread) (parser env-with-thread query)}))
        new-threads (into {} (map get-thread threads))]
    {:value new-threads})
  )

(defmethod read :thread/id
  [env k _]
  (get-from-thread env k)
  )
(defmethod read :thread/name
  [env k param]
  (get-from-thread env k)
  )
(defmethod read :thread/read
  [env k param]
  (get-from-thread env k)
  )
(defmethod read :thread/selected
  [{:keys [state thread] :as env} _ _]
  (println "READ thread/selected: " env)
  (let [{:keys [selected-thread]} @state]
    (if (= selected-thread thread)
            {:value true}
            {:value false}))
  )

(defmethod read :thread/last-message
  [{:keys [state query] :as env} _ _]
  (let [msg-selectors (:value (get-from-thread env :thread/messages))
        last-message (get-in @state (last msg-selectors))]
    (cond
      query {:value (select-keys last-message query)}
      :else {:value last-message}))
  )

(defmethod read :thread/messages
  [{:keys [state query] :as env} k _]
  (let [st            @state
        msg-selectors (:value (get-from-thread env k))
        select-msg    #(get-in st %)
        messages      (map select-msg msg-selectors)]
;;    (println "READ :thread/messages: " messages)
     (cond
      query {:value (into [] (map #(select-keys % query) messages))}
      :else {:value messages}))
  )

(defmulti mutate om/dispatch)

(defmethod mutate 'thread/select
  [{:keys [state]} _ {:keys [thread/id]}]
;;  (println "MUTATE thread/select: " @state)
  (let [swapper (fn [st]
                  (assoc-in
                   (assoc st :selected-thread [:threads/by-id id])
                   [:threads/by-id id :thread/read] true))]
    {:action #(swap! state swapper)}))

(defmethod mutate 'message/new
  [{:keys [state]} _ {:keys [thread/id message/text]}]
  (let [swapper (fn [st] (assoc-in
                         (assoc st :selected-thread [:threads/by-id id])
                         [:threads id :thread/read] true))
        now     (dt/now)
        new-msg {:message/id (str "m_" now)
                 :message/author-name "Bill"
                 :message/date (dt2/to-date now)
                 :message/text text
                 :message/read true
                 }]
    {:action #(swap! state update-in [:threads id :thread/messages] comp new-msg)}))

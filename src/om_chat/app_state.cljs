(ns om-chat.app-state
  (:require [cljs-time.core :as dt]
            [cljs-time.coerce :as dt2])
  )


(def raw-data [
               {
                :id "m_1"
                :threadID "t_1"
                :threadName "Jing and Bill"
                :authorName "Bill"
                :text "Hey Jing want to give a Flux talk at ForwardJS?"
                :timestamp (- (dt/now) 99999)
                }
               {
                :id "m_2"
                :threadID "t_1"
                :threadName "Jing and Bill"
                :authorName "Bill"
                :text "Seems like a pretty cool conference."
                :timestamp (- (dt/now) 89999)
                }
               {
                :id "m_3"
                :threadID "t_1"
                :threadName "Jing and Bill"
                :authorName "Jing"
                :text "Sounds good.  Will they be serving dessert?"
                :timestamp (- (dt/now) 79999)
                }
               {
                :id "m_4"
                :threadID "t_2"
                :threadName "Dave and Bill"
                :authorName "Bill"
                :text "Hey Dave want to get a beer after the conference?"
                :timestamp (- (dt/now) 69999)
                }
               {
                :id "m_5"
                :threadID "t_2"
                :threadName "Dave and Bill"
                :authorName "Dave"
                :text "Totally!  Meet you at the hotel bar."
                :timestamp (- (dt/now) 59999)
                }
               {
                :id "m_6"
                :threadID "t_3"
                :threadName "Functional Heads"
                :authorName "Bill"
                :text "Hey Brian are you going to be talking about functional stuff?"
                :timestamp (- (dt/now) 49999)
                }
               {
                :id "m_7"
                :threadID "t_3"
                :threadName "Bill and Brian"
                :authorName "Brian"
                :text "At ForwardJS?  Yeah of course.  See you there!"
                :timestamp (- (dt/now) 39999)
                }])


(defn convert-message [{:keys [id authorName text timestamp] :as msg}]
  {:message/id id
   :message/author-name authorName
   :message/text text
   :message/date (dt2/to-date timestamp)
   }
  )

(defn threads-by-id [coll msg]
  (let [{:keys [id threadID threadName timestamp]} msg]
    (if (contains? coll threadID)
      (assoc-in coll [threadID :thread/messages id]
                (convert-message msg))
      (assoc coll threadID
             {:thread/id threadID
              :thread/name threadName
              :thread/messages {id (convert-message msg)}
              })
      ))
  )

(defn threads [coll msg]
  (let [{:keys [threadID threadName]} msg
        {:keys [thread/id]} (last coll)
        last-msg-idx (dec (count coll))]
    (if (= id threadID)
      (update-in coll [last-msg-idx :thread/messages] conj (convert-message msg))
      (conj coll
             {:thread/id threadID
              :thread/name threadName
              :thread/messages [(convert-message msg)]
              })
      ))
  )

(def state {:threads  (reduce threads [] raw-data)
            })

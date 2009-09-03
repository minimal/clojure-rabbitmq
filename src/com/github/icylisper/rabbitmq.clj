
(ns com.github.icylisper.rabbitmq
  (:import (com.rabbitmq.client
	     ConnectionParameters
	     Connection
	     Channel
	     AMQP
	     ConnectionFactory)))


(def connection-params
  { :username "guest"
    :password "guest"
    :host "localhost"
    :port 5672
    :virtual-host "/"
    :type "direct"
    :exchange "sorting-room"
    :queue "po-box"
    :routing-key "tata"})

(def params
  (doto (new ConnectionParameters)
    (.setUsername "guest")
    (.setPassword "guest")
    (.setVirtualHost "/")
    (.setRequestedHeartbeat 0)))

(def conn (.newConnection (new ConnectionFactory params) "localhost" 5672))
(def channel (.createChannel conn))

(def mq-type "direct")
(def mq-exchange "sorting-room")
(def mq-queue "po-box")
(def routing-key "tata")

(defn connect []
  (.exchangeDeclare channel mq-exchange mq-type)
  (.queueDeclare channel mq-queue)
  (.queueBind channel mq-queue mq-exchange routing-key))

(defn publish [message]
  (let [msg-bytes (.getBytes message)]
    (.basicPublish channel mq-exchange routing-key nil msg-bytes)))

(defn disconnect []
  (map (memfn close) [channel conn]))

(comment
  (connect)
  (publish "message"))



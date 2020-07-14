# Prestacop

Projet Prestacop du groupe 8

1 - Lancer zookeeper : bin/zookeeper-server-start.sh config/zookeeper.properties

2 - Lancer kafka : bin/kafka-server-start.sh config/server.properties

3 - Creer le topic : bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic kafkatest

4 - Lancer le producer (données envoyées par le drone): sbt "runMain Producer localhost:9092 kafkatest 1"

5 - Lancer le programme spark streaming : sbt "runMain SPARKStreaming localhost:9092 kafkatest 1"

Les messages du drones sont stockés en local dans le dosser prestacop/src/main/scala/DroneData

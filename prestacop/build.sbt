name := "KafkaExample"

version := "1.0"

scalaVersion := "2.12.8"
resolvers in ThisBuild += Resolver.bintrayRepo("streetcontxt", "maven")
libraryDependencies += "org.apache.kafka" % "kafka-clients" % "0.10.1.0"
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % "2.4.0"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.4.0"
libraryDependencies += "org.apache.spark" %% "spark-streaming" % "2.4.0"
libraryDependencies += "com.streetcontxt" %% "kpl-scala" % "1.1.0"
libraryDependencies += "com.amazonaws" % "amazon-kinesis-producer" % "0.12.11"
libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.11.46"
//libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.6.4"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.1.3" % Runtime
libraryDependencies += "javax.xml.bind" % "jaxb-api" % "2.3.0"
libraryDependencies += "jp.co.bizreach" %% "aws-kinesis-scala" % "0.0.12"
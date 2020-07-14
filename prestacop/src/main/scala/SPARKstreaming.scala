import com.amazonaws.regions.Regions
import jp.co.bizreach.kinesis.{AmazonKinesis, PutRecordRequest}
import org.apache.kafka.clients.consumer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka010.{ConsumerStrategies, KafkaUtils, LocationStrategies}
import org.apache.spark.streaming.{Seconds, StreamingContext}

object SPARKStreaming {
  def main(args: Array[String]): Unit = {

    val brokers = "localhost:9092"
    val groupId = "GRP1"
    val topics = "kafkatest"

    val SparkConf = new SparkConf().setMaster("local[*]").setAppName("SparkStreaming")
    val ssc = new StreamingContext(SparkConf, Seconds(2))
    val sc = ssc.sparkContext
    sc.setLogLevel("OFF")

    val topicSet = topics.split(",").toSet
    val kafkaParams = Map[String, Object](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers,
      ConsumerConfig.GROUP_ID_CONFIG -> groupId,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer],
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> classOf[StringDeserializer]
    )

    val messages = KafkaUtils.createDirectStream[String, String](
      ssc, LocationStrategies.PreferConsistent, ConsumerStrategies.Subscribe[String, String](topicSet, kafkaParams)
    )

    val line = messages.map(_.value)

    implicit val region = Regions.EU_WEST_1;

    val client = AmazonKinesis();

    println(client);

    val string = line.toString().getBytes("UTF-8");

    println(string);

    val request = PutRecordRequest(
      streamName   = "spark_prestacop",
      partitionKey = "1",
      data         = string
    );

    try{
      client.putRecord(request);
    }catch{
      case e : Exception => println("Marche pô");
    }

    line.print();

    //save drone messages in local system
    val localmessage = messages.map(_.value)
    localmessage.saveAsTextFiles("./DroneData/")
    localmessage.print()

    ssc.start()
    ssc.awaitTermination()

  }
}
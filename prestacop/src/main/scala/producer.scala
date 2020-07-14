import java.util.Properties
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.kafka.common.serialization.StringSerializer
import java.net._
import java.io._
import scala.io._
import scala.concurrent._
import ExecutionContext.Implicits.global
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}
import scala.util.Random

class Producer(topic: String, brokers: String) {

  val producer = new KafkaProducer[String, String](configuration)

  case class Message(id: Int, longitude: Float, latitude: Float, date: String, infractionCode: Int, plate: String)

  private def configuration: Properties = {
    val props = new Properties()
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092")
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getCanonicalName)
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getCanonicalName)
    props
  }

  def extract(message: Message, fieldName: String) = {
    fieldName match {
    case "id" => message.id
    case "longitude" => message.longitude
    case "latitude" => message.latitude
    case "date" => message.date
    case "infractionCode" => message.infractionCode
    case "plate" => message.plate
    }
  }

  def getLocation() : List[Float] = {
    List.fill(2)(Random.nextFloat*1000)
  }

  def getCode() : Int = {
    val infractionCode = List(100, 200, 300, 400, 500, 600, 700);
    infractionCode(Random.nextInt(infractionCode.length))
  }

  def getDate() : String = {
    val format = new SimpleDateFormat("d-M-y HH:mm:ss");
    val date = format.format(Calendar.getInstance().getTime);
    date
  }

  def getPlate() : String = {
    val elements = Set("A678 WRL", "YY19 JCT", "VZR 2LE", "W77 G9U", "UMT TY1")
    val rnd = new Random
    elements.toVector(rnd.nextInt(elements.size))
  }

  def generateMessage() : String = {

    val id = Random.nextInt(1000000)
    val location = getLocation();
    val code = getCode();
    val date = getDate();
    val plate = getPlate();

    val message = Message(id, location(0), location(1),date, code, plate)
    
    "{\"message\" : {" +
          "\"id\" : " + extract(message, "id") + "," +
          "\"longitude\" : " + extract(message, "longitude") + "," +
          "\"latitude\" : " + extract(message, "latitude") + "," +
          "\"date\" : " + "\"" + extract(message, "date") + "\"" + "," +
          "\"infractionCode\" : " + extract(message, "infractionCode") + "," +
          "\"plate\" : " + "\"" + extract(message, "plate") + "\"" +
        "}" +
    "}";
  }

  def sendMessages(): Nothing = {
    val record = new ProducerRecord[String, String](topic, "1", generateMessage());
    producer.send(record);
    Thread.sleep(1000);
    sendMessages();
  }

}

object Producer extends App {

  val producer = new Producer(brokers = args(0), topic = args(1))
  producer.sendMessages()

}
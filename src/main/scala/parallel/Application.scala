package parallel

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._
import akka.{Done, NotUsed}
import akka.actor.ActorSystem
import akka.util.ByteString

import scala.concurrent._
import scala.concurrent.duration._
import java.nio.file.Paths
import java.util.concurrent.Executors

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression
import system.Tools

import scala.concurrent.ExecutionContext.global

/**
  * Created by gennady on 27/12/17.
  */
object Application extends App{
  val parallelLevel = 21
  val regression = new OLSMultipleLinearRegression()

  implicit val context = new ExecutionContext {
    val threadPool = Executors.newFixedThreadPool(10)
    override def execute(runnable: Runnable): Unit = {
      threadPool.submit(runnable)
    }
    override def reportFailure(cause: Throwable): Unit = {}
  }
  val system = ActorSystem("Stream")
  implicit val materializer = ActorMaterializer.create(system)

  def dataGenerator(): List[(List[Double], List[Double])] ={
    var signalAData:Array[List[Double]] = Array.fill(21)(Nil)

    var signalBData:Array[List[Double]] = Array.fill(21)(Nil)
    //проверить индексы и размерность
    for(i <- 0 until 21){
      signalAData(i) = Tools.readATypedFile("/home/gennady/bigdataProjects/univer_signal_resolver/src/main/resources/A.txt", i)
      signalBData(i) = Tools.readBTypedFile("/home/gennady/bigdataProjects/univer_signal_resolver/src/main/resources/B.txt", i)

    }
    tupled[Double](signalAData, signalBData).toList
  }

  def tupled[T](array: Array[List[T]], arrayTwo: Array[List[T]]):Array[(List[T],List[T])]={
    var temp:Array[(List[T],List[T])] = Array.fill(array.length)((Nil,Nil))
    for(i <- array.indices){
      temp(i) = (array(i), arrayTwo(i))
    }
    temp
  }

  def run(data:List[(List[Double],List[Double])]): Unit ={
    Source(data)
      .mapAsync(parallelLevel)(Stage.apply(context,_))
    .runWith(Sink.foreach{x => print("##"); x.foreach(x => print(x + ", "));
      println("#")})
  }


  val data = dataGenerator()
  run(data)

}

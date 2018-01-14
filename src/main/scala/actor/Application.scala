package actor

import java.util.concurrent.Executors

import actor.messages.Job
import akka.actor.{ActorSystem, Props}

import scala.concurrent.ExecutionContext

/**
  * Created by gennady on 11/01/18.
  */
object Application extends App{
  println("Mem " + Runtime.getRuntime.totalMemory())
  println("Cores " + Runtime.getRuntime.availableProcessors())

  private val paramMap = args.map{
    parameter => val pair = parameter.split("=",-1)
      (pair(0),pair(1))
  }.toMap

  val EXECUTORS = paramMap.getOrElse("EXECUTORS", "21").toInt
  val CHANNELS = paramMap.getOrElse("CHANNELS", "21").toInt
  val THREAD_POOL = paramMap.getOrElse("THREAD_POOL", "1000").toInt
  val SIGNAL_A = paramMap.getOrElse("SIGNAL_A","/home/gennady/bigdataProjects/univer_signal_resolver/src/main/resources/A.txt")
  val SIGNAL_B = paramMap.getOrElse("SIGNAL_B","/home/gennady/bigdataProjects/univer_signal_resolver/src/main/resources/B.txt")

  implicit val context = new ExecutionContext {
    val threadPool = Executors.newFixedThreadPool(THREAD_POOL)
    override def execute(runnable: Runnable): Unit = {
      threadPool.submit(runnable)
    }
    override def reportFailure(cause: Throwable): Unit = {}
  }

  val system = ActorSystem("actor-based-signal-system")
  val master = system.actorOf(Props(new Master()), "master")

  master ! Job(SIGNAL_A, SIGNAL_B, EXECUTORS, CHANNELS)


}

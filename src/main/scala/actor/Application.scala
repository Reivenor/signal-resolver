package actor

import java.util.concurrent.{Executors, ForkJoinPool}

import actor.messages.Job
import akka.actor.{ActorSystem, Props}

import scala.concurrent.ExecutionContext

/**
  * Created by gennady on 11/01/18.
  */
object Application extends App{
  println("Mem " + Runtime.getRuntime.totalMemory())
  println("Cores " + Runtime.getRuntime.availableProcessors())
  //Чтение параметров из коммандной строки
  private val paramMap = args.map{
    parameter => val pair = parameter.split("=",-1)
      (pair(0),pair(1))
  }.toMap

  val EXECUTORS = paramMap.getOrElse("EXECUTORS", "21").toInt //Количество исполнителей
  val CHANNELS = paramMap.getOrElse("CHANNELS", "21").toInt //Число каналов в файле
  val THREAD_POOL = paramMap.get("THREAD_POOL") //Пулл потоков в JVM
  val FEQUENCY = paramMap.getOrElse("FREQUENCY", "500").toInt
  val SIGNAL_A = paramMap.getOrElse("SIGNAL_A","/home/gennady/diplom/univer_signal_resolver/src/main/resources/A.txt")//ЗВП
  val SIGNAL_B = paramMap.getOrElse("SIGNAL_B","/home/gennady/diplom/univer_signal_resolver/src/main/resources/B.txt")//УЗВП

  //Устанавливаем пулл потоков по умолчанию или кастомный фиксированнный
  implicit val context = new ExecutionContext {
    val threadPool = THREAD_POOL match {
      case Some(value) => Executors.newFixedThreadPool(value.toInt)
      case None => ForkJoinPool.commonPool()
    }

    override def execute(runnable: Runnable): Unit = {
      threadPool.submit(runnable)
    }
    override def reportFailure(cause: Throwable): Unit = {}
  }
  //Создаем новую систему акторов
  val system = ActorSystem("actor-based-signal-system")
  //В систему акторов добавляем актор-мастер
  val master = system.actorOf(Props(new Master()), "master")
  //Передаем мастеру новое задание
  master ! Job(SIGNAL_A, SIGNAL_B, EXECUTORS, CHANNELS)

}

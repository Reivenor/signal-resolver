package actor

import java.io.{File, PrintWriter}

import actor.func.{Func, Jobs}
import actor.messages.{Job, Result, Task}
import akka.actor.{Actor, ActorRef, Props}
import akka.routing.RoundRobinPool


/**
  * Created by gennady on 11/01/18.
  */
class Master extends Actor{
  private var workerRouter:SimpleRouter[Worker] = _ //Отложенная инициализация роутера,
  // через который происходит общение с исполнителями
  var counter = 0
  var jobProgress:Int = 0
  println("Master created")

  /**
    * Функция обрабатывает посылки на Мастер
    * @return
    */
  override def receive: Receive = {
    case Job(pathA, pathB, workers, channels) => {
      /*Пришло новое задание - необходимо создать роутер с заданым числом воркеров - и отправить им задания - распределить работу*/
      workerRouter = new SimpleRouter[Worker](classOf[Worker], workers, context, self)

      jobProgress = channels

      (0 until channels).foreach{
        jobMessageIndex =>  workerRouter
          .send(Task(Func.readATypedFile(pathA, jobMessageIndex), //Загрузка ЗВП
            Func.readBTypedFile(pathB, jobMessageIndex), //Загрузка УЗВП
            Jobs.linearRegressionProcessing, self, jobMessageIndex) //Функция которая должна быть выполнена над данными каждым исполнителем

          )
      }
    }
    case Result(regressionParams, regressionEtc, channelIndex) => {
      /*Обработка пришедшего от сполнителя результата*/
      println("Regression params " + regressionParams.mkString(","))
      val writer = new PrintWriter(new File("regression_params_channel_" + (channelIndex + 1) +".txt"))
      writer.write("Параметры линейной регресии канала " + (channelIndex + 1) + "\n")
      regressionParams.foreach(x => writer.write(" " + x))
      writer.write("\nОстаток\n")
      writer.write(regressionEtc + "\n")
      writer.close()
      /*Если счетчик задания достигает всего объема посылок - останавливаем систему*/
      counter += 1
      if(counter >= jobProgress) stop()
    }
  }

  /**
    * Функция отсановки системы акторов через общий контекст по требованию
    */
  def stop(): Unit ={
    context.system.terminate()
  }
}

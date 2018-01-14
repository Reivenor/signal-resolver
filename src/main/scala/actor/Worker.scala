package actor

import actor.messages.{Result, Task}
import akka.actor.Actor

/**
  * Created by gennady on 11/01/18.
  */
class Worker extends Actor{
  /*Описание исполнителя, активирующегося по появлению сообщения с новой задачей и отправляющего результат мастеру*/
  override def receive: Receive = {
    case Task(aSignal, bSignal, function, master, index) => {
      println("Worker " + index + " recives a task")
      val (regressionParams, regressionEtc) = function(aSignal, bSignal)//Применение функции на данные из задания
      //Thread.sleep(10000 - 100 * index )
      master ! Result(regressionParams, regressionEtc, index) //Отдача результата
    }
  }
}

package actor

import actor.messages.{Result, Task}
import akka.actor.Actor

/**
  * Created by gennady on 11/01/18.
  */
class Worker extends Actor{

  override def receive: Receive = {
    case Task(aSignal, bSignal, function, index, master) => {
      println("Worker " + index + " recives a task")
      val (regressionParams, regressionEtc) = function(aSignal, bSignal)
      Thread.sleep(10000 - 100 * index )
      master ! Result(regressionParams, regressionEtc, index)
    }
  }
}

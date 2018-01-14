package actor

import actor.func.{Func, Jobs}
import actor.messages.{Job, Result, Task}

import akka.actor.{Actor, ActorRef, Props}
import akka.routing.RoundRobinPool


/**
  * Created by gennady on 11/01/18.
  */
class Master extends Actor{
  private var workerRouter:SimpleRouter[Worker] = _
  var counter = 0
  var jobProgress:Int = 0
  println("Master created")
  override def receive: Receive = {
    case Job(pathA, pathB, workers, channels) => {
      workerRouter = new SimpleRouter[Worker](classOf[Worker], workers, context, self)
      jobProgress = channels

      /*context
        .actorOf(RoundRobinPool(workers).props(Props(new Worker)), "workerRouter")*/
      (0 until channels).foreach{
        id =>  workerRouter
          .send(Task(Func.readATypedFile(pathA, id),
            Func.readATypedFile(pathA, id),
            Jobs.linearRegressionProcessing, id, self),
            id)
      }
    }
    case Result(regressionParams, regressionEtc, workerId) => {
      println("From " + (workerId + 1)+ " " + regressionEtc)
      println("Regression params " + regressionParams.mkString(","))
      //println("From " + workerId + " \n " + regressionParams.mkString(","))
      counter += 1
      if(counter >= jobProgress) stop()
    }
  }


  def stop(): Unit ={
    context.system.terminate()
  }
}

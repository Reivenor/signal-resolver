package actor

import akka.actor.{ActorContext, ActorRef, Props}

/**
  * Created by gennady on 11/01/18.
  */
class SimpleRouter[A](actor:Class[A], count:Int, context:ActorContext, _master:ActorRef) {
  var workersList:List[ActorRef] = Nil
  val master = _master

  private var lastFreeWorkerId:Int = 0 //Индекс следующего актора для равномерного распределения заданий

  println("Set master as " + master.path.toString)

  for(id <- 0 to count){
    workersList :::= List(
      context.actorOf(Props(actor),
        id.toString))
    println("Actor with ID " + id + " created")
  }

  def send(message:Any): Unit ={
    workersList(lastFreeWorkerId) ! message
    println("Task sended to worker " + lastFreeWorkerId)
    lastFreeWorkerId = if(lastFreeWorkerId < count) lastFreeWorkerId + 1 else 0
  }
}

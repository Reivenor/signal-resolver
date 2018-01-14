package actor

import akka.actor.{ActorContext, ActorRef, Props}

/**
  * Created by gennady on 11/01/18.
  */
class SimpleRouter[A](actor:Class[A], count:Int, context:ActorContext, _master:ActorRef) {
  var workersList:List[ActorRef] = Nil
  val master = _master
  println("Set master as " + master.path.toString)

  for(id <- 0 to count){
    //if(id)//TODO не по одному сообщению на исполнитель
    workersList :::= List(
      context.actorOf(Props(actor),
        id.toString))
    println("Actor with ID " + id + " created")
  }

  def send(message:Any, id:Int): Unit ={
    workersList(id) ! message
    println("Task sended to worker " + id)
  }
}

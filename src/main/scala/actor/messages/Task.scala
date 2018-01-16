package actor.messages

import akka.actor.ActorRef

/**
  * Created by gennady on 11/01/18.
  */
case class Task(aSignal:List[Double],
                bSignal:List[Double],
                function: (List[Double],List[Double])=>(List[Double], List[Double]),//A => B
                sender:ActorRef,
                channelName:Int //TODO complite channels enumeration
               )
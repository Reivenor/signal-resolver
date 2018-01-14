package parallel

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression
import system.Tools

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by gennady on 27/12/17.
  */
object Stage {
  def apply(context:ExecutionContext,
            dataTuple:(List[Double], List[Double])
           ): Future[List[Double]] =Future{
    val regression = new OLSMultipleLinearRegression()
    println(Thread.currentThread().getName + " started")
    val matrix = Tools
      .transponce(Tools
        .buildMatrix(dataTuple._2,
          dataTuple._1))
    regression.newSampleData(dataTuple._2.toArray , matrix)
    println(Thread.currentThread().getName + "ended")
    regression.estimateRegressionParameters().toList
  }(context)

}

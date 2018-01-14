package actor.func

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression

/**
  * Created by gennady on 11/01/18.
  */
object Jobs {
  val linearRegressionProcessing = (aSignal:List[Double], bSignal:List[Double])=>{
    val regression = new OLSMultipleLinearRegression()
    val matrix = Func.transponce(Func.buildMatrix(bSignal, aSignal))
    regression.newSampleData(bSignal.toArray, matrix)
    (regression.estimateRegressionParameters().toList,
      regression.estimateRegressandVariance())

  }

  val testFunction = (aSignal:List[Double], bSignal:List[Double]) => {
    val list = (0 to 200000).map(_.toDouble).toList
    (list, list.sum)
  }
}

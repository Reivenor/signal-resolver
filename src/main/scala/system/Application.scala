package system

import java.io.{File, PrintWriter}

import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression

import scala.io.Source

/**
  * Created by gennady on 16/12/17.
  */
object Application extends App{

  for(i <- 0 to 20){
    val writer = new PrintWriter(new File("debug_" + (i+1) +".txt"))

    val regression = new OLSMultipleLinearRegression()

    val channel:Int = i

    val signalA = Tools.readATypedFile("/home/gennady/bigdataProjects/univer_signal_resolver/src/main/resources/A.txt", channel)
    println("A: " + signalA.length)
    val signalB = Tools.readBTypedFile("/home/gennady/bigdataProjects/univer_signal_resolver/src/main/resources/B.txt", channel)
    println("B: " + signalB.length)
    val matrix = Tools.transponce(Tools.buildMatrix(signalB, signalA))

    for(i <- matrix){
      i.foreach(x => writer.write(x + " "))
      writer.write("\n")
    }
    regression.newSampleData(signalB.toArray, matrix)
    val finalWriter = new PrintWriter(new File("regression_params_" + (i+1) + ".txt"))
    regression.estimateRegressionParameters().foreach(x => finalWriter.write(" " + x))
    println(regression.estimateRegressionParameters().length)
    writer.close()
    finalWriter.close()
  }




}

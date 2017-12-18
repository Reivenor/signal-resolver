package system

import scala.io.Source


/**
  * Created by gennady on 13/12/17.
  */
object Tools{

  private def matrixVectorConstructor(signalA:List[Double],
                                      lengthB:Int,
                                      positionB:Int):Array[Double]={
    var matrixVector = List.fill(lengthB)(0d).toArray
    signalA.copyToArray(matrixVector, positionB)
    //matrixVector.foreach(println(_))
    matrixVector
  }

  def buildMatrix(signalB:List[(Double)], signalA:List[(Double)]):Array[Array[Double]]={
    val standPlotCount = signalB.length / 500
    println("Stand point count: " + standPlotCount)
    var positions:List[Int] = Nil
    for(i <- 0 until signalB.length by 500){
      positions = positions ::: List(i)
    }
    var matrix:Array[Array[Double]] = Array
      .fill(positions.length)(Array.empty)
    var counter = 0
    positions.foreach{
      index => matrix(counter) = matrixVectorConstructor(signalA, signalB.length, index)
        counter += 1
    }
    matrix
  }

  def transponce(array: Array[Array[Double]]):Array[Array[Double]]={
    val pos = array.length
    val length = array(0).length
    var newArray:Array[Array[Double]] = Array.fill(length)(Array.fill(pos)(0))

    for(i <- 0 until length){
      for(p <- 0 until pos){
        newArray(i)(p) = array(p)(i)
      }
    }

    newArray
  }


  def readATypedFile(path:String, channel:Int):List[Double]={
    Source.fromFile(path)
      .getLines()
      .toList
    .map(_.split(" ")(channel))
    .filter(!_.startsWith(";"))
    .map(_.toDouble)
  }

  def readBTypedFile(path:String, channel:Int):List[(Double)]={
    Source.fromFile(path)
      .getLines()
      .toList
      .map(_.split(" ")(channel))
      .filter(!_.startsWith(";"))
      .map(_.toDouble)

  }
}

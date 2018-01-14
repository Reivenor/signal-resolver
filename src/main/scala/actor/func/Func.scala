package actor.func

import actor.Application

import scala.io.Source

/**
  * Created by gennady on 11/01/18.
  */
object Func {
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
  //Более эффективное по памяти транспонирование матрицы
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

  /**
    * Построение матрицы по опорным точкам сигнала Б из стгнала А
    * @param signalB ЗВП файл
    * @param signalA УЗВП файл
    * @return матрица сигналов А выстроенных по опорным точкам
    */
  def buildMatrix(signalB:List[(Double)], signalA:List[(Double)]):Array[Array[Double]]={
    /*Установка данных сигнала А относительно опорной точки из Б*/
    def matrixVectorConstructor(signalA:List[Double],
                                lengthB:Int,
                                positionB:Int):Array[Double]={
      var matrixVector = List.fill(lengthB)(0d).toArray
      signalA.copyToArray(matrixVector, positionB)
      matrixVector
    }
    //val standPlotCount = signalB.length / 500
    var positions:List[Int] = Nil
    for(i <- 0 until signalB.length by Application.FEQUENCY){ //for цикл с шагом в частоту
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

}

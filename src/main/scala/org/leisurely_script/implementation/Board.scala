package org.leisurely_script.implementation

import org.leisurely_script.gdl._

import scala.util.Try

/**
  * Created by mcsmash on 1/13/16.
  */
class Board(ruleSet:BoardRuleSet) {
  var occupancyMatrices:Map[PieceRule, Array[Array[Int]]] = {
    for (piece <- ruleSet.pieces)
      yield (piece -> Array.ofDim[Int](ruleSet.size(0), ruleSet.size(1)))
  }.toMap

  //def nInARow(n:Int, piece:PhysicalPiece):Boolean = { }
  protected def findInOccupancyMatrices(truthFunction:(Int)=>Boolean):Option[(PieceRule, Coordinate)] = {
    for (pair <- occupancyMatrices) {
      pair match {
        case (key:PieceRule, matrix:Array[Array[Int]]) => {
          findInMatrix(matrix, truthFunction) match {
            case Some(coordinate:Coordinate) => return Some((key, coordinate))
            case None => return None
          }
        }
      }
    }
    None
  }
  protected def findInMatrix(matrix:Array[Array[Int]], truthFunction:(Int)=>Boolean):Option[Coordinate] = {
    for (i <- 0 until matrix.size) {
      for (j <- 0 until matrix(i).size) {
        if (truthFunction(matrix(i)(j))) {
          return Some(Coordinate(i, j))
        }
      }
    }
    None
  }
  def empty:Boolean = {
    findInOccupancyMatrices(_ != 0) match {
      case Some(_) => false
      case None => true
    }
  }
  def full:Boolean = {
    //TODO: Full is more complex than empty. Need to allow the user to define what a full board even looks like.
    findInOccupancyMatrices(_ == 0) match {
      case Some(_) => false
      case None => true
    }
  }
  protected[leisurely_script] def push(thing:Equipment, coord:Coordinate) = {
    coord match {
      case Coordinate(x:Int, y:Int) => occupancyMatrices(thing.rule)(x)(y) += 1
    }
  }
  protected[leisurely_script] def pop(thing:Equipment, coord:Coordinate) = {
    coord match {
      case Coordinate(x: Int, y: Int) => occupancyMatrices(thing.rule)(x)(y) -= 1
    }
  }

}

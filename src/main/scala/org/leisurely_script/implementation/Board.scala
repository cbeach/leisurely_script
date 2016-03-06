package org.leisurely_script.implementation

import scala.collection.mutable.{Stack, ArrayBuffer};

import org.leisurely_script.gdl._

import scala.util.{Try, Success, Failure}

/**
  * Created by mcsmash on 1/13/16.
  */
class Board(rS:BoardRuleSet, nInARowFunc:Option[(Array[Array[Int]])=>Boolean]) {
  val boardRuleSet = rS
  val nInARowFunction = nInARowFunc
  var occupancyMatrices:Map[PieceRule, Array[Array[Int]]] = {
    for (piece <- boardRuleSet.pieces)
      yield (piece -> Array.ofDim[Int](boardRuleSet.size(0), boardRuleSet.size(1)))
  }.toMap
  var occupancyStacks:ArrayBuffer[ArrayBuffer[Stack[Equipment]]] = {
    val returnArray:ArrayBuffer[ArrayBuffer[Stack[Equipment]]]
      = new ArrayBuffer[ArrayBuffer[Stack[Equipment]]](boardRuleSet.size(0))
    for (i <- 0 until boardRuleSet.size(0)) {
      returnArray += {
        for (j <- 0 until boardRuleSet.size(1))
          yield new Stack[Equipment]()
      }.to[ArrayBuffer]
    }
    returnArray
  }
  def nInARow(piece:PhysicalPiece):Boolean = {
    nInARowFunction match {
      case Some(func) => func(occupancyMatrices(piece.rule))
      case None => throw new IllegalExecutionException("No rule requires nInARow. nInARow is unavailable.")
    }
  }
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
    //TODO: Full is more complex than empty. Need to allow the user to define what "full board" even means.
    findInOccupancyMatrices(_ == 0) match {
      case Some(_) => false
      case None => true
    }
  }
  protected[leisurely_script] def push(thing:Equipment, coord:Coordinate):Try[Board] = {
    coord match {
      case Coordinate(x:Int, y:Int) => {
        occupancyStacks(x)(y).push(thing)
        Success(this)
      }
    }
  }
  protected[leisurely_script] def pop(coord:Coordinate):Try[Board] = {
    coord match {
      case Coordinate(x: Int, y: Int) => {
        occupancyStacks(x)(y).pop()
        Success(this)
      }
    }
  }
  def nodes:List[BoardNode] = boardRuleSet.graph.nodes.toList
  def numberOfPieces:Int = {
    var accum = 0
    occupancyStacks.foreach((a:ArrayBuffer[Stack[Equipment]]) => {
      a match {
        case arrayBuffer: ArrayBuffer[Stack[Equipment]] => arrayBuffer.foreach({
          case stack:Stack[Equipment] => accum += stack.size
        })
      }
    })
    accum
  }
}

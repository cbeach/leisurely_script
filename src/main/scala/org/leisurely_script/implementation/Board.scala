package org.leisurely_script.implementation

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

import org.leisurely_script.gdl._

import scala.util.{Try, Success, Failure}

/**
  * Created by mcsmash on 1/13/16.
  */
class Board(rS:BoardRuleSet) {
  val boardRuleSet = rS
  private val nodeRows:mutable.HashMap[
    (Int, PieceRule, PlayerValidator),
    Set[Set[Coordinate]]
  ] = rS.nodeRows
  private val _occupancyMatrices:Map[PieceRule, Array[Array[Int]]] = {
    for (piece <- boardRuleSet.pieces)
      yield piece -> Array.ofDim[Int](boardRuleSet.size(0), boardRuleSet.size(1))
  }.toMap
  private val _occupancyStacks:ArrayBuffer[ArrayBuffer[mutable.Stack[Equipment]]] = {
    val returnArray:ArrayBuffer[ArrayBuffer[mutable.Stack[Equipment]]]
      = new ArrayBuffer[ArrayBuffer[mutable.Stack[Equipment]]](boardRuleSet.size(0))
    for (i <- 0 until boardRuleSet.size(0)) {
      returnArray += {
        for (j <- 0 until boardRuleSet.size(1))
          yield new mutable.Stack[Equipment]()
      }.to[ArrayBuffer]
    }
    returnArray
  }
  def nInARow(n:Int, piece:PhysicalPiece):Boolean = {
    val possibleRows = nodeRows((n, piece.rule, SomePlayers(piece.owner.getPlayers)))
    val matrix = _occupancyMatrices(piece.rule)
    var retVal = false
    if (n > 0) {
      possibleRows.takeWhile(row => !{
        retVal = row.map(_ match {
          case Coordinate(x: Int, y: Int) => matrix(x)(y) > 0
        }).reduce(_ && _)
        retVal
      })
    }
    retVal
  }
  protected def findInOccupancyMatrices(truthFunction:(Int)=>Boolean):Option[(PieceRule, Coordinate)] = {
    for (pair <- _occupancyMatrices) {
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
        _occupancyStacks(x)(y).push(thing)
        Success(this)
      }
    }
  }
  protected[leisurely_script] def pop(coord:Coordinate):Try[Board] = {
    coord match {
      case Coordinate(x: Int, y: Int) => {
        _occupancyStacks(x)(y).pop()
        Success(this)
      }
    }
  }
  def nodes:List[BoardNode] = boardRuleSet.graph.nodes.toList
  def numberOfPieces:Int = {
    var accum = 0
    _occupancyStacks.foreach((a:ArrayBuffer[mutable.Stack[Equipment]]) => {
      a match {
        case arrayBuffer: ArrayBuffer[mutable.Stack[Equipment]] => arrayBuffer.foreach({
          case stack:mutable.Stack[Equipment] => accum += stack.size
        })
      }
    })
    accum
  }
  def getNumberOfOccupancyMatrices:Int = _occupancyMatrices.size
  def getOccupancyMatricesDimensions(p:PieceRule):Tuple3[Option[Int], Option[Int], Option[Int]] = {
    boardRuleSet.size.size match {
      case 1 => (Some(_occupancyMatrices(p).length), None, None)
      case 2 => (Some(_occupancyMatrices(p).length),
                 Some(_occupancyMatrices(p)(0).length), None)
    }
  }
  def occupancyMatrices(p:PieceRule)(x:Int)(y:Int):Int = _occupancyMatrices(p)(x)(y)
  def occupancyStacks(x:Int)(y:Int) = _occupancyStacks(x)(y)
}

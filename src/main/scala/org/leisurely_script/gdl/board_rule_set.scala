package org.leisurely_script.gdl

import org.leisurely_script.gdl.expressions.{BoardFullExpression, BoardEmptyExpression, NInARowExpression}

import scala.util.{Try, Success, Failure}
import scala.util.control.Breaks._

import org.leisurely_script.implementation.Board

import Direction._
import NeighborType._
import Shape._

case class BoardRuleSet(val size:List[Int],
                        val boardShape:Shape,
                        val neighborType:NeighborType,
                        val nodeShape:Shape,
                        val pieces:List[PieceRule],
                        val graph:Graph = new Graph()) {
  var playableBoard:Option[Board] = None
  def this(other:BoardRuleSet) = {
    this(other.size, other.boardShape, other.neighborType, other.nodeShape, other.pieces, new Graph(other.graph))
  }
  def nodes() = {
    graph.nodesByCoord
  }
  def generateGraph() = {
    generateNodes()
    generateEdges()
  }
  def generateNodes() = {
    for (i <- 0 until size(0)) {
      for (j <- 0 until size(1)) {
        graph.add(BoardNode(Coordinate(i, j)))
      }
    }
  }
  def generateEdges() = {
    for (i <- 0 until size(0)) {
      for (j <- 0 until size(1)) {
        if (neighborType != NoDirect) {
          // N
          if (j > 0) {
            graph.add(BoardEdge((graph.nodesByCoord(Coordinate(i, j)), graph.nodesByCoord(Coordinate(i, j - 1))) , N))
          }

          // S
          if (j < size(1) - 1) {
            graph.add(BoardEdge((graph.nodesByCoord(Coordinate(i, j)), graph.nodesByCoord(Coordinate(i, j + 1))), S))
          }

          // E
          if (i > 0) {
            graph.add(BoardEdge((graph.nodesByCoord(Coordinate(i, j)), graph.nodesByCoord(Coordinate(i - 1, j))), E))
          }

          // W
          if (i < size(0) - 1) {
            graph.add(BoardEdge((graph.nodesByCoord(Coordinate(i, j)), graph.nodesByCoord(Coordinate(i + 1, j))), W))
          }
        }

        if (neighborType == Indirect) {
          // NE
          if (i > 0 && j > 0) {
            graph.add(BoardEdge((graph.nodesByCoord(Coordinate(i, j)), graph.nodesByCoord(Coordinate(i - 1, j - 1))), NE))
          }

          // NW
          if (i < size(0) - 1 && j > 0) {
            graph.add(BoardEdge((graph.nodesByCoord(Coordinate(i, j)), graph.nodesByCoord(Coordinate(i + 1, j - 1))), NW))
          }

          // SE
          if (i > 0 && j < size(1) - 1) {
            graph.add(BoardEdge((graph.nodesByCoord(Coordinate(i, j)), graph.nodesByCoord(Coordinate(i - 1, j + 1))) , SE))
          }

          // SW
          if (i < size(0) - 1 && j < size(1) - 1) {
            graph.add(BoardEdge((graph.nodesByCoord(Coordinate(i, j)), graph.nodesByCoord(Coordinate(i + 1, j + 1))), SW))
          }
        }
      }
    }
  }
  /**
   * Is the board empty?
   *
   * @return Boolean  true if there are no pieces on the board, false if there are
   */
  def empty:BoardEmptyExpression = {
    BoardEmptyExpression(this)
  }
  /**
   * Is the board full?
   *
   * @return Boolean  true if there are no more empty nodes on the board, false if there are
   */
  def full:BoardFullExpression = {
    BoardFullExpression(this)
  }
  def numberOfPieces(pieceCounter:(BoardNode)=>Int=null):Int = {
    graph.nodesByCoord.map(node => {
      pieceCounter match {
        case func:((BoardNode)=>Int) => func(node._2)
        case _ => node._2.equipment.size
      }
    }).sum
  }
  def push(thing:Equipment, coord:Coordinate):Try[BoardRuleSet] = {
    Try(new BoardRuleSet(size, boardShape, neighborType, nodeShape, pieces, graph.push(thing, coord).get))
  }
  def pop(coord:Coordinate):Try[BoardRuleSet] = {
    Try(new BoardRuleSet(size, boardShape, neighborType, nodeShape, pieces, graph.pop(coord).get))
  }
  def nInARow(n:Int, piece:PieceRule, player:PlayerValidator, neighborType:NeighborType=null):NInARowExpression = {
    NInARowExpression(n, piece, this, player, neighborType)
  }
  def wellFormed:Unit = {
    if (graph.nodesByCoord.size == 0) {
      throw new IllegalBoardException("The board must have nodes, no nodes found.")
    }
  }
  def getPlayableBoard:Try[Board] = {
    //TODO: Add error checking
    playableBoard match {
      case Some(pBoard: Board) => {
        Success(pBoard)
      }
      case None => {
        Success(new Board(this))
      }
    }
  }
}


object BoardRuleSet {
  def apply(size:List[Int], boardShape:Shape, neighborType:NeighborType, nodeShape:Shape, pieces:List[PieceRule]):BoardRuleSet = {
    val board = new BoardRuleSet(size, boardShape, neighborType, nodeShape, pieces)
    board.generateGraph()
    board
  }
}

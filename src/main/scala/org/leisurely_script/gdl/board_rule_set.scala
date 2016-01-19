package org.leisurely_script.gdl

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
  def empty(truthFunction:(BoardNode)=>Boolean=null):Boolean = {
    for (node <- graph.nodesByCoord) {
      if (node._2.empty(truthFunction) == false) {
        return false
      }
    }
    true
  }
  /**
   * Is the board full?
   *
   * @return Boolean  true if there are no more empty nodes on the board, false if there are
   */
  def full(truthFunction:(BoardNode)=>Boolean=null):Boolean = {
    for (node <- graph.nodesByCoord) {
      if (node._2.empty(truthFunction) == true) {
        return false
      }
    }
    true
  }
  def numberOfPieces(pieceCounter:(BoardNode)=>Int=null):Int = {
    graph.nodesByCoord.map(node => {
      pieceCounter match {
        case func:((BoardNode)=>Int) => func(node._2)
        case _ => node._2.equipment.size
      }
    }).reduce(_+_)
  }
  def push(thing:Equipment, coord:Coordinate):Try[BoardRuleSet] = {
    Try(new BoardRuleSet(size, boardShape, neighborType, nodeShape, pieces, graph.push(thing, coord).get))
  }
  def pop(coord:Coordinate):Try[BoardRuleSet] = {
    Try(new BoardRuleSet(size, boardShape, neighborType, nodeShape, pieces, graph.pop(coord).get))
  }
  def nInARow(n:Int, piece:PhysicalPiece, neighborType:NeighborType=null):Set[ConcretelyKnownPlayer] = {
    def recursiveWalk(x:Int, n:Int, thisNode:BoardNode, piece:PhysicalPiece, direction:Direction=null):Boolean = {
      val matchingPieces = thisNode.equipment.filter(eq => {
        eq match {
          case p:PhysicalPiece => piece.name == p.name && piece.owner.getPlayers == p.owner.getPlayers
          case _ => false
        }
      })

      val edges = direction match {
        case null => graph.outEdges(thisNode.coord)
        case _ => graph.outEdges(thisNode.coord).filter(edge => direction == edge.direction)
      }

      if (matchingPieces.length == 0
      || (x != n - 1 && edges.length == 0)) {
        false
      } else if (x != n - 1) {
        (for (e <- edges) yield recursiveWalk(x + 1, n, e.nodes._2, piece, e.direction)).exists(x => x == true)
      } else if (x == n - 1 && matchingPieces.length > 0) {
        true
      } else {
        false
      }
    }

    var players:Set[ConcretelyKnownPlayer] = Set()
    for ((coord, node) <- graph.nodesByCoord) {
      if (recursiveWalk(0, n, node, piece)) {
        piece.owner match {
          case tempPlayers:ConcretelyKnownPlayer => players = players ++ tempPlayers.getPlayers
          case _ => throw new IllegalPlayerException("Equipment that has been placed on the board must be owned by ConcretelyKnownPlayers.")
        }
      }
    }
    return players
  }
  def wellFormed:Unit = {
    if (graph.nodesByCoord.size == 0) {
      throw new IllegalBoardException("The board must have nodes, no nodes found.")
    }
  }
  def getPlayableBoard(nInARow:Int=0):Try[Board] = {
    //TODO: Add error checking
    var nInARowFunc:Option[(Array[Array[Int]])=>Boolean] = None
    if (nInARow > 0) {
      val possibleRows = graph.setOfNLengthRows(nInARow)
      nInARowFunc = Some((matrix) => {
        var retVal:Boolean = false
        possibleRows.takeWhile(row => !{
          val boolFunc: (Coordinate)=>Boolean = (coord:Coordinate) => {
            coord match {
              case Coordinate(x: Int, y: Int) => matrix(x)(y) > 0
            }
          }
          retVal = row.map(boolFunc).reduce(_ && _)
          retVal
        })
        retVal
      })
    }
    Success(new Board(this, nInARowFunc))
  }
}


object BoardRuleSet {
  def apply(size:List[Int], boardShape:Shape, neighborType:NeighborType, nodeShape:Shape, pieces:List[PieceRule]):BoardRuleSet = {
    val board = new BoardRuleSet(size, boardShape, neighborType, nodeShape, pieces)
    board.generateGraph()
    board
  }
}

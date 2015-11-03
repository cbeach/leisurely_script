package org.leisurelyscript

import scala.util.{Try, Success, Failure}
import scala.util.control.Breaks._

import Direction._
import NeighborType._
import Shape._


class Board(val size:List[Int], 
    val boardShape:Shape, 
    val neighborType:NeighborType, 
    val nodeShape:Shape,
    val graph:Graph = new Graph()) extends Equipment {

    def this(other:Board) = {
        this(other.size, other.boardShape, other.neighborType, other.nodeShape, new Graph(other.graph))
    }

    def nodes() = {
        graph.nodes
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
                        graph.add(BoardEdge((graph.nodes(Coordinate(i, j)), graph.nodes(Coordinate(i, j - 1))) , N))
                    }

                    // S
                    if (j < size(1) - 1) {
                        graph.add(BoardEdge((graph.nodes(Coordinate(i, j)), graph.nodes(Coordinate(i, j + 1))), S))
                    }

                    // E
                    if (i > 0) {
                        graph.add(BoardEdge((graph.nodes(Coordinate(i, j)), graph.nodes(Coordinate(i - 1, j))), E))
                    }

                    // W
                    if (i < size(0) - 1) {
                        graph.add(BoardEdge((graph.nodes(Coordinate(i, j)), graph.nodes(Coordinate(i + 1, j))), W))
                    }
                }

                if (neighborType == Indirect) {
                    // NE
                    if (i > 0 && j > 0) { 
                        graph.add(BoardEdge((graph.nodes(Coordinate(i, j)), graph.nodes(Coordinate(i - 1, j - 1))), NE))
                    }

                    // NW
                    if (i < size(0) - 1 && j > 0) { 
                        graph.add(BoardEdge((graph.nodes(Coordinate(i, j)), graph.nodes(Coordinate(i + 1, j - 1))), NW))
                    }
                    
                    // SE
                    if (i > 0 && j < size(1) - 1) { 
                        graph.add(BoardEdge((graph.nodes(Coordinate(i, j)), graph.nodes(Coordinate(i - 1, j + 1))) , SE))
                    }
                    
                    // SW
                    if (i < size(0) - 1 && j < size(1) - 1) { 
                        graph.add(BoardEdge((graph.nodes(Coordinate(i, j)), graph.nodes(Coordinate(i + 1, j + 1))), SW))
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
        for (node <- graph.nodes) {
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
        for (node <- graph.nodes) {
            if (node._2.empty(truthFunction) == true) {
                return false
            }
        }
        true
    }

    def place(thing:Equipment, coord:Coordinate):Try[Board] = {
        val newBoard = new Board(this)
        val node = Try(newBoard.graph.nodes(coord))
        node match {
            case Success(node:BoardNode) => {
                node.equipment = thing :: node.equipment
                Success(newBoard)
            }
            case Failure(exception) => Failure(exception)
        }
    }

    def nInARow(n:Int, piece:Piece, neighborType:NeighborType=null):Set[Player] = {
        def recursiveWalk(x:Int, n:Int, e:BoardEdge, piece:Piece):Boolean = {
            val thisNode = e.boardNodes._1
            val matchingPieces = thisNode.equipment.filter(eq => {
                eq match {
                    case p:Piece => piece.name == p.name && piece.owner == p.owner
                    case _ => false
                }
            })

            val edges = thisNode.edges.filter(edge => e.direction == edge.direction)

            if (matchingPieces.length == 0 
            || (x != n && edges.length == 0)) {
                false
            } else if (x != n) {
                (for (e <- edges) yield recurse(x + 1, n, e, piece)).exists(x => x)
            } else {
                true
            }
        }

        var players:Set[Player] = Set()
        for (node <- graph.nodes) {
            for (edge <- node._2.edges) {
                if (recursiveWalk(0, n, edge, piece)) {
                    players += piece.owner
                }
            }
        }
        return players
    }
}


object Board {
    def apply(size:List[Int], boardShape:Shape, neighborType:NeighborType, nodeShape:Shape):Board = {
        val board = new Board(size, boardShape, neighborType, nodeShape)
        board.generateGraph()
        board
    }
}


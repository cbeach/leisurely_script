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

    def numberOfPieces(pieceCounter:(BoardNode)=>Int=null):Int = {
        graph.nodes.map(node => {
            pieceCounter match {
                case func:((BoardNode)=>Int) => func(node._2)
                case _ => node._2.equipment.size
            }
        }).reduce(_+_)
    }

    def push(thing:Equipment, coord:Coordinate):Try[Board] = {
        Try(new Board(size, boardShape, neighborType, nodeShape, graph.push(thing, coord).get))
    }

    def pop(coord:Coordinate):Try[Board] = {
        Try(new Board(size, boardShape, neighborType, nodeShape, graph.pop(coord).get))
    }

    def nInARow(n:Int, piece:Piece, neighborType:NeighborType=null):Set[Player] = {
        def recursiveWalk(x:Int, n:Int, thisNode:BoardNode, piece:Piece, direction:Direction=null):Boolean = {
            val matchingPieces = thisNode.equipment.filter(eq => {
                eq match {
                    case p:Piece => piece.name == p.name && piece.owner == p.owner
                    case _ => false
                }
            })

            val edges = direction match {
                case null => thisNode.edges
                case _ => thisNode.edges.filter(edge => direction == edge.direction)
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

        var players:Set[Player] = Set()
        for ((coord, node) <- graph.nodes) {
            if (recursiveWalk(0, n, node, piece)) {
                players += piece.owner
            }
        }
        return players
    }
    
    def wellFormed:Unit = {
        if (graph.nodes.size == 0) {
            throw new IllegalBoardException("The board must have nodes, no nodes found.")
        }
    }
}


object Board {
    def apply(size:List[Int], boardShape:Shape, neighborType:NeighborType, nodeShape:Shape):Board = {
        val board = new Board(size, boardShape, neighborType, nodeShape)
        board.generateGraph()
        board
    }
}


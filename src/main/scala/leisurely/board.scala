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

    def pop(coord:Coordinate):Try[Board] = {
        val newBoard = new Board(this)
        val node = Try(newBoard.graph.nodes(coord))
        node match {
            case Success(node:BoardNode) => Try(node.equipment = node.equipment.tail) match {
                case Success(_) => Success(newBoard)
                case Failure(_) => Failure(new Exception(s"Could not pop piece off of node ${coord})"))
            }
            case Failure(exception) => Failure(exception)
        }
        
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
            println(s"x:${x}")
            thisNode.edges.foreach(e => println(s"(${e.boardNodes._1.coord.x}, ${e.boardNodes._1.coord.y}) -> (${e.boardNodes._2.coord.x}, ${e.boardNodes._2.coord.y}): ${e.direction}"))
            println()
            //println(s"${"\t" * x}x:${x}, n:${n}, coord:(${thisNode.coord.x}, ${thisNode.coord.y}), dir:${direction} edges:${thisNode.edges.size}, ${edges.size}")

            if (matchingPieces.length == 0 
            || (x != n - 1 && edges.length == 0)) {
                println(s"${"\t" * x}false: matchingPieces:${matchingPieces.length} x:${x} == n:${n} edges.length:${edges.length}")
                false
            } else if (x != n - 1) {
                val temp = (for (e <- edges) yield recursiveWalk(x + 1, n, e.boardNodes._2, piece, e.direction))
                println(s"${"\t" * x}recurse: ${temp}")
                temp.exists(x => x == true)
            } else if (x == n - 1 && matchingPieces.length > 0) {
                println(s"${"\t" * x}true")
                true
            } else {
                println("Default false")
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
}


object Board {
    def apply(size:List[Int], boardShape:Shape, neighborType:NeighborType, nodeShape:Shape):Board = {
        val board = new Board(size, boardShape, neighborType, nodeShape)
        board.generateGraph()
        board
    }
}


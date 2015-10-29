package org.leisurelyscript

import Direction._
import NeighborType._
import Shape._


class Board(val size:List[Int], val boardShape:Shape, val neighborType:NeighborType, val nodeShape:Shape) {
    val graph:Graph = new Graph()

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
}


object Board {
    def apply(size:List[Int], boardShape:Shape, neighborType:NeighborType, nodeShape:Shape):Board = {
        val board = new Board(size, boardShape, neighborType, nodeShape)
        board.generateGraph()
        board
    }
}


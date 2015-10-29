package org.leisurelyscript

import Direction._
import NeighborType._
import Shape._


class Board() {
    val graph:Graph = new Graph()
    var size:List[Int] = List()
    var boardShape:Shape = Square
    var neighborType:NeighborType = Direct
    var nodeShape:Shape = Square

    def this(size:List[Int], boardShape:Shape, neighborType:NeighborType, nodeShape:Shape) {
        this()
        this.size = size
        this.boardShape = boardShape
        this.neighborType = neighborType
        this.nodeShape = nodeShape
    }

    def nodes() = {
        graph.nodes
    }

    def generateGraph() = {
        generateNodes()
        generateEdges()
    }

    def generateNodes() = {
        for (i <- 0 to size(0)) {
            for (j <- 0 to size(1)) {
                graph.add(BoardNode(Coordinate(i, j)))
            }
        }
    }

    def generateEdges() = {
        for (i <- 0 to size(0)) {
            for (j <- 0 to size(1)) {
                if (neighborType != NoDirect) {
                    // Horizontal (Left to right for digraphs)
                    if (i == 0 || i % size(0) != 2) {
                        graph.add(BoardEdge((graph.nodes(i), graph.nodes(i + 1)), N))
                        graph.add(BoardEdge((graph.nodes(i + 1), graph.nodes(i)), S))
                    }

                    // Vertical (Top to bottom for digraphs)
                    if (i < size(0) * (size(1) - 1)) {
                        graph.add(BoardEdge((graph.nodes(i + size(0)), graph.nodes(i)) , N))
                        graph.add(BoardEdge((graph.nodes(i), graph.nodes(i + size(0))), S))
                    }
                }

                if (neighborType == Indirect) {
                    // Diagonal edges
                }
            }
        }
    }
}

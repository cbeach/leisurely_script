package org.leisurelyscript

import scalax.collection.Graph // or scalax.collection.mutable.Graph
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._


class Board() {
    val graph:Graph[BoardNode, BoardEdge] = Graph(BoardEdge( (new BoardNode(0, 0), new BoardNode(0, 1)), "E"))
    def this(size:List[Int], boardShape:Shape, neighborType:NeighborTypes, nodeShape:Shape) {
        this()
           
    }
    def nodes() = {
        graph.nodes
    }
}

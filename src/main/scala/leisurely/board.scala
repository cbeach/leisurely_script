package org.leisurelyscript


class Board() {
    val graph:Graph[BoardNode, UnDiEdge] = Graph()
    def this(size:List[Int], boardShape:Shape, neighborType:NeighborTypes, nodeShape:Shape) {
        this()
    }
    def nodes() = {
        graph.nodes
    }
}

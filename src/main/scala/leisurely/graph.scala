package org.leisurelyscript


class Graph(var nodes:Map[Coordinate, BoardNode]=Map(), 
    var edges:List[BoardEdge]=List()) {

    def this(other:Graph) = {
        this(other.nodes, other.edges)
    }

    def add(node:BoardNode) = {
        nodes += (node.coord -> node)
    }

    def add(edge:BoardEdge) = {
        edges = edge :: edges
        val source = this.nodes(edge.boardNodes._1.coord)
        source.edges = edge :: source.edges
    }
}

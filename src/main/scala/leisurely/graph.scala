package org.leisurelyscript


class Graph {
    var nodes:List[BoardNode] = List()
    var edges:List[BoardEdge] = List()
    def add(node:BoardNode) = {
        nodes = node :: nodes 
    }
    def add(edge:BoardEdge) = {
        edges = edge :: edges
    }
}

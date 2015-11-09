package org.leisurelyscript

import scala.util.{Try, Success, Failure}

import Direction._


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
        val source = this.nodes(edge.nodes._1.coord)
        source.edges = edge :: source.edges
    }

    def push(thing:Equipment, coord:Coordinate):Try[Graph] = {
        val oldNode = nodes(coord)
        val newNode = BoardNode(coord, thing :: oldNode.equipment)
        replaceNode(newNode)
    }

    def pop(coord:Coordinate):Try[Graph] = {
        val oldNode = nodes(coord)
        val newNode = BoardNode(coord, oldNode.equipment.tail)
        replaceNode(newNode)
    }

    def replaceNode(newNode:BoardNode):Try[Graph] = {
        val oldNode = nodes(newNode.coord)
        newNode.edges = oldNode.edges.map(edge => {
            BoardEdge((newNode, edge.nodes._2), edge.direction)
        })

        // Create new edges for the new node
        val newEdges:List[BoardEdge] = oldNode.edges.map(edge=>{
            BoardEdge((newNode, edge.nodes._2), edge.direction) :: edge.nodes._2.edges.filter(e=>e.nodes._2 == oldNode).map(e=>{  
                    BoardEdge((e.nodes._1, newNode), e.direction)
                })
        }).flatten

        // Travel to all nodes that are destinations of new node and update their edges
        oldNode.edges.foreach(edge => {
            edge.nodes._2.edges = edge.nodes._2.edges.map(edge2 => {
                // If the destination of this edge is the old node 
                if (edge2.nodes._2 eq oldNode) {
                    // Return a new edge with a new destination
                    edge2.copy(nodes=(edge2.nodes._1, newNode))
                } else {
                    // Nothing needs to be done
                    edge2
                }
            })
        })

        val newNodeMap = nodes - oldNode.coord + (newNode.coord -> newNode)
        val newEdgeList = newEdges ++ edges.filter(e => {
            !newEdges.contains(e)
        })
        Try(new Graph(newNodeMap, newEdgeList))

    }

    override def toString:String = {
        var string:String = ""
        nodes.foreach(pair => {
            string += s"${pair._2.label}: (${pair._2.coord.x}, ${pair._2.coord.y})\n"
            pair._2.edges.foreach(edge => {
                string += s"\t-> ${edge.nodes._2.label}: (${edge.nodes._2.coord.x}, ${edge.nodes._2.coord.y})\n"
            })
        })
        string
    }
}

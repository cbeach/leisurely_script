package org.leisurelyscript.gdl

import scala.util.{Try, Success, Failure}
import scala.collection.mutable


case class Graph(var nodes:Map[Coordinate, BoardNode]=Map(),
                 var edges:List[BoardEdge]=List(),
                 val outEdges:mutable.HashMap[Coordinate, List[BoardEdge]]=mutable.HashMap()) {
  outEdges ++= edges.groupBy[Coordinate](edge => {
    edge.nodes._1.coord
  })
  def this(other:Graph) = {
    this(other.nodes, other.edges, other.outEdges)
  }
  def add(node:BoardNode):Graph = {
    nodes += (node.coord -> node)
    this
  }
  def add(edge:BoardEdge):Graph = {
    if (nodes.contains(edge.nodes._1.coord)) {
      if (!nodes.contains(edge.nodes._2.coord)) {
        throw(new IllegalBoardEdgeException("The destination node does not exist in this graph instance"))
      }
      val sourceNodeCoordinate = edge.nodes._1.coord
      // Make sure that the source node doesn't already have an edge that travels in the given direction
      outEdges.get(sourceNodeCoordinate) match {
        case Some(newOutEdges:List[BoardEdge]) => {
          newOutEdges.foreach(e => {
            if (edge.direction == e.direction) {
              throw (new IllegalBoardEdgeException("A node can not have two edges with the same direction."))
            }
          })
        }
        case None => {} //Do nothing. This just means that this is the first edge to leave this particular node.
      }
      edges = edge :: edges
      outEdges.get(sourceNodeCoordinate) match {
        case Some(existingEdges:List[BoardEdge]) =>
          outEdges += (sourceNodeCoordinate -> (edge :: existingEdges))
        case None => outEdges += (sourceNodeCoordinate -> List(edge))
      }
    } else {
      throw(new IllegalBoardEdgeException("The source node does not exist in this graph instance"))
    }
    this
  }
  def push(thing:Equipment, coord:Coordinate):Try[Graph] = {
    val (newNodeMap, newEdgeList) = deepCopyNodesAndEdges
    val oldNode = nodes(coord)
    val newNode = BoardNode(coord, thing :: oldNode.equipment)
    replaceNode(newNode, newNodeMap, newEdgeList)
  }
  def pop(coord:Coordinate):Try[Graph] = {
    val (newNodeMap, newEdgeList) = deepCopyNodesAndEdges
    val oldNode = nodes(coord)
    val newNode = BoardNode(coord, oldNode.equipment.tail)
    replaceNode(newNode, newNodeMap, newEdgeList)
  }
  def deepCopyNodesAndEdges:Tuple2[Map[Coordinate, BoardNode], List[BoardEdge]] = {
    val newNodeList = for (node <- nodes) yield BoardNode(
       Coordinate(node._1.x, node._1.y))

    nodes.values.zip(newNodeList).foreach({
      case (oldNode:BoardNode, newNode:BoardNode) => {
        newNode.equipment = oldNode.equipment.map(_.copy)
      }
    })

    val newNodeMap:Map[Coordinate, BoardNode] = newNodeList.map(node => {
      (node.coord, node)
    }).toMap

    val newEdgeList = edges.map(oldEdge => {
      val a:BoardNode = newNodeMap(oldEdge.nodes._1.coord)
      val b:BoardNode = newNodeMap(oldEdge.nodes._2.coord)
      BoardEdge((a, b), oldEdge.direction)
    })

    newEdgeList.foreach(edge => {
      val sourceNode = edge.nodes._1
      outEdges + (sourceNode.coord -> (edge :: outEdges(sourceNode.coord)))
    })
    (newNodeMap, newEdgeList)
  }
  def replaceNode(newNode:BoardNode,
          nodeMap:Map[Coordinate, BoardNode],
          edgeList:List[BoardEdge]):Try[Graph] = {
    //Grab the old node and update the new node's list of outgoing edges
    val oldNode = nodeMap(newNode.coord)
    outEdges + (newNode.coord -> outEdges(oldNode.coord).map(edge => {
      BoardEdge((newNode, edge.nodes._2), edge.direction)
    }))

    val updatedEdgeList:List[BoardEdge] = edgeList.map(edge => {
      if (edge.nodes._1 == oldNode) {
        BoardEdge((newNode, edge.nodes._2), edge.direction)
      } else if (edge.nodes._2 == oldNode) {
        BoardEdge((edge.nodes._1, newNode), edge.direction)
      } else {
        edge
      }
    })

    val updatedNodeMap:Map[Coordinate, BoardNode] = nodeMap.map {
      case (coord:Coordinate, node:BoardNode) => {
        // If this is the old node then just return a ref to the new one
        if (node eq oldNode) {
          (coord, newNode)
        } else {
          (coord, node)
        }
      }
    }

    Try(new Graph(updatedNodeMap, updatedEdgeList))
  }
  override def toString:String = {
    var string:String = ""
    nodes.foreach(pair => {
      string += s"${pair._2.label}: (${pair._2.coord.x}, ${pair._2.coord.y})\n"
      outEdges(pair._2.coord).foreach(edge => {
        string += s"\t-> ${edge.nodes._2.label}: (${edge.nodes._2.coord.x}, ${edge.nodes._2.coord.y})\n"
      })
    })
    string
  }
}

object Graph {

}

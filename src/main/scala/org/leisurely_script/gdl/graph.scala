package org.leisurely_script.gdl

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer
import scala.util.{Try, Success, Failure}
import scala.collection.mutable


case class Graph(val nodes:mutable.ListBuffer[BoardNode]=mutable.ListBuffer(),
                 val edges:mutable.ListBuffer[BoardEdge]=mutable.ListBuffer()) {

  val nodesByCoord:mutable.HashMap[Coordinate, BoardNode] =
    mutable.HashMap(nodes.groupBy(_.coord).map(pair => (pair._1, pair._2(0))).toSeq: _*)

  val outEdges = mutable.HashMap[Coordinate, mutable.ListBuffer[BoardEdge]]()
  outEdges ++= edges.groupBy[Coordinate](edge => {
    edge.nodes._1.coord
  })
  def this(nL:List[BoardNode],
           eL:List[BoardEdge]) = {
    this(mutable.ListBuffer(nL.toSeq: _*), mutable.ListBuffer(eL.toSeq: _*))
  }
  def this(other:Graph) = {
    this(other.nodes, other.edges)
  }
  def add(node:BoardNode):Unit = {
    nodes.append(node)
    nodesByCoord(node.coord) = node
  }
  def add(edge:BoardEdge):Unit = {
    if (nodesByCoord.contains(edge.nodes._1.coord)) {
      if (!nodesByCoord.contains(edge.nodes._2.coord)) {
        throw(new IllegalBoardEdgeException("The destination node does not exist in this graph instance"))
      }
      val sourceNodeCoordinate = edge.nodes._1.coord
      // Make sure that the source node doesn't already have an edge that travels in the given direction
      outEdges.get(sourceNodeCoordinate) match {
        case Some(newOutEdges:ListBuffer[BoardEdge]) => {
          newOutEdges.foreach(e => {
            if (edge.direction == e.direction) {
              throw (new IllegalBoardEdgeException("A node can not have two edges with the same direction."))
            }
          })
        }
        case None => {} //Do nothing. This just means that this is the first edge to leave this particular node.
      }
      edges.append(edge)
      outEdges.get(edge.nodes._1.coord) match {
        case Some(eLB:mutable.ListBuffer[BoardEdge]) => eLB.append(edge)
        case None => outEdges.put(edge.nodes._1.coord, ListBuffer(edge))
      }
    } else {
      throw(new IllegalBoardEdgeException("The source node does not exist in this graph instance"))
    }
  }
  def push(thing:Equipment, coord:Coordinate):Try[Graph] = {
    val (newNodeMap, newEdgeList) = deepCopyNodesAndEdges
    val oldNode = nodesByCoord(coord)
    val newNode = BoardNode(coord, thing :: oldNode.equipment)
    replaceNode(newNode, newNodeMap, newEdgeList)
  }
  def pop(coord:Coordinate):Try[Graph] = {
    val (newNodeMap, newEdgeList) = deepCopyNodesAndEdges
    val oldNode = nodesByCoord(coord)
    val newNode = BoardNode(coord, oldNode.equipment.tail)
    replaceNode(newNode, newNodeMap, newEdgeList)
  }
  def deepCopyNodesAndEdges:Tuple2[Map[Coordinate, BoardNode], List[BoardEdge]] = {
    val newNodeList = for (node <- nodesByCoord) yield BoardNode(
       Coordinate(node._1.x, node._1.y))

    nodesByCoord.values.zip(newNodeList).foreach({
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
      outEdges + (sourceNode.coord -> (edge +: outEdges(sourceNode.coord)))
    })
    (newNodeMap, newEdgeList.toList)
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

    val updatedNodes:List[BoardNode] = nodeMap.map({
      case (coord:Coordinate, node:BoardNode) => {
        // If this is the old node then just return a ref to the new one
        if (node eq oldNode) {
          newNode
        } else {
          node
        }
      }
    }).toList
    Try(new Graph(updatedNodes, updatedEdgeList))
  }
  override def toString:String = {
    var string:String = ""
    nodesByCoord.foreach(pair => {
      string += s"${pair._2.label}: (${pair._2.coord.x}, ${pair._2.coord.y})\n"
      outEdges(pair._2.coord).foreach(edge => {
        string += s"\t-> ${edge.nodes._2.label}: (${edge.nodes._2.coord.x}, ${edge.nodes._2.coord.y})\n"
      })
    })
    string
  }
  def setOfNLengthRows(n:Int): Set[List[Coordinate]] = {
    @tailrec
    def takeUntilN(accum:mutable.Queue[BoardEdge]):Option[List[Coordinate]] = {
      if (accum.size == n - 1) {
        return Some({
          (accum.map(_.nodes._1.coord).toList :+ accum.last.nodes._2.coord).sorted
        })
      } else {
        // There can only be one outgoing edge per direction per node, so the following expression
        // can only yield a list with length 0 or 1.
        val direction = accum.last.direction
        Try(outEdges(accum.last.nodes._2.coord)).getOrElse(List[BoardEdge]()).find(_.direction == direction)
        match {
          case Some(edge:BoardEdge) => accum.enqueue(edge)
          case None => return None
        }
      }
      takeUntilN(accum)
    }
    var rowSet = Set[List[Coordinate]]()
    for (node <- nodes) {
      for (edge <- outEdges.getOrElse(node.coord,
          mutable.ListBuffer[BoardEdge]()).toList) {
        takeUntilN(mutable.Queue(edge)).foreach(list => {
          rowSet = rowSet + list
        })
      }
    }
    rowSet
  }
}

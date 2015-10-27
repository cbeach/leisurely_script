package org.leisurelyscript

import scalax.collection.Graph // or scalax.collection.mutable.Graph
import scalax.collection.GraphPredef._
import scalax.collection.GraphEdge._


case class BoardEdge[N](boardNodes:Product, val edgeLabel:String) 
    extends UnDiEdge[N](boardNodes)
    with    ExtendedKey[N]
    with    EdgeCopy[BoardEdge]
    with    OuterEdge[N,BoardEdge] {
    def keyAttributes = Seq(edgeLabel)
    override def copy[NN](newNodes: Product) =
        new BoardEdge[NN](newNodes, edgeLabel)
}

object BoardEdge {
  def apply(from: BoardNode, to: BoardNode, edgeLabel:String) =
    new BoardEdge[BoardNode](NodeProduct(from, to), edgeLabel)
  //def unapply(e: BoardEdge[BoardNode]): Option[(BoardNode, BoardNode, String)] =
  //    if (e eq null) None else Some(e._1, e._2, e.edgeLabel)
}
object BoardEdgeImplicitWrapper {
    implicit class BoardEdgeAssoc[A <: BoardNode](val e: UnDiEdge[A]) {
      @inline def ## (edgeLabel: String) =
        new BoardEdge[A](e.nodes, edgeLabel) with OuterEdge[A,BoardEdge]
    }
}

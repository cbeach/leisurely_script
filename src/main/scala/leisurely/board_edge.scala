package org.leisurelyscript

import Direction._

case class BoardEdge(nodes:Tuple2[BoardNode, BoardNode], 
    direction:Direction) {
    override def toString:String = {
        s"(${nodes._1.coord.x}, ${nodes._1.coord.y}) -> (${nodes._2.coord.x}, ${nodes._2.coord.y}): direction: ${direction}"
    }
}

package org.leisurelyscript

import Direction._

case class BoardEdge(boardNodes:Tuple2[BoardNode, BoardNode], 
    direction:Direction) {}

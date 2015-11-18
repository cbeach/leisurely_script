package org.leisurelyscript

import MoveAction._

case class Move(piece:Piece, player:Player, action:MoveAction, node:BoardNode) { 
    override def toString:String = {
        s"Move(${piece}, ${player}, ${action}, ${node})"
    }
}

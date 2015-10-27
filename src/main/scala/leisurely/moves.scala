package org.leisurelyscript

import MoveAction._

case class LegalMove(val owner:Player, val precondition:(Game) => Boolean, action:MoveAction, val postcondition:() => Boolean) {
    def legal(game:Game, player:Player, move:Move):Boolean = {
        if (precondition(game) == true) {
            // create a temporary move
            // test postcondition
            true
        } else {
            false
        }
    }
}

case class Move(val piece:Piece, val player:Player, val action:MoveAction, val tile:BoardNode) {}

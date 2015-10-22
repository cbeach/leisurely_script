package org.leisurelyscript

case class LegalMove(val owner:Player, val precondition:(game:Game) => Boolean, action:MoveAction, val postcondition:() => Boolean) {
    def legal(game:Game)(player:Player, move:Move):Boolean = {
        if (precondition(game) == true) {
            // create a temporary move
            // test postcondition
        } else {
            false
        }
    }
}

case class Move(val piece:Piece, val player:Player, val action:MoveAction, val tile:BoardTile) {}

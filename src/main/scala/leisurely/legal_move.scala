package org.leisurelyscript

import MoveAction._


class LegalMove(val owner:Player, val precondition:(Game) => Boolean, val action:MoveAction, val postcondition:() => Boolean) {
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


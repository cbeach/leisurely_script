package org.leisurelyscript

import MoveAction._


class LegalMove(
    val owner:Player, 
    val precondition:(Game, Move)=>Boolean=(game:Game, move:Move)=>true, 
    val action:MoveAction, 
    val postcondition:(Game, Move)=>Boolean=null) {

    def legal(game:Game, move:Move):Boolean = {
        if (!owner.valid(game, move.player)) {
            return false 
        } else if (precondition(game, move)) {
            if (postcondition == null) {
                true
            } else {
                game.nonValidatedApplyMove(move)
                postcondition(game, move)
            }
        } else {
            false
        }
    }
}

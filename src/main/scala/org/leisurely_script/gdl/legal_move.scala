package org.leisurely_script.gdl

import MoveAction._


class LegalMove(
  val owner:PlayerValidator,
  val precondition:(Game, Move)=>Boolean=(game:Game, move:Move)=>true,
  val action:MoveAction,
  val postcondition:(Game, Move)=>Boolean=null) {

  def legal(game:Game, move:Move):Boolean = {
    if (!owner.playersValid(game, move.player)) {
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

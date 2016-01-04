package org.leisurely_script.gdl

import org.leisurely_script.implementation.Game


case class EndCondition(result:GameResultState.Value, affectedPlayer:PlayerValidator, private val condition:(Game, Player) => Boolean) {
  def conditionMet(game:Game, player:Player):Boolean = {
    condition(game, player)
  }
}

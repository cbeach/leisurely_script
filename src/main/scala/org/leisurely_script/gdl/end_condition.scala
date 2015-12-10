package org.leisurelyscript.gdl


case class EndCondition(result:GameResultState.Value, affectedPlayer:PlayerValidator, private val condition:(Game, Player) => Boolean) {
    def conditionMet(game:Game, player:Player):Boolean = {
        condition(game, player)
    }
}

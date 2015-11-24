package org.leisurelyscript


case class EndCondition(result:GameResultState.Value, player:Player, condition:(Game, Player) => Boolean) {}

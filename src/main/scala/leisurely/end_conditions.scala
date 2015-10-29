package org.leisurelyscript


case class EndCondition(result:GameResult.Value, player:Player, condition:(Game) => Boolean) {}

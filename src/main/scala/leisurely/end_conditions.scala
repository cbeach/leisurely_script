package org.leisurelyscript


case class EndCondition(result:GameResult, player:Player, condition:(Game) => Boolean) {}

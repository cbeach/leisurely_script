package org.leisurelyscript.gdl


case class GameResult(
    val result:GameResultState.Value, 
    val ranking:Option[List[List[Player]]], 
    val conditionThatWasMet:Option[EndCondition]=None) {}

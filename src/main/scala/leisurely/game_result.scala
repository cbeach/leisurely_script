package org.leisurelyscript


case class GameResult(
    val result:GameResultState.Value, 
    val ranking:List[List[Player]], 
    val conditionThatWasMet:EndCondition=null) {}

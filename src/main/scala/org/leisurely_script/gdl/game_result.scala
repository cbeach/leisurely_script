package org.leisurely_script.gdl

import GameResultState._

case class GameResult(
  result:GameResultState.Value,
  ranking:Option[List[List[Player]]],
  conditionThatWasMet:Option[EndCondition]=None) {}

case class SGameResult(player:Set[Player]=Set(), result:GameResultState.Value=Pending) {}

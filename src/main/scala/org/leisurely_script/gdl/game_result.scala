package org.leisurely_script.gdl

import GameResultState._
import org.leisurely_script.implementation.Game

case class GameResult(
  result:GameResultState.Value,
  ranking:Option[List[List[Player]]],
  conditionThatWasMet:Option[EndCondition]=None) {}

case class SGameResult(private val _players:PlayerValidator, result:GameResultState.Value=Pending) {
  def players:PlayerValidator = _players
  def players(game:Game):Set[Player] = {
    _players.getPlayers(game)
  }
}

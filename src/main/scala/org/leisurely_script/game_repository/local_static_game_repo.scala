package org.leisurely_script.repository

import scala.util.{Try, Success, Failure}

import org.leisurely_script.gdl._
import org.leisurely_script.gdl.ImplicitDefs.Views.Game._
import GameFactory.AvailableGames
import GameFactory.AvailableGames._



case object LocalStaticRepository extends GameRepository {
  val availableGames = GameFactory.AvailableGames
  override def submit(game:GameRuleSet):Unit = {
    throw new IllegalOperationException("Error submitting game. The local static repository is read only")
  }
  override def load(gameID:String):Try[GameRuleSet] = {
    Try(GameFactory.load(gameID)) flatten
  }
  def load(gameID:AvailableGames.Value):Try[GameRuleSet] = {
    Try(GameFactory.load(gameID)) flatten
  }
  override def update(gameID:String, game:GameRuleSet):Unit = {
    throw new IllegalOperationException("Error updating game. The local static repository is read only")
  }
  override def remove(gameID:String):Unit = {
    throw new IllegalOperationException("Error removing game. The local static repository is read only")
  }
}

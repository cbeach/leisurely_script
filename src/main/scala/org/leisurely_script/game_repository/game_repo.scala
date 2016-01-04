package org.leisurely_script.repository

import scala.util.{Try, Success, Failure}

import org.leisurely_script.gdl._
import org.leisurely_script.gdl.ImplicitDefs.Views.Game._

trait GameRepository {
  def submit(game:GameRuleSet):Unit
  def load(gameID:String):Try[GameRuleSet]
  def update(gameID:String, game:GameRuleSet):Unit
  def remove(gameID:String):Unit
}

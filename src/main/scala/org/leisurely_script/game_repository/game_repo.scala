package org.leisurelyscript.repository

import scala.util.{Try, Success, Failure}

import org.leisurelyscript.gdl._
import org.leisurelyscript.gdl.ImplicitDefs.Views.Game._

trait GameRepository {
  def submit(game:Game):Unit
  def load(gameID:String):Try[Game]
  def update(gameID:String, game:Game):Unit
  def remove(gameID:String):Unit
}

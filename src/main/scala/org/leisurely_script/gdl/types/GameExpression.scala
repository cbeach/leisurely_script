package org.leisurely_script.gdl.types

import scala.reflect.runtime.universe._
import scala.reflect._
import org.leisurely_script.implementation.Game


trait GameExpression {
  type T
  protected var game:Option[Game] = None
  def setGame(g:Game) = {
    game = Some(g)
  }
  def evaluate:Option[T]
}

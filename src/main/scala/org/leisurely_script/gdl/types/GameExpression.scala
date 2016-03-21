package org.leisurely_script.gdl.types

import scala.reflect.runtime.universe._
import org.leisurely_script.implementation.Game


trait GameExpression[T] {
  protected var game:Option[Game] = None
  val tt:TypeTag[T] = typeTag[T]
  def setGame(g:Game) = {
    game = Some(g)
  }
  def evaluate:Option[T]
}

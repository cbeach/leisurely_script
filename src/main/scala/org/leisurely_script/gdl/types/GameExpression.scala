package org.leisurely_script.gdl.types

import org.leisurely_script.gdl.GameRuleSet
import org.leisurely_script.implementation.Game


trait GameExpression[T] {
  protected var game:Option[Game] = None
  def setGame(g:Game) = {
    game = Some(g)
  }
  def evaluate:Option[T]
  def getChildExpressions:List[GameExpression[Any]] = List()
  def gameCreationHook(gameRuleSet:GameRuleSet):Unit = {}
}

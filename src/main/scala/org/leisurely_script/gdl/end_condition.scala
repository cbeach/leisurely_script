package org.leisurely_script.gdl

import org.leisurely_script.gdl.types.{GameExpression, GameResultExpression}


case class EndCondition(result:GameExpression[SGameResult]) {
  def getChildExpressions:List[GameExpression[Any]] =
    List(result.asInstanceOf[GameExpression[Any]])
  def evaluate: Option[SGameResult] = result.evaluate
}

package org.leisurely_script.gdl

import org.leisurely_script.gdl.types.{GameExpression, GameResultExpression}
import org.leisurely_script.implementation.Game


case class EndCondition(result:GameExpression[SGameResult]) {}

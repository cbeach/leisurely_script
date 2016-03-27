package org.leisurely_script.gdl.types

import org.leisurely_script.gdl._

/**
  * Created by mcsmash on 3/12/16.
  */
case class GameResultExpression(players:PlayerValidator, result:GameResultState.Value)
  extends GameExpression[SGameResult] {
  def this(result: SGameResult) = this(result.players, result.result)
  def evaluate:Option[SGameResult] = Some(SGameResult(players, result))
}

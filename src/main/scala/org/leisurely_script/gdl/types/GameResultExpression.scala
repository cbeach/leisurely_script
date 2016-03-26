package org.leisurely_script.gdl.types

import org.leisurely_script.gdl.ImplicitDefs.Views.TypeConversions._
import org.leisurely_script.gdl._

/**
  * Created by mcsmash on 3/12/16.
  */
case class GameResultExpression(players:Option[PlayerValidator], result:GameResultState.Value)
  extends GameExpression[SGameResult] {
  def this(result: SGameResult) = {
    this(Some(result.players), result.result)
  }
  def evaluate:Option[SGameResult] = {
    players match {
      case Some(p:Set[Player]) => Some(SGameResult(SomePlayers(p), result))
    }
  }
}

package org.leisurely_script.gdl.types

import org.leisurely_script.gdl.ImplicitDefs.Views.TypeConversions._
import org.leisurely_script.gdl.{PlayerValidator, SGameResult, Player, GameResultState}

/**
  * Created by mcsmash on 3/12/16.
  */
case class GameResultExpression(player:Option[PlayerValidator], result:GameResultState.Value)
  extends GameExpression[SGameResult] {
  def this(result: SGameResult) = {
    this(result.player, result.result)
  }
  def evaluate:Option[SGameResult] = {
    player match {
      case Some(p:Player) => Some(SGameResult(p.getPlayers(game.get), result))
    }
  }
}

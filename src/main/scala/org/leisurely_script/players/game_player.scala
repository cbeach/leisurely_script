package org.leiesurelyscript.players


import org.leisurely_script.gdl._
import org.leisurely_script.implementation.Game

trait GamePlayer {
  def playTurn(game:Game):Move
}

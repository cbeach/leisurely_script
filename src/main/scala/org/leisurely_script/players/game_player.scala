package org.leiesurelyscript.players


import org.leisurely_script.gdl._

trait GamePlayer {
  def playTurn(game:Game):Move
}

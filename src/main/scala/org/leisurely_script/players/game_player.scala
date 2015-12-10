package org.leiesurelyscript.players


import org.leisurelyscript.gdl._

trait GamePlayer {
    def playTurn(game:Game):Move
}

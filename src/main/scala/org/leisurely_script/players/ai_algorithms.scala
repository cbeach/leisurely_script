package org.leiesurelyscript.players

import org.leisurely_script.implementation.Game

import scala.util.Random

import org.leisurely_script.gdl.{Move, GameRuleSet}


class BreadthFirstPlayer extends GamePlayer {
  override def playTurn(game:Game):Move = {
    null
  }
}

class RandomPlayer extends GamePlayer {
  val rand = new Random()
  override def playTurn(game:Game):Move = {
    val legalMoves = game.legalMoves(game.players.current)
    legalMoves(rand.nextInt(legalMoves.size))
  }
}

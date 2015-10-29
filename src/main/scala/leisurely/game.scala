package org.leisurelyscript

import scala.util.{Try, Success, Failure}

import GameResult._


class Game(val name:String, val players:Players, val board:Board) {
    def add(playerList:Player*):Game = {
        Game(this, playerList.toList)
    }

    def add(board:Board):Game = {
        Game(this, board)
    }

    def gameValid(): Boolean = {
        true
    }

    def inputs: Map[String, Input] = {
        Map[String, Input]()
    }

    def legalMoves: List[Move] = {
        List[Move]()
    }

    def partialScore: List[Double] = {
        List[Double]()
    }
    def partialScore(player: Player): Double = {
        1.0
    }
    def gameResult(): Option[GameResult] = {
        Some(Win)
    }
    def applyMove(move:Move): Try[Game] = {
        Try(new Game(name, players, board))
    }
    //def applyMove[T1, T2, T3...](input:Input*): Try[Game] 
    //  - Each type parameter applies to a different input
    //history(): List[Game]
    //def applyMove:
}

object Game {
    def apply(name:String = java.util.UUID.randomUUID.toString, 
              players:List[Player] = null, 
              board:Board = null):Game = new Game(name, new Players(players), board)
    def apply(game:Game, players:List[Player]):Game 
        = new Game(game.name, new Players(players), game.board)
    def apply(game:Game, board:Board):Game = new Game(game.name, game.players, board)
}

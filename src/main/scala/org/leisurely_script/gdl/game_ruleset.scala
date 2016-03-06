package org.leisurely_script.gdl

import scala.util.{Try, Success, Failure}

import GameStatus._
import GameResultState._
import MoveAction._

import org.leisurely_script.implementation.Game


case class PlayerListWrapper(val list:List[Player]) {}
case class PieceRuleListWrapper(val list:List[PieceRule]) {}
case class EndConditionListWrapper(val list:List[EndCondition]) {}

class GameRuleSet(val name:String,
                  val players:Players,
                  val board:BoardRuleSet,
                  val pieces:List[PieceRule],
                  val endConditions:List[EndCondition],
                  var playerScoringFunction:Option[(Player, GameResultState.Value, Option[Player])=>Double]=None) {
  playerScoringFunction = Some(playerScoringFunction.getOrElse(
    (player:Player, resultState:GameResultState.Value, winner:Option[Player])=>{
      resultState match {
        case Win =>
          winner match {
            case Some(winningPlayer) if (winningPlayer == player) => 1.0
            case _ => 0.0
          }
        case Lose => 0.0
        case Tie => 1.0
        case Pending => 0.0
      }
    }
  ))
  def this(game:GameRuleSet) = {
    this(game.name, game.players, game.board, game.pieces, game.endConditions)
  }
  def add(board:BoardRuleSet):GameRuleSet = {
    new GameRuleSet(name, players, board, pieces, endConditions)
  }
  def add(players:Players):GameRuleSet = {
    new GameRuleSet(name, players, board, pieces, endConditions)
  }
  def add(players:PlayerListWrapper):GameRuleSet = {
    new GameRuleSet(name, new Players(players.list), board, pieces, endConditions)
  }
  def add(pieces:PieceRuleListWrapper):GameRuleSet = {
    new GameRuleSet(name, players, board, pieces.list, endConditions)
  }
  def add(endConditions:EndConditionListWrapper):GameRuleSet = {
    new GameRuleSet(name, players, board, pieces, endConditions.list)
  }
  def startGame():Game = {
    Try(wellFormed) match {
      case Success(_) => {
        new Game(this)
      }
      case Failure(exception) => {
        throw exception
      }
    }
  }
  private def wellFormed():Unit = {
    if (players.all.size <= 0) {
      throw new IllegalGameException("A game requires one or more players. None found.")
    }

    if (board == null) {
      throw new IllegalGameException("A game requires a board. None found.")
    } else {
      board.wellFormed
    }

    if (pieces.size <= 0) {
      throw new IllegalGameException("A game requires one or more pieces. None found.")
    } else {
      pieces.foreach(_.wellFormed)
    }

    if (endConditions.size <= 0) {
      throw new IllegalGameException("A game requires one or more end conditions. None found.")
    }
  }
  def inputs: Map[String, Input] = {
    Map[String, Input]()
  }
}


object GameRuleSet {
  def apply(name:String = java.util.UUID.randomUUID.toString,
            players:Players = new Players(List[Player]()),
            board:BoardRuleSet = null,
            pieces:List[PieceRule] = List[PieceRule](),
            endConditions:List[EndCondition] = List[EndCondition]()
        ):GameRuleSet = new GameRuleSet(name, players, board, pieces, endConditions)
}

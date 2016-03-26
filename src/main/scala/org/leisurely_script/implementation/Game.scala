package org.leisurely_script.implementation

import scala.util.{Try, Success, Failure}
import scala.collection.mutable

import org.leisurely_script.gdl._
import GameStatus._
import GameResultState._
import MoveAction._

/**
  * Created by mcsmash on 12/21/15.
  */
class Game(rS:GameRuleSet) {
  val ruleSet:GameRuleSet = rS
  val history:mutable.Stack[Game] = mutable.Stack()
  val board = ruleSet.board.getPlayableBoard match {
    case Success(board:Board) => board
    case Failure(ex) => throw ex
  }
  val gameResult:SGameResult = SGameResult(AllPlayers, Pending)
  val status = InProgress

  private def playerScore(player:Player, resultState:GameResultState.Value, winner:Option[Player]) = {
    val func = ruleSet.playerScoringFunction.get
    func(player, resultState, winner)
  }
  private def rankPlayers(resultState:GameResultState.Value, winner:Option[Player]):List[List[Player]] = {
    val playerScores:List[Tuple2[Player, Double]] =
      ruleSet.players.all.zip(ruleSet.players.all.map(playerScore(_, resultState, winner)))
    val playerScoreMap:List[List[Player]] = playerScores.groupBy({
      case (player:Player, score:Double) => score
    }).toList.map({
      case (score:Double, list:List[(Player, Double)]) => (score, list.map(_._1))
    }).sortWith((first:Tuple2[Double, List[Player]], second:Tuple2[Double, List[Player]]) => {
      first._1 > second._1
    }).map({
      case (score:Double, list:List[Player]) => list
    })
    playerScoreMap
  }
  def inputs: Map[String, Input] = {
    ruleSet.inputs
  }
  def legalMoves(player:Player):List[Move] = {
    status match {
      case Finished => List[Move]()
      case InProgress => ruleSet.pieces.flatMap (piece => piece.legalMoves (this, player) )
      case _ => throw new IllegalGameException("This game has not even started yet. Legal moves are not available at this time.")
    }
  }
  def partialScore: List[Double] = {
    //TODO: Implement this
    throw new NotImplementedError("Partial scores are not implemented yet")
  }
  def partialScore(player: Player): Double = {
    //TODO: Implement this
    throw new NotImplementedError("Partial scores are not implemented yet")
  }
  def nonValidatedApplyMove(move:Move):Try[Game] = {
    move.action match {
      case Push => board.push(move.piece, move.node.coord) match {
        case Success(updatedBoard:Board) => {
          Try(this)
        }
        case Failure(ex) => Failure(ex)
      }
      case Pop => board.pop(move.node.coord) match {
        case Success(updatedBoard:Board) => {
          Try(this)
        }
        case Failure(ex) => Failure(ex)
      }
    }
  }
  def applyMove(move:Move): Try[Game] = {
    status match {
      case WaitingToBegin => Failure(new IllegalGameStateException("The game has not started yet."))
      case InProgress =>
        if (isMoveLegal(move)) {
          move.action match {
            case Push => board.push(move.piece, move.node.coord) match {
              case Success(newBoard) => {
                Try(this)
              }
              case Failure(ex) => Failure(ex)
            }
            case Pop => board.pop(move.node.coord) match {
              case Success(newBoard) => {
                Try(this)
              }
              case Failure(ex) => Failure(ex)
            }
          }
        } else {
          Failure(new IllegalMoveException(s"${move} is an illegal move."))
        }
      case Finished => {
        Failure(new IllegalGameStateException("The game is over, no more moves are possible."))
      }
    }
  }
  def isMoveLegal(move:Move):Boolean = {
    for (piece <- ruleSet.pieces) {
      if (piece.isMoveLegal(this, move)) {
        return true
      }
    }
    false
  }
  def isMoveTerminal(move:Move):Boolean = {
    if (!isMoveLegal(move)) {
      throw(new IllegalMoveException())
    }
    val game = applyMove(move).get
    gameResult.result match {
      case Pending => false
      case _ => true
    }
  }
  object all {
    def players = ruleSet.players.all
  }
  object previous {
    def player = ruleSet.players.previous
  }
  object current {
    def player = ruleSet.players.current
  }
  object next {
    def player = ruleSet.players.next
  }
  def piece = ruleSet.pieces
  def player(i:Int) = all.players(i)
}

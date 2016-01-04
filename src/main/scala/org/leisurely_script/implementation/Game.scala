package org.leisurely_script.implementation

import scala.util.{Try, Success, Failure}

import org.leisurely_script.gdl._
import GameStatus._
import GameResultState._
import MoveAction._

/**
  * Created by mcsmash on 12/21/15.
  */
class Game(val ruleSet:GameRuleSet,
           val name:String,
           val players:Players,
           val board:Board,
           val pieces:List[PieceRule],
           val endConditions:List[EndCondition],
           val history:List[Game]=List[Game](),
           var gameResult:Option[GameResult]=None,
           var status:GameStatus.Value=InProgress,
           var playerScoringFunction:Option[(Player, GameResultState.Value, Option[Player])=>Double]=None) {
  gameResult = Some({
    val conditionValues:List[Boolean] = endConditions.map(endCondition => {
      val affectedPlayerSet = endCondition.affectedPlayer.getPlayers(this)
      endCondition.conditionMet(this, affectedPlayerSet.head)
    })

    if (history.isEmpty) {
      GameResult(Pending, None)
    } else {
      history(0).gameResult.get.result match {
        case Pending =>
          if (conditionValues.nonEmpty && conditionValues.reduce(_ || _)) {
            val conditionThatWasMetIndex = conditionValues.indexOf(true)
            val conditionThatWasMet = endConditions(conditionValues.indexOf(true))
            val endResult = conditionThatWasMet.result
            val winner:Option[Player] = endResult match {
              case Win => {
                players.all.find(p => {
                  if (conditionThatWasMet.affectedPlayer.getPlayers(this).size != 1) {
                    throw new IllegalPlayerException("Only one winning player is currently supported")
                  }
                  p == conditionThatWasMet.affectedPlayer.getPlayers(this).head
                })
              }
              case _ => None
            }
            // What if two conditions are met at the same time?
            GameResult(endResult, Some(rankPlayers(endResult, winner)), Some(conditionThatWasMet))
          } else {
            GameResult(Pending, None)
          }
        case _ => history(0).gameResult.get
      }
    }
  })
  status = {
    gameResult.get.result match {
      case Pending => status
      case _ => Finished
    }
  }
  private def playerScore(player:Player, resultState:GameResultState.Value, winner:Option[Player]) = {
    val func = playerScoringFunction.get
    func(player, resultState, winner)
  }
  private def rankPlayers(resultState:GameResultState.Value, winner:Option[Player]):List[List[Player]] = {
    val playerScores:List[Tuple2[Player, Double]] =
      players.all.zip(players.all.map(playerScore(_, resultState, winner)))
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
  def this(gRS: GameRuleSet) {
    this(gRS, gRS.name, gRS.players, gRS.board, gRS.pieces, gRS.endConditions, List[Game](), None,
      InProgress, gRS.playerScoringFunction)
  }
  def inputs: Map[String, Input] = {
    ruleSet.inputs
  }
  def legalMoves(player:Player):List[Move] = {
    status match {
      case Finished => List[Move]()
      case InProgress => pieces.flatMap (piece => piece.legalMoves (this, player) )
      case _ => throw new IllegalGameException("This game has not even started yet. Legal moves are not available at this time.")
    }
  }
  def partialScore: List[Double] = {
    ruleSet.partialScore
  }
  def partialScore(player: Player): Double = {
    ruleSet.partialScore(player)
  }
  def nonValidatedApplyMove(move:Move):Try[Game] = {
    move.action match {
      case Push => board.push(move.piece, move.node.coord) match {
        case Success(newBoard) => {
          Try(new Game(ruleSet, name, players.endTurn, newBoard, pieces, endConditions, this :: this.history,
            gameResult, status, playerScoringFunction))
        }
        case Failure(ex) => Failure(ex)
      }
      case Pop => board.pop(move.node.coord) match {
        case Success(newBoard) => {
          Try(new Game(ruleSet, name, players.endTurn, newBoard, pieces, endConditions, this :: this.history,
            gameResult, status, playerScoringFunction))
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
                Try(new Game(ruleSet, name, players.endTurn, newBoard, pieces, endConditions, this :: this.history,
                  gameResult, status, playerScoringFunction))
              }
              case Failure(ex) => Failure(ex)
            }
            case Pop => board.pop(move.node.coord) match {
              case Success(newBoard) => {
                Try(new Game(ruleSet, name, players.endTurn, newBoard, pieces, endConditions, this :: this.history,
                  gameResult, status, playerScoringFunction))
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
    for (piece <- pieces) {
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
    gameResult.get.result match {
      case Pending => return false
      case _ => return true
    }
  }
}

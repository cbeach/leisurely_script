package org.leisurelyscript

import scala.util.{Try, Success, Failure}

import GameStatus._
import GameResultState._
import MoveAction._


case class PlayerListWrapper(val list:List[Player]) {}
case class PieceRuleListWrapper(val list:List[PieceRule]) {}
case class EndConditionListWrapper(val list:List[EndCondition]) {}

class Game(
    val name:String,
	val players:Players,
	val board:Board,
	val pieces:List[PieceRule],
	val endConditions:List[EndCondition],
	val history:List[Game]=List[Game](),
    var gameResult:Option[GameResult]=None,
    var status:GameStatus.Value=Invalid,
    private var playerScoringFunction:Option[(Player, GameResultState.Value, Option[Player])=>Double]=None) {

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

    gameResult = Some(gameResult.getOrElse({
        val conditionValues:List[Boolean] = endConditions.map(endCondition => {
            val affectedPlayerSet = endCondition.affectedPlayer.getPlayers(this)
            endCondition.conditionMet(this, affectedPlayerSet.head)
        })
         
        if (history.size == 0) {
            GameResult(Pending, None)
        } else {
            history(0).gameResult.get.result match {
                case Pending => 
                    if (conditionValues.size > 0 && conditionValues.reduce(_ || _)) {
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
    }))

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

    def this(game:Game) = {
        this(game.name, game.players, game.board, game.pieces, game.endConditions, game.history, game.gameResult, game.status)
    }

    def add(board:Board):Game = {
        new Game(name, players, board, pieces, endConditions, history, gameResult, status)
    }

    def add(players:Players):Game = {
        new Game(name, players, board, pieces, endConditions, history, gameResult, status)
    }
    def add(players:PlayerListWrapper):Game = { 
        new Game(name, new Players(players.list), board, pieces, endConditions, history, gameResult, status)
    }
    def add(pieces:PieceRuleListWrapper):Game = { 
        new Game(name, players, board, pieces.list, endConditions, history, gameResult, status)
    }
    def add(endConditions:EndConditionListWrapper):Game = { 
        new Game(name, players, board, pieces, endConditions.list, history, gameResult, status)
    }

    def startGame():Game = {
        Try(wellFormed) match {
            case Success(_) => {
                status = InProgress
                this
            }
            case Failure(exception) => {
                status = Invalid
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

    def legalMoves(player:Player):List[Move] = {
        pieces.flatMap(piece => piece.legalMoves(this, player))
    }

    def partialScore: List[Double] = {
        List[Double]()
    }

    def partialScore(player: Player): Double = {
        1.0
    }

    def nonValidatedApplyMove(move:Move):Try[Game] = {
        move.action match {
            case Push => board.push(move.piece, move.node.coord) match {
                case Success(newBoard) => {
                    Try(new Game(name, players.endTurn, newBoard, pieces, endConditions, 
                        this :: history, status=status))
                }
                case Failure(ex) => Failure(ex) 
            }
            case Pop => board.pop(move.node.coord) match {
                case Success(newBoard) => {
                    Try(new Game(name, players.endTurn, newBoard, pieces, endConditions, 
                        this :: history, status=status))
                }
                case Failure(ex) => Failure(ex)
            }
        }
    }

    def applyMove(move:Move): Try[Game] = {
        status match {
            case Invalid => Failure(new IllegalGameStateException("This game is not valid."))
            case WaitingToBegin => Failure(new IllegalGameStateException("The game has not started yet."))
            case InProgress => 
                if (isMoveLegal(move)) {
                    move.action match {
                        case Push => board.push(move.piece, move.node.coord) match {
                            case Success(newBoard) => {
                                Try(new Game(name, players.endTurn, newBoard, pieces, endConditions, 
                                    this :: history, status=status))
                            }
                            case Failure(ex) => Failure(ex) 
                        }
                        case Pop => board.pop(move.node.coord) match {
                            case Success(newBoard) => {
                                Try(new Game(name, players.endTurn, newBoard, pieces, endConditions, 
                                    this :: history, status=status))
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


object Game {
    def apply(name:String = java.util.UUID.randomUUID.toString, 
              players:Players = new Players(List[Player]()), 
              board:Board = null,
              pieces:List[PieceRule] = List[PieceRule](),
              endConditions:List[EndCondition] = List[EndCondition]()
              ):Game = new Game(name, players, board, pieces, endConditions)
}

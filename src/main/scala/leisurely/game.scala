package org.leisurelyscript

import scala.util.{Try, Success, Failure}

import GameStatus._
import GameResultState._
import MoveAction._


class Game(
    val name:String,
	val players:Players,
	val board:Board,
	val pieces:List[Piece],
	val endConditions:List[EndCondition],
	val history:List[Game] = List[Game]()) {

    val gameResult:GameResult = {
        val conditionValues:List[Boolean] = endConditions.map(endCondition => {
            endCondition.condition(this)
        })
         
        if (history.size == 0) {
            GameResult(Pending, rankPlayers) 
        } else {
            history(0).gameResult.result match {
                case Pending => 
                    if (conditionValues.size > 0 && conditionValues.reduce(_ || _)) {
                        val conditionThatWasMetIndex = conditionValues.indexOf(true)
                        val conditionThatWasMet = endConditions(conditionValues.indexOf(true))
                        val endResult = conditionThatWasMet.result
                        // What if two conditions are met at the same time?
                        GameResult(endResult, rankPlayers, conditionThatWasMet)
                    } else {
                        GameResult(Pending, rankPlayers)
                    }
                case _ => history(0).gameResult
            }
        }
    }

    val status:GameStatus.Value = {
        gameResult.result match {
            case Pending => InProgress
            case _ => Finished
        }
    }

    var playerScoringFunction:(Player)=>Double = player => {
        gameResult.result match {
            case Win => {
                if (true) {
                    1.0
                } else {
                    0.0
                }
            }
            case Lose => 0.0
            case Tie => 1.0
            case Pending => 0.0
        }
    }

    private def playerScore(player:Player) = { playerScoringFunction(player) }
    private def rankPlayers() = {List[List[Player]]()}

    def this(game:Game) = {
        this(game.name, game.players, game.board, game.pieces, game.endConditions, game.history)
    }

    def add(board:Board):Game = {
        Game(this, board)
    }

    def add(players:Players):Game = {
        Game(this, players)
    }

    def add[T](list:List[T]):Game = { 
        list match {
            case (first:Player)::(rest:List[Player]) => Game(this, new Players(first::rest))
            case (first:Piece)::(rest:List[Piece]) => Game(this, list)
            case (first:EndCondition)::(rest:List[EndCondition]) => Game(this, list)
            case _ => throw(new IllegalGameAttributeException("Can not add object to game. Invalid type."))
        }
    }

    def gameValid(): Boolean = {
        true
    }

    def inputs: Map[String, Input] = {
        Map[String, Input]()
    }

    def legalMoves:List[Move] = {
        List[Move]()
    }

    def partialScore: List[Double] = {
        List[Double]()
    }

    def partialScore(player: Player): Double = {
        1.0
    }

    //def results(): GameResult = { }

    def nonValidatedApplyMove(move:Move):Try[Game] = {
        move.action match {
            case Push => board.push(move.piece, move.node.coord) match {
                case Success(newBoard) =>
                    Try(new Game(name, players, newBoard, pieces, endConditions, this :: history))
                case Failure(ex) => Failure(ex) 
            }
            case Pop => board.pop(move.node.coord) match {
                case Success(newBoard) =>
                    Try(new Game(name, players, newBoard, pieces, endConditions, this :: history))
                case Failure(ex) => Failure(ex)
            }
        }
    }

    def applyMove(move:Move): Try[Game] = {
        if (isMoveLegal(move)) {
            move.action match {
                case Push => board.push(move.piece, move.node.coord) match {
                    case Success(newBoard) =>
                        Try(new Game(name, players, newBoard, pieces, endConditions, this :: history))
                    case Failure(ex) => Failure(ex) 
                }
                case Pop => board.pop(move.node.coord) match {
                    case Success(newBoard) =>
                        Try(new Game(name, players, newBoard, pieces, endConditions, this :: history))
                    case Failure(ex) => Failure(ex)
                }
            }
        } else {
            Failure(new IllegalMoveException())
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
        gameResult.result match {
            case Pending => return false
            case _ => return true
        }
    }
}


object Game {
    def apply(name:String = java.util.UUID.randomUUID.toString, 
              players:Players = new Players(List[Player]()), 
              board:Board = null,
              pieces:List[Piece] = List[Piece](),
              endConditions:List[EndCondition] = List[EndCondition]()
              ):Game = new Game(name, players, board, pieces, endConditions)
    private def apply(game:Game, board:Board):Game 
        = new Game(game.name, game.players, board, game.pieces, game.endConditions)
    private def apply[T](game:Game, list:List[T]):Game = {
        list match {
            case (pl:Player)::(pls:List[Player]) => 
                new Game(game.name, new Players(pl::pls), game.board, game.pieces, game.endConditions)
            case (pi:Piece)::(pis:List[Piece]) => 
                new Game(game.name, game.players, game.board, pi:: pis, game.endConditions)
            case (eC:EndCondition)::(eCs:List[EndCondition]) => 
                new Game(game.name, game.players, game.board, game.pieces, eC::eCs)
            case _ => throw(new IllegalGameAttributeException("How did you get in here. You shouldn't be in here..."))
        }
    }
    private def apply(game:Game, players:Players) = {
        new Game(game.name, players, game.board, game.pieces, game.endConditions)
    }
}

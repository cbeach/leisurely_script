package org.leisurelyscript

import scala.util.{Try, Success, Failure}

import GameResult._
import MoveAction._


class Game(val name:String,
	val players:Players,
	val board:Board,
	val pieces:List[Piece],
	val endConditions:List[EndCondition],
	val history:List[Game] = List[Game]()) {

    private val privateUUID:String = java.util.UUID.randomUUID.toString
    def this(game:Game) = {
        this(game.name, game.players, game.board, game.pieces, game.endConditions, game.history)
    }

    def add(board:Board):Game = {
        Game(this, board)
    }

    def add[T](list:List[T]):Game = { 
        list match {
            case (first:Player)::rest => Game(this, list)
            case (first:Piece)::rest => Game(this, list)
            case (first:EndCondition)::rest => Game(this, list)
            case _ => throw(new IllegalGameAttributeException("Can not add object to game. Invalid type."))
        }
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

    def nonValidatedApplyMove(move:Move):Try[Game] = {
        move.action match {
            case Push => board.push(move.piece, move.node.coord) match {
                case Success(newBoard) => {
                    Try(new Game(name, players, newBoard, pieces, endConditions, history))
                } 
                case Failure(ex) => Failure(ex) 
            }
            case Pop => board.pop(move.node.coord) match {
                case Success(newBoard) => {
                    Try(new Game(name, players, newBoard, pieces, endConditions, history))
                }
                case Failure(ex) => Failure(ex)
            }
        }
    }

    def applyMove(move:Move): Try[Game] = {
        if (isMoveLegal(move)) {
            move.action match {
                case Push => board.push(move.piece, move.node.coord) match {
                    case Success(newBoard) => {
                        Try(new Game(name, players, newBoard, pieces, endConditions, history))
                    } 
                    case Failure(ex) => Failure(ex) 
                }
                case Pop => board.pop(move.node.coord) match {
                    case Success(newBoard) => {
                        Try(new Game(name, players, newBoard, pieces, endConditions, history))
                    }
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
}

object Game {
    def apply(name:String = java.util.UUID.randomUUID.toString, 
              players:List[Player] = null, 
              board:Board = null,
              pieces:List[Piece] = null,
              endConditions:List[EndCondition] = null
              ):Game = new Game(name, new Players(players), board, pieces, endConditions)
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
}

package org.leisurelyscript.test.util

import scala.util.{Try, Success, Failure}

import org.scalatest.Tag

import org.leisurelyscript.gdl._
import MoveAction._
import org.leisurelyscript.repository.LocalStaticRepository


object LongRunningTests extends Tag("LongRunngingTests")

object GraphUtilities {
    def sortEdges(game:Game):Iterable[BoardEdge] = {
        game.board.graph.edges.sortWith((e1, e2) => {
            if (e1.nodes._1.coord.x == e2.nodes._1.coord.x) {
                e1.nodes._2.coord.x < e2.nodes._2.coord.x
            } else {
                e1.nodes._1.coord.x < e2.nodes._1.coord.x
            }
        })
    }
}

package GameUtilities {
    object TicTacToeUtilities {
        def boardToString(board:Board):String = {
            val strList = for (i <- 0 until 3; j <- 0 until 3) yield {
                val equipment = board.graph.nodes(Coordinate(i, j)).equipment
                if (equipment.size == 0) {
                    "-"
                } else {
                    equipment(0) match {
                        case piece:PhysicalPiece => {
                            if (piece.owner.getPlayers.size != 1) {
                                throw new IllegalPlayerException("Only one player can own a piece in TicTacToe")
                            } else {
                                piece.owner.getPlayers.head.name
                            }
                        }
                    }
                }
            }
            s"\n ${strList.slice(0, 3).mkString("")} \n ${strList.slice(3, 6).mkString("")} \n ${strList.slice(6, 9).mkString("")}"
        }
        def boardToInt(board:Board):Int = {
            val intList = for (i <- 0 until 3; j <- 0 until 3) yield {
                val equipment = board.graph.nodes(Coordinate(i, j)).equipment
                if (equipment.size == 0) {
                    0
                } else {
                    equipment(0) match {
                        case piece:PhysicalPiece => {
                            if (piece.owner.getPlayers.size != 1) {
                                throw new IllegalPlayerException("Only one player can own a piece in TicTacToe")
                            } else {
                                piece.owner.getPlayers.head.name match {
                                    case "X" => 1
                                    case "O" => 2
                                    case wat => {
                                        println(s"Who is ${wat}. This is supposed to be TicTacToe.")
                                        throw new IllegalPlayerException(s"Who is ${wat}. This is supposed to be TicTacToe.")
                                    }
                                }
                            }
                        }
                    }
                }
            }
            var returnInt = 0
            for (i <- 8 to 0 by -1) {
                returnInt += intList(i) * Math.pow(10, i).toInt
            }
            returnInt
        }
        def movesFromTiedGame(game:Option[Game]=None):List[Game] = {
            val move0:Game = {
                game getOrElse { 
                    LocalStaticRepository.load("TicTacToe") match {
                        case Success(tTT:Game) => tTT.startGame()
                        case Failure(ex) => throw ex
                    }
                }
            }.startGame()
            val move1 = move0.applyMove(Move(move0.pieces(0).getPhysicalPiece(move0.players.current), move0.players.current, Push, move0.board.graph.nodes(Coordinate(1, 1)))).get
            val move2 = move1.applyMove(Move(move1.pieces(0).getPhysicalPiece(move1.players.current), move1.players.current, Push, move1.board.graph.nodes(Coordinate(0, 0)))).get
            val move3 = move2.applyMove(Move(move2.pieces(0).getPhysicalPiece(move2.players.current), move2.players.current, Push, move2.board.graph.nodes(Coordinate(0, 1)))).get
            val move4 = move3.applyMove(Move(move3.pieces(0).getPhysicalPiece(move3.players.current), move3.players.current, Push, move3.board.graph.nodes(Coordinate(2, 1)))).get
            val move5 = move4.applyMove(Move(move4.pieces(0).getPhysicalPiece(move4.players.current), move4.players.current, Push, move4.board.graph.nodes(Coordinate(1, 0)))).get
            val move6 = move5.applyMove(Move(move5.pieces(0).getPhysicalPiece(move5.players.current), move5.players.current, Push, move5.board.graph.nodes(Coordinate(1, 2)))).get
            val move7 = move6.applyMove(Move(move6.pieces(0).getPhysicalPiece(move6.players.current), move6.players.current, Push, move6.board.graph.nodes(Coordinate(0, 2)))).get
            val move8 = move7.applyMove(Move(move7.pieces(0).getPhysicalPiece(move7.players.current), move7.players.current, Push, move7.board.graph.nodes(Coordinate(2, 0)))).get
            val move9 = move8.applyMove(Move(move8.pieces(0).getPhysicalPiece(move8.players.current), move8.players.current, Push, move8.board.graph.nodes(Coordinate(2, 2)))).get
            
            List[Game](move0, move1, move2, move3, move4, move5, move6, move7, move8, move9)
        }
        def movesFromTiedGame(game:Game):List[Game] = {
            movesFromTiedGame(Some(game))
        }
        def movesFromFastestXWin(game:Option[Game]=None):List[Game] = {
            val move0:Game = {
                game getOrElse { 
                    LocalStaticRepository.load("TicTacToe") match {
                        case Success(tTT:Game) => tTT.startGame()
                        case Failure(ex) => throw ex
                    }
                }
            }.startGame()
            val move1 = move0.applyMove(Move(move0.pieces(0).getPhysicalPiece(move0.players.current), move0.players.current, Push, move0.board.graph.nodes(Coordinate(0, 0)))).get
            val move2 = move1.applyMove(Move(move1.pieces(0).getPhysicalPiece(move1.players.current), move1.players.current, Push, move1.board.graph.nodes(Coordinate(1, 0)))).get
            val move3 = move2.applyMove(Move(move2.pieces(0).getPhysicalPiece(move2.players.current), move2.players.current, Push, move2.board.graph.nodes(Coordinate(0, 1)))).get
            val move4 = move3.applyMove(Move(move3.pieces(0).getPhysicalPiece(move3.players.current), move3.players.current, Push, move3.board.graph.nodes(Coordinate(1, 1)))).get
            val move5 = move4.applyMove(Move(move4.pieces(0).getPhysicalPiece(move4.players.current), move4.players.current, Push, move4.board.graph.nodes(Coordinate(0, 2)))).get
            List[Game](move0, move1, move2, move3, move4, move5)
        }
        def movesFromFastestXWin(game:Game):List[Game] = {
            movesFromFastestXWin(Some(game))
        }
    }
}

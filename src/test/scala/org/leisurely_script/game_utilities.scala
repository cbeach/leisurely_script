package org.leisurely_script.test.util

import scala.util.{Try, Success, Failure}

import org.scalatest.Tag

import org.leisurely_script.gdl._
import MoveAction._
import org.leisurely_script.repository.LocalStaticRepository


object LongRunningTests extends Tag("LongRunningTests")

object GraphUtilities {
  def sortEdges(game:GameRuleSet):Iterable[BoardEdge] = {
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

  import org.leisurely_script.implementation.{Board, Game}

  object TicTacToeUtilities {
    def boardToString(board:BoardRuleSet):String = {
      val strList = for (i <- 0 until 3; j <- 0 until 3) yield {
        val equipment = board.graph.nodesByCoord(Coordinate(i, j)).equipment
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
    def boardToString(board:Board):String = {
      "This doesn't make much sense at the moment"
    }
    def boardToInt(board:BoardRuleSet):Int = {
      val intList = for (i <- 0 until 3; j <- 0 until 3) yield {
        val equipment = board.graph.nodesByCoord(Coordinate(i, j)).equipment
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
    def movesFromTiedGame(game:Option[GameRuleSet]=None):List[Game] = {
      val move0:Game = game.getOrElse({
          LocalStaticRepository.load("TicTacToe") match {
            case Success(tTT:GameRuleSet) => tTT
            case Failure(ex) => throw ex
          }
        }).startGame()
      val move1 = move0.applyMove(Move(move0.piece(0).getPhysicalPiece(move0.current.player), move0.current.player, Push, move0.board.boardRuleSet.graph.nodesByCoord(Coordinate(1, 1)))).get
      val move2 = move1.applyMove(Move(move1.piece(0).getPhysicalPiece(move1.current.player), move1.current.player, Push, move1.board.boardRuleSet.graph.nodesByCoord(Coordinate(0, 0)))).get
      val move3 = move2.applyMove(Move(move2.piece(0).getPhysicalPiece(move2.current.player), move2.current.player, Push, move2.board.boardRuleSet.graph.nodesByCoord(Coordinate(0, 1)))).get
      val move4 = move3.applyMove(Move(move3.piece(0).getPhysicalPiece(move3.current.player), move3.current.player, Push, move3.board.boardRuleSet.graph.nodesByCoord(Coordinate(2, 1)))).get
      val move5 = move4.applyMove(Move(move4.piece(0).getPhysicalPiece(move4.current.player), move4.current.player, Push, move4.board.boardRuleSet.graph.nodesByCoord(Coordinate(1, 0)))).get
      val move6 = move5.applyMove(Move(move5.piece(0).getPhysicalPiece(move5.current.player), move5.current.player, Push, move5.board.boardRuleSet.graph.nodesByCoord(Coordinate(1, 2)))).get
      val move7 = move6.applyMove(Move(move6.piece(0).getPhysicalPiece(move6.current.player), move6.current.player, Push, move6.board.boardRuleSet.graph.nodesByCoord(Coordinate(0, 2)))).get
      val move8 = move7.applyMove(Move(move7.piece(0).getPhysicalPiece(move7.current.player), move7.current.player, Push, move7.board.boardRuleSet.graph.nodesByCoord(Coordinate(2, 0)))).get
      val move9 = move8.applyMove(Move(move8.piece(0).getPhysicalPiece(move8.current.player), move8.current.player, Push, move8.board.boardRuleSet.graph.nodesByCoord(Coordinate(2, 2)))).get

      List[Game](move0, move1, move2, move3, move4, move5, move6, move7, move8, move9)
    }
    def movesFromTiedGame(game:GameRuleSet):List[Game] = {
      movesFromTiedGame(Some(game))
    }
    def movesFromFastestXWin(game:Option[GameRuleSet]=None):List[Game] = {
      val move0:Game = game.getOrElse({
          LocalStaticRepository.load("TicTacToe") match {
            case Success(tTT:GameRuleSet) => tTT
            case Failure(ex) => throw ex
          }
        }).startGame()
      val move1 = move0.applyMove(Move(move0.piece(0).getPhysicalPiece(move0.current.player), move0.current.player, Push, move0.board.boardRuleSet.graph.nodesByCoord(Coordinate(0, 0)))).get
      val move2 = move1.applyMove(Move(move1.piece(0).getPhysicalPiece(move1.current.player), move1.current.player, Push, move1.board.boardRuleSet.graph.nodesByCoord(Coordinate(1, 0)))).get
      val move3 = move2.applyMove(Move(move2.piece(0).getPhysicalPiece(move2.current.player), move2.current.player, Push, move2.board.boardRuleSet.graph.nodesByCoord(Coordinate(0, 1)))).get
      val move4 = move3.applyMove(Move(move3.piece(0).getPhysicalPiece(move3.current.player), move3.current.player, Push, move3.board.boardRuleSet.graph.nodesByCoord(Coordinate(1, 1)))).get
      val move5 = move4.applyMove(Move(move4.piece(0).getPhysicalPiece(move4.current.player), move4.current.player, Push, move4.board.boardRuleSet.graph.nodesByCoord(Coordinate(0, 2)))).get
      List[Game](move0, move1, move2, move3, move4, move5)
    }
    def movesFromFastestXWin(game:GameRuleSet):List[Game] = {
      movesFromFastestXWin(Some(game))
    }
  }
}
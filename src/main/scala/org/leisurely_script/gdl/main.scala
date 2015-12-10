package org.leisurelyscript.gdl

import scala.util.{Try, Success, Failure}
import scala.collection.mutable.Queue

import org.leisurelyscript.gdl._
import GameStatus._
import org.leisurelyscript.repository.LocalStaticRepository
import org.leisurelyscript.repository.GameFactory.AvailableGames._


object Main {
    def main(args:Array[String]) {
        val numberOfPossibleGames = 255168
        var entryQueue = Queue[Game](LocalStaticRepository.load(TicTacToe).get.startGame())
        var exitQueue = Queue[Game]()
        var counter = 0
        var lastDiff = 0
        var currentDiff = 0
        var currentSum = 0
        while(entryQueue.nonEmpty && counter < 100000) {
            //currentSum = entryQueue.size + exitQueue.size
            //currentDiff = entryQueue.size - exitQueue.size
            //println(s"${entryQueue.size} -> ${exitQueue.size} \n\tsum: ${currentSum}\n\tdelta: ${lastDiff - currentDiff}")
            //lastDiff = currentDiff
            counter += 1
            val head = entryQueue.dequeue()
            val legalMoves = head.legalMoves(head.players.current)
            if (head.status != Finished) {
                legalMoves.foreach(move => {
                    entryQueue += {
                        head.applyMove(move) match {
                            case Success(nS) => nS
                            case Failure(_) => {
                                //println(s"An illegal move was provided by Game.legalMoves. Move: ${move}\n Current player: ${head.players.current}\n Board: ${boardToString(head.board)}.")
                                throw new IllegalPlayerException("An illegal move was provided by Game.legalMoves.")
                            }
                        }
                    }
                })
            } else {
                exitQueue += head
            }
        }
        //println(s"exitQueue.size: ${exitQueue.size}")
    }
}

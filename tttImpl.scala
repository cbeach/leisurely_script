package org.beachc.leisurely

import org.beachc.leisurely.metaprogrammers.GameStateGenerator
import scala.meta._

import org.beachc.leisurely.implicits._
import ast._
import runTime.GameState

//@GameStateGenerator
object Tic_Tac_Toe extends GameRuleSet("TicTacToe") {
  val players = List(
    Player("X"),
    Player("Y")
  )
  val playStyle = Antagonistic
  val turns = Sequential
  // Add the "Layer" abstraction on top of the Graph
  val graph = Graph(
    List(
      PointNode((0, 0)), PointNode((0, 1)), PointNode((0, 2)), 
      PointNode((1, 0)), PointNode((1, 1)), PointNode((1, 2)), 
      PointNode((2, 0)), PointNode((2, 1)), PointNode((2, 2))
    ),
    List(
      BidirectionalEdge(PointNode((0, 0)), PointNode((0, 1))),
      BidirectionalEdge(PointNode((0, 0)), PointNode((1, 0))),
      BidirectionalEdge(PointNode((0, 0)), PointNode((1, 1))),

      BidirectionalEdge(PointNode((0, 1)), PointNode((0, 0))),
      BidirectionalEdge(PointNode((0, 1)), PointNode((0, 2))),
      BidirectionalEdge(PointNode((0, 1)), PointNode((1, 0))),
      BidirectionalEdge(PointNode((0, 1)), PointNode((1, 1))),
      BidirectionalEdge(PointNode((0, 1)), PointNode((1, 2))),

      BidirectionalEdge(PointNode((0, 2)), PointNode((0, 1))),
      BidirectionalEdge(PointNode((0, 2)), PointNode((1, 1))),
      BidirectionalEdge(PointNode((0, 2)), PointNode((1, 2))),

      BidirectionalEdge(PointNode((1, 0)), PointNode((0, 0))),
      BidirectionalEdge(PointNode((1, 0)), PointNode((0, 1))),
      BidirectionalEdge(PointNode((1, 0)), PointNode((1, 1))),
      BidirectionalEdge(PointNode((1, 0)), PointNode((2, 0))),
      BidirectionalEdge(PointNode((1, 0)), PointNode((2, 1))),

      BidirectionalEdge(PointNode((1, 1)), PointNode((0, 0))),
      BidirectionalEdge(PointNode((1, 1)), PointNode((0, 1))),
      BidirectionalEdge(PointNode((1, 1)), PointNode((0, 2))),
      BidirectionalEdge(PointNode((1, 1)), PointNode((1, 0))),
      BidirectionalEdge(PointNode((1, 1)), PointNode((1, 2))),
      BidirectionalEdge(PointNode((1, 1)), PointNode((2, 0))),
      BidirectionalEdge(PointNode((1, 1)), PointNode((2, 1))),
      BidirectionalEdge(PointNode((1, 1)), PointNode((2, 2))),

      BidirectionalEdge(PointNode((1, 2)), PointNode((0, 1))),
      BidirectionalEdge(PointNode((1, 2)), PointNode((0, 2))),
      BidirectionalEdge(PointNode((1, 2)), PointNode((1, 1))),
      BidirectionalEdge(PointNode((1, 2)), PointNode((2, 1))),
      BidirectionalEdge(PointNode((1, 2)), PointNode((2, 2))),

      BidirectionalEdge(PointNode((2, 0)), PointNode((1, 0))),
      BidirectionalEdge(PointNode((2, 0)), PointNode((1, 1))),
      BidirectionalEdge(PointNode((2, 0)), PointNode((2, 1))),

      BidirectionalEdge(PointNode((2, 1)), PointNode((1, 0))),
      BidirectionalEdge(PointNode((2, 1)), PointNode((1, 1))),
      BidirectionalEdge(PointNode((2, 1)), PointNode((1, 2))),
      BidirectionalEdge(PointNode((2, 1)), PointNode((2, 0))),
      BidirectionalEdge(PointNode((2, 1)), PointNode((2, 2))),

      BidirectionalEdge(PointNode((2, 2)), PointNode((1, 1))),
      BidirectionalEdge(PointNode((2, 2)), PointNode((1, 2))),
      BidirectionalEdge(PointNode((2, 2)), PointNode((2, 1)))
    )
  )
  val pieces = List(
    Piece("token", AnyPlayer)
  )
  val legalMoves: List[LegalMove] = List(
    LegalMove(
      AnyPlayer, 
      "token",
      (in: Input, player: Player, gs: GameState) => in match {

        case ButtonInput(coord: Discrete2DCoordinate) => (gs.graph((coord.x, coord.y)).isEmpty && gs.players(gs.currentPlayer) == player)
        case _ => false
      },
      // I really don't like this signature, needs to be more general
      Push
    )
  )
  val endConditions = List(
    EndCondition(
      CurrentPlayer,
      (gs: GameState) => if (gs.threeInARow(PreviousPlayer, "token")) {
        Win
      } else {
        Pending
      }
    ),
    EndCondition(
      CurrentPlayer,
      (gS: GameState) => {
        if (gS.boardFull() && !gS.threeInARow(PreviousPlayer, "token")) {
          Draw
        } else {
          Pending
        }
      }
    )
  )
  val inputs = List(
      ButtonInput(Discrete2DCoordinate(0, 0)), 
      ButtonInput(Discrete2DCoordinate(1, 0)), 
      ButtonInput(Discrete2DCoordinate(2, 0)), 
      ButtonInput(Discrete2DCoordinate(0, 1)), 
      ButtonInput(Discrete2DCoordinate(1, 1)), 
      ButtonInput(Discrete2DCoordinate(2, 1)), 
      ButtonInput(Discrete2DCoordinate(0, 2)), 
      ButtonInput(Discrete2DCoordinate(1, 2)), 
      ButtonInput(Discrete2DCoordinate(2, 2))
  )
}

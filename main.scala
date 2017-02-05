package beachc

import scala.language.implicitConversions
import runTime._
import Conversions._

// The abstract class Player is always present. Sub-classes/objects are auto-generated
object Player_X extends Player("X")
object Player_Y extends Player("Y")

// The abstract class Piece is always present. Sub-classes/objects are auto-generated
case class Piece_token(owner: Player) extends Piece(owner)

object TicTacToe extends GameState("TicTacToe") {
  val players: List[Player] = List(Player_X, Player_Y)
  var currentPlayer: Int = 0
  // A callback for each legal move
  val legalMoves: List[(DiscreteCoord2D, List[Player], GameState) => Boolean] = List(
    // The argument type is specified by the player type in the LegalMove definition
    (coord: DiscreteCoord2D, players: List[Player], gs: GameState) => {
      // Check condition
      if (gs.graph((coord.x, coord.y)).isEmpty) {
        // Perform specified action
        gs.graph((coord.x, coord.y)).push(Piece_token(players(currentPlayer)))
        // Signal that the move was successful by returning true
        true
      } else {
        // Signal that the move was not successful by returning false
        false
      }
    }
  )
  var currentResultState = Pending

  val endConditions: List[EndCondition] = List(
    EndCondition(CurrentPlayer, (gs: GameState) => gs.ThreeInARow(PreviousPlayer, Piece_token(CurrentPlayer)), (Win, Pending)),
    EndCondition(AllPlayers, (gS: GameState) => gS.boardFull && !gS.ThreeInARow(PreviousPlayer, Piece_token(CurrentPlayer)), (Draw, Pending))
  )

  // Auto-generated from the Graph, Input and Piece definitions
  val token_point_0_0: Point = Point((0, 0))
  val token_point_0_1: Point = Point((0, 1))
  val token_point_0_2: Point = Point((0, 2))
  val token_point_1_0: Point = Point((1, 0))
  val token_point_1_1: Point = Point((1, 1))
  val token_point_1_2: Point = Point((1, 2))
  val token_point_2_0: Point = Point((2, 0))
  val token_point_2_1: Point = Point((2, 1))
  val token_point_2_2: Point = Point((2, 2))

  val graph: Map[(Int, Int), Point] = Map(
    ((0, 0), token_point_0_0), ((0, 1), token_point_0_1), ((0, 2), token_point_0_2),
    ((1, 0), token_point_1_0), ((1, 1), token_point_1_1), ((1, 2), token_point_1_2),
    ((2, 0), token_point_2_0), ((2, 1), token_point_2_1), ((2, 2), token_point_2_2)
  )

  // The inputs that bridge the gap between the state machine and the UI
  val inputs: List[List[ButtonInput[DiscreteCoord2D]]] = List(
    List(ButtonInput(this, DiscreteCoord2D(0, 0), legalMoves), 
         ButtonInput(this, DiscreteCoord2D(1, 0), legalMoves), 
         ButtonInput(this, DiscreteCoord2D(2, 0), legalMoves)),
    List(ButtonInput(this, DiscreteCoord2D(0, 1), legalMoves), 
         ButtonInput(this, DiscreteCoord2D(1, 1), legalMoves), 
         ButtonInput(this, DiscreteCoord2D(2, 1), legalMoves)),
    List(ButtonInput(this, DiscreteCoord2D(0, 2), legalMoves), 
         ButtonInput(this, DiscreteCoord2D(1, 2), legalMoves), 
         ButtonInput(this, DiscreteCoord2D(2, 2), legalMoves))
  )

  def isCurrentPlayer(player: Player): Boolean = player == players(currentPlayer)
  def nextTurn() = {
    currentPlayer += 1 
    currentPlayer = if (currentPlayer == players.length) { 
      0 
    } else { 
      currentPlayer
    }
  }
}

object Main {
  def main(args: Array[String]): Unit = {

  }
}

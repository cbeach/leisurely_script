package beachc

package object runTime {
  type Name = String
  type Layer = Int

  object Conversions {
    implicit def TwoTuple2DiscreteCoord2D(rawCoord: (Int, Int)): DiscreteCoord2D = DiscreteCoord2D(rawCoord._1, rawCoord._2)
  }

  abstract class GameState(val name:Name) { 
    val graph: Map[(Int, Int), Point]
    def place(owner: Player, piece: Piece, node: Node): GameState = this
    def push(owner: Player, piece: Piece, node: Node): GameState = this
    def pop(owner: Player, piece: Piece, node: Node): GameState = this
    def TwoInARow(owner: Player, piece: Piece): Boolean = true
    def ThreeInARow(owner: Player, piece: Piece): Boolean = true
    def FourInARow(owner: Player, piece: Piece): Boolean = true
    def boardFull: Boolean = true
    def boardEmpty: Boolean = true
  }

  // Graph code
  case class DiscreteCoord2D(x: Int, y: Int) {}
  abstract class Node(label: Any) {
    var pieces: List[Piece] = List()
    def push(piece: Piece): Unit = {
      
    }
  }
  case class Point(label: DiscreteCoord2D) extends Node(label) {
    def isEmpty(): Boolean = {
      pieces isEmpty
    }
  }

  // Players
  abstract class Player(val name: String) {}
  object CurrentPlayer extends Player("Current")
  object NextPlayer extends Player("Next")
  object PreviousPlayer extends Player("Last")
  object AllPlayers extends Player("All")
  object Player_Nil extends Player("")

  // Pieces
  abstract class Piece(owner: Player)

  // Inputs
  abstract class Input(gs: GameState) {}
  case class ButtonInput[L](gs: GameState, label: L, callbacks: List[Function3[L, List[Player], GameState, Boolean]]) extends Input(gs) {
    def trigger(players: List[Player]): Unit = {
      callbacks.foreach(_(label, players, TicTacToe))
    }
  }

  trait GameOutcome
  case object Win extends GameOutcome {}
  case object Lose extends GameOutcome {}
  case object Draw extends GameOutcome {}
  case object Pending extends GameOutcome {}
  case class EndCondition(player: Player, gs: (GameState) => Boolean, outcome: (GameOutcome, GameOutcome)) {}
}

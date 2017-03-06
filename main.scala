package beachc

import scala.collection.mutable.Stack
import scala.language.implicitConversions
import runTime._
import Conversions._
import ast.{
  Player,
  PreviousPlayer,
  CurrentPlayer,
  NextPlayer,
  AnyPlayer,
  AllPlayers,
  NullPlayer
}

// The abstract class Player is always present. Sub-classes/objects are auto-generated
object Player_X extends Player("X")
object Player_O extends Player("O")

class Piece_token(owner: Player) extends Piece(owner) 
object Piece_token {
  def apply(owner: Player): Piece_token = owner match {
    case Player_X => Token_X
    case Player_O => Token_O
  }
  def apply(gs: GameState, owner: Player): Piece_token = owner match {
    case Player_X => Token_X
    case Player_O => Token_O
    case PreviousPlayer => this(gs.getPreviousPlayer)
    case CurrentPlayer => this(gs.getCurrentPlayer)
    case NextPlayer => this(gs.getNextPlayer)
  }
}

case object Token_X extends Piece_token(Player_X)
case object Token_O extends Piece_token(Player_O)

case class TicTacToe() extends GameState("TicTacToe") {
  val players: List[Player] = List(Player_X, Player_O)
  val playersState: Map[Player, PlayerState] = Map(players.map((p) => (p, PlayerState(p))):_*)
  var currentPlayer: Int = 0
  val pieces: Map[String, Map[Player, Piece]] = Map(
    ("token", Map(
      (Player_X, Token_X)
    ))
  )
  // A callback for each legal move
  val legalMoves: List[(Discrete2DCoordinate, Player, GameState) => Boolean] = List(
    // The argument type is specified by the player type in the LegalMove definition
    (coord: Discrete2DCoordinate, player: Player, gs: GameState) => {
      // Check condition
      if (gs.graph((coord.x, coord.y)).isEmpty && gs.isCurrentPlayer(player)) {
        // Perform specified action
        player match {
          case Player_X => gs.graph((coord.x, coord.y)).push(Token_X)
          case Player_O => gs.graph((coord.x, coord.y)).push(Token_O)
        }
        gs.nextTurn
        // Signal that the move was successful by returning true
        true
      } else {
        // Signal that the move was not successful by returning false
        false
      }
    }
  )

  val endConditions: List[EndCondition] = List(
    EndCondition(CurrentPlayer, (gs: GameState) => gs.threeInARow(CurrentPlayer, Piece_token(gs, CurrentPlayer)), Win),
    EndCondition(AnyPlayer, (gs: GameState) => gs.boardFull() && !gs.threeInARow(CurrentPlayer, Piece_token(gs, CurrentPlayer)), Draw)
  )

  // Auto-generated from the Graph, Input and Piece definitions
  val token_point_0_0: PointNode = PointNode((0, 0))
  val token_point_0_1: PointNode = PointNode((0, 1))
  val token_point_0_2: PointNode = PointNode((0, 2))
  val token_point_1_0: PointNode = PointNode((1, 0))
  val token_point_1_1: PointNode = PointNode((1, 1))
  val token_point_1_2: PointNode = PointNode((1, 2))
  val token_point_2_0: PointNode = PointNode((2, 0))
  val token_point_2_1: PointNode = PointNode((2, 1))
  val token_point_2_2: PointNode = PointNode((2, 2))

  val graph: Map[(Int, Int), PointNode] = Map(
    ((0, 0), token_point_0_0), ((0, 1), token_point_0_1), ((0, 2), token_point_0_2),
    ((1, 0), token_point_1_0), ((1, 1), token_point_1_1), ((1, 2), token_point_1_2),
    ((2, 0), token_point_2_0), ((2, 1), token_point_2_1), ((2, 2), token_point_2_2)
  )

  val nodes = List(
    token_point_0_0,
    token_point_0_1,
    token_point_0_2,
    token_point_1_0,
    token_point_1_1,
    token_point_1_2,
    token_point_2_0,
    token_point_2_1,
    token_point_2_2)

  // The inputs that bridge the gap between the state machine and the UI
  val inputs: List[ButtonInput[Discrete2DCoordinate]] = List(
    ButtonInput(this, Discrete2DCoordinate(0, 0), legalMoves), 
    ButtonInput(this, Discrete2DCoordinate(0, 1), legalMoves), 
    ButtonInput(this, Discrete2DCoordinate(0, 2), legalMoves),
    ButtonInput(this, Discrete2DCoordinate(1, 0), legalMoves), 
    ButtonInput(this, Discrete2DCoordinate(1, 1), legalMoves), 
    ButtonInput(this, Discrete2DCoordinate(1, 2), legalMoves),
    ButtonInput(this, Discrete2DCoordinate(2, 0), legalMoves), 
    ButtonInput(this, Discrete2DCoordinate(2, 1), legalMoves), 
    ButtonInput(this, Discrete2DCoordinate(2, 2), legalMoves))

  override def threeInARow(owner: Player, piece: Piece): Boolean = {
    // Dynamically generated code
    val getPiece = (player: Player) => {
      player match {
        case Player_X => Token_X
        case Player_O => Token_O
      }
    }

    // Only valid if there's one piece
    val targetPiece = owner match {
      case Player_X => Token_X
      case Player_O => Token_O
      case PreviousPlayer => getPiece(this.getPreviousPlayer)
      case CurrentPlayer => getPiece(this.getCurrentPlayer)
      case NextPlayer => getPiece(this.getNextPlayer)
    }
    
    // I can do this because there is no stacking
    val _0_0 = token_point_0_0.pieces.headOption.getOrElse(NullPiece)
    val _0_1 = token_point_0_1.pieces.headOption.getOrElse(NullPiece)
    val _0_2 = token_point_0_2.pieces.headOption.getOrElse(NullPiece)
    val _1_0 = token_point_1_0.pieces.headOption.getOrElse(NullPiece)
    val _1_1 = token_point_1_1.pieces.headOption.getOrElse(NullPiece)
    val _1_2 = token_point_1_2.pieces.headOption.getOrElse(NullPiece)
    val _2_0 = token_point_2_0.pieces.headOption.getOrElse(NullPiece)
    val _2_1 = token_point_2_1.pieces.headOption.getOrElse(NullPiece)
    val _2_2 = token_point_2_2.pieces.headOption.getOrElse(NullPiece)

    ((_0_0 == targetPiece && _0_1 == targetPiece && _0_2 == targetPiece)
      || (_1_0 == targetPiece && _1_1 == targetPiece && _1_2 == targetPiece) 
      || (_2_0 == targetPiece && _2_1 == targetPiece && _2_2 == targetPiece) 
      || (_0_0 == targetPiece && _1_0 == targetPiece && _2_0 == targetPiece) 
      || (_0_1 == targetPiece && _1_1 == targetPiece && _2_1 == targetPiece) 
      || (_0_2 == targetPiece && _1_2 == targetPiece && _2_2 == targetPiece) 
      || (_0_0 == targetPiece && _1_1 == targetPiece && _2_2 == targetPiece) 
      || (_0_2 == targetPiece && _1_1 == targetPiece && _2_0 == targetPiece))
  }

  override def boardFull(predicate: (Node) => Boolean 
    = (node: Node) => !node.pieces.isEmpty) = {
    nodes.forall(predicate)
  }
}

object Main {
  def gameSim(simulation: Stack[Int])   = {
    import beachc.Interfaces.TicTacToe_CLIInterface
    val interface = TicTacToe_CLIInterface(TicTacToe())
    def gameSimulator(): Int = {
      println(simulation head)
      simulation.pop()
    }

    //interface getInput(scala.io.StdIn.readInt)
    while (interface.gs.currentResultState == Pending) {
      interface getInput(gameSimulator)
      interface printBoard()
      println(interface.gs.currentResultState)
    }

  }
  def main(args: Array[String]): Unit = {
    val simulateXWin = Stack.range(0, 9)
    val simulateOWin = Stack(1, 0, 3, 4, 7, 8)
    val simulateDraw = Stack(1, 0, 2, 5, 3, 6, 4, 7, 8)

    gameSim(simulateXWin)
    gameSim(simulateOWin)
    gameSim(simulateDraw)
  }
}

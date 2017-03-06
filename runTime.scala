package beachc
import ast.{
  Player,
  PreviousPlayer,
  CurrentPlayer,
  NextPlayer,
  AnyPlayer,
  AllPlayers,
  NullPlayer
}


package object runTime {
  type Name = String
  type Layer = Int

  object Conversions {
    implicit def TwoTuple2Discrete2DCoordinate(rawCoord: (Int, Int)): Discrete2DCoordinate = Discrete2DCoordinate(rawCoord._1, rawCoord._2)
  }

  abstract class GameState(val name:Name) { 
    val players: List[Player]
    val playersState: Map[Player, PlayerState]
    val inputs: List[Input[_]]
    val graph: Map[(Int, Int), PointNode]
    val nodes: List[Node]
    val endConditions: List[EndCondition]
    val pieces: Map[String, Map[Player, Piece]]

    var currentPlayer: Int
    var currentResultState: GameOutcome = Pending

    def place(owner: Player, piece: Piece, node: Node): GameState = this
    def push(owner: Player, piece: Piece, node: Node): GameState = this
    def pop(owner: Player, piece: Piece, node: Node): GameState = this

    def twoInARow(owner: Player, piece: Piece): Boolean = true
    def threeInARow(owner: Player, piece: Piece): Boolean = true
    def fourInARow(owner: Player, piece: Piece): Boolean = true

    def twoInARow(owner: Player, label: String): Boolean = twoInARow(owner, getPiece(owner, label))
    def threeInARow(owner: Player, label: String): Boolean = threeInARow(owner, getPiece(owner, label))
    def fourInARow(owner: Player, label: String): Boolean = fourInARow(owner, getPiece(owner, label))

    def boardFull(predicate: (Node) => Boolean 
      = (node: Node) => !node.pieces.isEmpty): Boolean = true
    def boardEmpty: Boolean = nodes.forall((node) => node.pieces.isEmpty)
    def getPreviousPlayer: Player = {
      if (currentPlayer == 0) {
        players.last
      } else {
        players(currentPlayer - 1)
      }
    }
    def getCurrentPlayer: Player = players(currentPlayer)
    def getNextPlayer: Player = {
      if (currentPlayer == (players.length - 1)) {
        players(0)
      } else {
        players(currentPlayer + 1)
      }
    }
    def getPlayerState(i: Int): PlayerState = playersState(players(i))
    def getPlayerState(p: Player): PlayerState = playersState(p)
    def isCurrentPlayer(p: Player) = players(currentPlayer) == p
    def checkEndConditions = {
      endConditions.view.zipWithIndex.find(eCond => {
        if (eCond._1.predicate(this)) {
          println(s"EndCondition(${eCond._2}) satisfied")
          println
          true
        } else {
          false
        }
      }).map(_._1(this))
    }
    def nextTurn = {
      checkEndConditions
      currentPlayer += 1 
      currentPlayer = if (currentPlayer == players.length) { 
        0 
      } else { 
        currentPlayer
      }
    }
    def getPiece(owner: Player, label: String): Piece = {
      owner match {
        case PreviousPlayer => pieces(label)(getCurrentPlayer)
        case CurrentPlayer => pieces(label)(getCurrentPlayer)
        case NextPlayer => pieces(label)(getNextPlayer)
      }
    }
  }
  
  // Graph code
  case class Discrete2DCoordinate(x: Int, y: Int) {}
  abstract class Node(label: Any) {
    var pieces: List[Piece] = List()
    def push(piece: Piece): Unit = {
      pieces = piece :: pieces      
    }
  }
  case class PointNode(label: Discrete2DCoordinate) extends Node(label) {
    def isEmpty(): Boolean = {
      pieces isEmpty
    }
  }

  case class PlayerState(player: Player, var state: GameOutcome = Pending) {}

  // Pieces
  trait Entity
  abstract class Piece(owner: Player) extends Entity
  case object NullPiece extends Piece(NullPlayer) {}

  // Inputs
  abstract class Input[L](gs: GameState, label: L, callbacks: List[Function3[L, Player, GameState, Boolean]]) {
    def trigger(player: Player): Unit = {
      callbacks.foreach(_(label, player, gs))
    }
  }
  case class ButtonInput[L](gs: GameState, label: L, callbacks: List[Function3[L, Player, GameState, Boolean]]) extends Input(gs, label, callbacks) {}

  trait GameOutcome
  case object Win extends GameOutcome {}
  case object Lose extends GameOutcome {}
  case object Draw extends GameOutcome {}
  case object Pending extends GameOutcome {}
  case class EndCondition(player: Player, predicate: (GameState) => Boolean, outcome: GameOutcome) {
    def apply(gs: GameState): Unit = {
      player match {
        case PreviousPlayer => {
           
        }
        case CurrentPlayer => {}
        case NextPlayer => {}
        case AllPlayers => {}
        case otherPlayer => {
          gs.players.find(_ == otherPlayer)
        }
      }
      gs.currentResultState = outcome
    }
  }
}

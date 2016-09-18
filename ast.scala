package beachc

package object ast {
  implicit def tupleToCoord(t: Tuple2[Int, Int]): Coordinate = Coordinate(t._1, t._2)
  implicit def CoordToPoint(c: Coordinate): Point = new Point(c)

  type Layer = Int
  trait Input[T] {
    def apply(): T
  }
  trait NodeInput extends Input[Node] {
    def apply(): Node
  }
  case object CoordinateInput extends NodeInput {
    var x: Int = -1
    var y: Int = -1

    override def apply(): Point = Point(Coordinate(x, y))
  }
  abstract class NodeSelection extends Input[Node] {
    def apply: Node = new Point((0, 0))
  }

  // Signal to the meta programmer that an artifact needs access to the implementation's state.
  class GameState(val name:Name) { 
    val space: List[Graph] = List[Graph]() 
    def place(owner: Player, piece: Piece, node: Node): GameState = this
    def push(owner: Player, piece: Piece, node: Node): GameState = this
    def pop(owner: Player, piece: Piece, node: Node): GameState = this
    def TwoInARow(owner: Player, piece: Piece): Boolean = true
    def TwoInARow(owner: Player, piece: String): Boolean = true
    def ThreeInARow(owner: Player, piece: Piece): Boolean = true
    def ThreeInARow(owner: Player, piece: String): Boolean = true
    def FourInARow(owner: Player, piece: Piece): Boolean = true
    def FourInARow(owner: Player, piece: String): Boolean = true
    def boardFull: Boolean = true
    def boardFull(l: Layer): Boolean = true
    def boardEmpty: Boolean = true
    def boardEmpty(l: Layer): Boolean = true
  }
  type ScoringFunction = (GameState) => Double
  
  case class GameRuleSet(name: Name, players: Players, board: Graph, pieces: Pieces, endConds: EndConditions) {
    def letsPlayAGame() = {}
  }
  type Name = String

  /**
   * Player definitions
   **/
  type Players = List[Player]
  trait Player
  case class Person(val name:Name, sF: ScoringFunction) extends Player {}
  case class Computer(val name:Name, sF: ScoringFunction) extends Player {}
  case object PreviousPlayer extends Player {}
  case object CurrentPlayer extends Player {}
  case object NextPlayer extends Player {}
  case object AllPlayers extends Player {}
  case object NoPlayer extends Player {}
  case object AnyPlayer extends Player {}
  case class SomePlayers(players: Players) extends Player {}

  /**
   * Graph definitions
   **/
  trait NeighborType
  case object Direct extends NeighborType
  case object Indirect extends NeighborType
  case object NoDirect extends NeighborType
  trait Node {
    var stack: List[Entity] = List[Entity]()
    def isEmpty: Boolean = stack.isEmpty
  }
  trait Edge {
    def traverse(n: Node): Option[Node]
  }
  case class Coordinate(_1: Int, _2: Int) {}

  case class Graph(nodes: List[Node], edges: List[Edge]) {
    def apply(n: Node): Node = nodes.find(_ == n) match {
      case Some(n) => n
      case None => throw new Exception("Node not found")
    }
  }
  case class Point(position: Coordinate) extends Node { }
  abstract class Directed(n1: Node, n2: Node) extends Edge {
    override def traverse(n: Node): Option[Node] = 
      if (n == n1) { Some(n2) } 
      else if (n == n2) { Some(n1) } 
      else { None }
  }
  abstract class Bidirectional(n1: Node, n2: Node) extends Edge {
    override def traverse(n: Node): Option[Node] = 
      if (n == n1) { Some(n2) } 
      else if (n == n2) { None } 
      else { None }
  }
  case class CardinalEdge(n1: Node, n2: Node, d: CardinalDirection) extends Directed(n1, n2) {}
  trait CardinalDirection
  case object North extends CardinalDirection
  case object South extends CardinalDirection
  case object East extends CardinalDirection
  case object West extends CardinalDirection
  case object NorthEast extends CardinalDirection
  case object NorthWest extends CardinalDirection
  case object SouthEast extends CardinalDirection
  case object SouthWest extends CardinalDirection

  /**
   * Move and action definitions
   **/
  case class Attribute[T](attr: T)

  trait Action 
  case class Push(pieceName: Name, node:Node) extends Action
  case class Pop(pieceName: Name, node:Node) extends Action
  case class Place(pieceName: Name, node:Node) extends Action
  case class SetAttribute(pieceName: Name, attr:Attribute[_]) extends Action 

  case object PushToken extends Action {
    def apply(gS: GameState): GameState = new GameState("TicTacToe") //gS.push(CurrentPlayer, 
  }
  case class LegalMove(
    initiator: Player, 
    precondition: (GameState) => Boolean=(gs:GameState)=>true, 
    action: Action, 
    postcondition: (GameState) => Boolean=(gs:GameState)=>true) {}

  /**
   * Piece definitions
   **/
  trait Entity
  type Pieces = List[Piece]
  case class Piece(name: Name, owner: Player, moves: LegalMove) extends Entity {}

  /**
   * EndCondition definitions
   **/
  type EndConditions = List[EndCondition]
  type GameCondition = (GameState) => EndState
  case class EndCondition(val player:Player, cond: GameCondition) {}

  trait EndState 
  case object Win extends EndState
  case object Lose extends EndState
  case object Draw extends EndState
  case object Pending extends EndState
}

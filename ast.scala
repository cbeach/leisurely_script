package org.beachc.leisurely

import runTime.GameState

package object ast {

  abstract class GameRuleSet(name: Name) {
    val playStyle: PlayStyle
    val turnStyle: TurnStyle
    val players: List[Player]
    val graph: Graph
    val pieces: List[Piece]
    val legalMoves: List[LegalMove]
    val endConditions: List[EndCondition]
    val inputs: List[Input]  
  }

  /**
   * Play style
   **/
  trait PlayStyle
  case object Antagonistic extends PlayStyle
  case object Oportunistic extends PlayStyle
  case object Cooperative extends PlayStyle

  /**
   * Turn style
   **/
  trait TurnStyle
  case object Sequential extends TurnStyle
  case object Simultaneous extends TurnStyle
  case object Mixed extends TurnStyle
  case object FixedTime extends TurnStyle

  /**
   * Turn phase
   **/
  trait TurnPhase

  /**
   * Graph definitions
   **/
  trait NeighborType
  case object Direct extends NeighborType
  case object Indirect extends NeighborType
  case object NoDirect extends NeighborType

  trait Node {
    var pieces: List[Entity] = List[Entity]()
    def isEmpty: Boolean = pieces.isEmpty
  }
  trait Edge {
    def traverse(n: Node): Option[Node]
  }

  case class Graph(nodes: List[Node], edges: List[Edge]) {
    def apply(n: Node): Node = nodes.find(_ == n) match {
      case Some(n) => n
      case None => throw new Exception("Node not found")
    }
  }
  // Nodes
  case class PointNode(position: Discrete2DCoordinate) extends Node {
    def apply(rawCoord: (Int, Int)): PointNode = PointNode(Discrete2DCoordinate(rawCoord._1, rawCoord._2))  
  }

  // Edges
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

  case class BidirectionalEdge(n1: Node, n2: Node) extends Bidirectional(n1, n2) {}
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
  object Push extends Action
  object Pop extends Action
  object Place extends Action
  case class SetAttribute(pieceName: Name, attr:Attribute[_]) extends Action 

  case class LegalMove(
    initiator: Player, 
    label: String,
    precondition: (Input, Player, GameState) => Boolean=(in, player, gs)=>true, 
    action: Action, 
    postcondition: (Input, Player, GameState) => Boolean=(in, player, gs)=>true) {}

  /**
   * Piece definitions
   **/
  trait Entity
  type Pieces = List[Piece]
  case class Piece(name: Name, owner: Player) extends Entity {}

  /**
   * EndCondition definitions
   **/
  type EndConditions = List[EndCondition]
  type GameCondition = (GameState) => GameOutcome
  case class EndCondition(val player:Player, cond: GameCondition) {}

  trait GameOutcome 
  case object Win extends GameOutcome
  case object Lose extends GameOutcome
  case object Draw extends GameOutcome
  case object Pending extends GameOutcome
}

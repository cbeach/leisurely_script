/**
 * Common definitions
 *  - implicit conversions
 *  - type aliases
 *  - base classes/traits
 *  - container case classes/objects
 *
 *
 *
**/
package org.beachc

package object leisurely {
  import org.beachc.leisurely.runTime.{GameState}

  type Name = String
  type Layer = Int

  case class Discrete2DCoordinate(x: Int, y: Int) {}

  object implicits {
    import org.beachc.leisurely.ast.{PointNode}
    implicit def TwoTuple2Discrete2DCoordinate(rawCoord: (Int, Int)): Discrete2DCoordinate = Discrete2DCoordinate(rawCoord._1, rawCoord._2)
    implicit def CoordToPoint(c: Discrete2DCoordinate): PointNode = PointNode(c)
  }

  /**
   * Input definitions
   **/
  trait Input
  abstract class PlayerInput[L](label: L) extends Input {}
  case class ButtonInput[L](label: L) extends PlayerInput(label) {}

  /**
   * Player definitions
   **/
  class Player(val name: String) {
    override def toString: String = s"Player(${name})"
  }

  object Player {
    def apply(name: String): Player = new Player(name)
  }

  object PreviousPlayer extends Player("Last")
  object CurrentPlayer extends Player("Current")
  object NextPlayer extends Player("Next")
  object AnyPlayer extends Player("Any")
  object AllPlayers extends Player("All")
  object NoPlayer extends Player("None") {}
  object NullPlayer extends Player("Null")
}

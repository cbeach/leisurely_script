package org.leisurely_script.gdl


case class Coordinate(x:Int, y:Int) extends Ordered[Coordinate] {
  import scala.math.Ordered.orderingToOrdered
  def compare(that: Coordinate): Int = that match {
    case Coordinate(otherX:Int, otherY:Int) => (x, y) compare (otherX, otherY)
  }
}

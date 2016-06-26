import scala.reflect.runtime.universe._
import scala.reflect.{ClassTag, classTag}
import scala.math.Ordered.orderingToOrdered

object casey extends App {
  sealed trait GameExpression[A] {
    def evaluate: Option[A]
  }
  case class Operator_<[L, R](left: L, right: R, n: Boolean) extends BooleanExrpression(n) {}
  case class Operator_<=[L, R](left: L, right: R, n: Boolean) extends BooleanExrpression(n) {}
  case class Operator_>[L, R](left: L, right: R, n: Boolean) extends BooleanExrpression(n) {}
  case class Operator_>=[L, R](left: L, right: R, n: Boolean) extends BooleanExrpression(n) {}

  class BooleanExrpression(n: Boolean) extends GameExpression[Boolean] with Comp[Boolean] {
    val ord = Ordering[Boolean]
    def evaluate = Some(n)
    def compare[B](other: GameExpression[B]) = 
      new IntExpression(other.evaluate.get match {
        case o: Boolean => if(evaluate.get < o) -1 else if(evaluate.get == o) 0 else 1
      })
  }

  case class IntExpression(n: Int) extends GameExpression[Int] with Comp[Int] {
    val ord = Ordering[Int]
    def evaluate = Some(n)
    def compare[B](other: GameExpression[B]) = 
      new IntExpression(other.evaluate.get match {
        case o: Byte => if(evaluate.get < o.toInt) -1 else if(evaluate.get == o.toInt) 0 else 1
        case o: Char => if(evaluate.get < o.toInt) -1 else if(evaluate.get == o.toInt) 0 else 1
        case o: Short => if(evaluate.get < o.toInt) -1 else if(evaluate.get == o.toInt) 0 else 1
        case o: Int => if(evaluate.get < o) -1 else if(evaluate.get == o) 0 else 1
        case o: Long => if(evaluate.get.toLong < o) -1 else if(evaluate.get.toLong == o) 0 else 1
        case o: Double => if(evaluate.get.toDouble < o) -1 else if(evaluate.get.toDouble == o) 0 else 1
        case o: Float => if(evaluate.get.toFloat < o) -1 else if(evaluate.get.toFloat == o) 0 else 1
      })
  }
  case class LongExpression(n: Long) extends GameExpression[Long] with Comp[Long] {
    val ord = Ordering[Long]
    def evaluate = Some(n)
    def compare[B](other: GameExpression[B]) = 
      new IntExpression(other.evaluate.get match {
        case o: Byte => if(evaluate.get < o.toLong) -1 else if(evaluate.get == o.toLong) 0 else 1
        case o: Char => if(evaluate.get < o.toLong) -1 else if(evaluate.get == o.toLong) 0 else 1
        case o: Short => if(evaluate.get < o.toLong) -1 else if(evaluate.get == o.toLong) 0 else 1
        case o: Int => if(evaluate.get < o.toLong) -1 else if(evaluate.get == o.toLong) 0 else 1
        case o: Long => if(evaluate.get < o) -1 else if(evaluate.get == o) 0 else 1
        case o: Double => if(evaluate.get.toDouble < o) -1 else if(evaluate.get.toDouble == o) 0 else 1
        case o: Float => if(evaluate.get.toFloat < o) -1 else if(evaluate.get.toFloat == o) 0 else 1
      })
  }
  implicit def BtoBE(e: Boolean) = new BooleanExrpression(e)
  implicit def ItoIE(e: Int) = new IntExpression(e)
  implicit def LtoLE(e: Long) = new LongExpression(e)

  implicit def BEtoB(e: BooleanExrpression) = e.evaluate.get
  implicit def IEtoI(e: IntExpression) = e.evaluate.get
  implicit def LEtoL(e: LongExpression) = e.evaluate.get

  implicit def LEtoIE(e: LongExpression) = IntExpression(e.evaluate.get.toInt)
  implicit def IEtoLE(e: IntExpression) = LongExpression(e.evaluate.get.toLong)

  trait Comp[A] { outer: GameExpression[A] =>
    val ord: Ordering[A]
    def compare[B](right: GameExpression[B]): IntExpression
    def <[B](other: GameExpression[B]): Operator_<[GameExpression[A], GameExpression[B]] = {
      Operator_<[GameExpression[A], GameExpression[B]](outer, other, outer.compare(other).evaluate.get < 0) 
    }
    def <=[B](other: GameExpression[B]): Operator_<=[GameExpression[A], GameExpression[B]] = {
      Operator_<=[GameExpression[A], GameExpression[B]](outer, other, outer.compare(other).evaluate.get <= 0)  
    }
    def >[B](other: GameExpression[B]): Operator_>[GameExpression[A], GameExpression[B]] = {
      Operator_>[GameExpression[A], GameExpression[B]](outer, other, outer.compare(other).evaluate.get > 0) 
    }
    def >=[B](other: GameExpression[B]): Operator_>=[GameExpression[A], GameExpression[B]] = {
      Operator_>=[GameExpression[A], GameExpression[B]](outer, other, outer.compare(other).evaluate.get >= 0) 
    }
  }  
  trait Arith[A] { outer: GameExpression[A] =>
    val num: Numeric[A]
    def +(e: GameExpression[A])(implicit e1: A =:= Int): IntExpression = 
      IntExpression(outer.evaluate.get + e.evaluate.get)
  }
  trait Test[B <: AnyVal] {}

  val i = IntExpression(10)
  val l = LongExpression(20)
  val m = LongExpression(10)
  val a: BooleanExrpression =  i < l
  val b: BooleanExrpression =  i <= l
  val c: BooleanExrpression =  i > l
  val d: BooleanExrpression =  i >= l
  val e: BooleanExrpression =  i < m
  val f: BooleanExrpression =  i <= m
  val g: BooleanExrpression =  i > m
  val h: BooleanExrpression =  i >= m

  assert(a.evaluate.get == true)
  assert(b.evaluate.get == true)
  assert(c.evaluate.get == false)
  assert(d.evaluate.get == false)
  assert(e.evaluate.get == false)
  assert(f.evaluate.get == true)
  assert(g.evaluate.get == false)
  assert(h.evaluate.get == true)

}

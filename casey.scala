import scala.reflect.runtime.universe._
import scala.reflect.{ClassTag, classTag}
import scala.math.Ordered.orderingToOrdered

object casey extends App {
  sealed trait Expr[A] {
    def eval: A
  }
  trait BoolExpr extends Expr[Boolean]
  trait Operator_<[L, R] extends BoolExpr {
    val left: L
    val right: R
  }

  case class IntExpr(n: Int) extends Expr[Int] with Comp[Int] {
    val ord = Ordering[Int]
    def eval = n
  }
  case class LongExpr(n: Long) extends Expr[Long] with Comp[Long] {
    val ord = Ordering[Long]
    def eval = n
  }
  implicit def LEtoIE(e: LongExpr) = IntExpr(e.eval.toInt)
  implicit def IEtoLE(e: IntExpr) = LongExpr(e.eval.toLong)

  trait Comp[A] { outer: Expr[A] =>
    val ord: Ordering[A]
    def <[B: ClassTag](other: Expr[B])(implicit e1: A =:= Int, e2: B =:= Long): Operator_<[A, B] = {
      val left = outer.eval
      val right = other.eval
      val temp = left match {
        case l: Int => println("int")
      }
      new Operator_<[A, B] {
        val left = outer.eval
        val right = other.eval
        def eval = outer.eval.toLong < other.eval
      }
    }
  }  
  trait Arith[A] { outer: Expr[A] =>
    val num: Numeric[A]
    def +(e: Expr[A])(implicit e1: A =:= Int): IntExpr = 
      IntExpr(outer.eval + e.eval)
  }
  trait Test[B <: AnyVal] {}

  val i = IntExpr(10)
  val l = LongExpr(20)
  val b: BoolExpr =  i < l

  println(s"evaluation of b.eval is: ${b.eval}")

}

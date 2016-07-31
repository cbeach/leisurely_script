package org.leisurely_script.gdl.expressions.operator_ast_nodes

import org.leisurely_script.gdl.types._

/**
  * Created by mcsmash on 7/31/16.
  */
private[gdl] object ArithmeticOperators {
  sealed abstract class BaseArithmeticOperator[L, R] extends AnyValExpression[AnyVal] {
    val left: L
    val right: R
    val DDOp: (Double, Double) => Double
    val LLOp: (Long, Long) => Long

    override def evaluate: Option[AnyVal] = {
      val retVal: Option[AnyVal] = Some(left match {
        case l: DoubleExpression => {
          right match {
            case r: DoubleExpression => DDOp(l.evaluate.get, r.evaluate.get)
            case r: LongExpression => DDOp(l.evaluate.get, r.evaluate.get.toDouble)
          }
        }
        case l: LongExpression => right match {
          case r: DoubleExpression => DDOp(l.evaluate.get.toDouble, r.evaluate.get)
          case r: LongExpression => LLOp(l.evaluate.get, r.evaluate.get)
        }
      })
      retVal
    }
  }
  case class Operator_+[L, R](left: L, right: R) extends BaseArithmeticOperator[L, R] {
    val DDOp = (l: Double, r: Double) => l + r
    val LLOp = (l: Long, r: Long) => l + r
  }
  case class Operator_-[L, R](left: L, right: R) extends BaseArithmeticOperator[L, R] {
    val DDOp = (l: Double, r: Double) => l - r
    val LLOp = (l: Long, r: Long) => l - r
  }
  case class Operator_*[L, R](left: L, right: R) extends BaseArithmeticOperator[L, R] {
    val DDOp = (l: Double, r: Double) => l * r
    val LLOp = (l: Long, r: Long) => l + r
  }
  case class Operator_/[L, R](left: L, right: R) extends BaseArithmeticOperator[L, R] {
    val DDOp = (l: Double, r: Double) => l / r
    val LLOp = (l: Long, r: Long) => l / r
  }
  case class Operator_%[L, R](left: L, right: R) extends BaseArithmeticOperator[L, R] {
    val DDOp = (l: Double, r: Double) => l % r
    val LLOp = (l: Long, r: Long) => l % r
  }
  case class UnaryOperator_+[R](self: R) extends AnyValExpression[AnyVal] {
    override def evaluate:Option[AnyVal] = self match {
      case s: LongExpression => {
        val temp = +s.evaluate.get
        Some(temp)
      }
      case s: DoubleExpression => {
        val temp = +s.evaluate.get
        Some(temp)
      }
      case s => throw new IllegalArgumentException(s"Unknown argument type. $s");
    }
  }
  case class UnaryOperator_-[R](self: R) extends AnyValExpression[AnyVal] {
    override def evaluate:Option[AnyVal] = self match {
      case s: LongExpression => Some(-s.evaluate.get)
      case s: DoubleExpression => Some(-s.evaluate.get)
      case s => throw new IllegalArgumentException(s"Unknown argument type. $s");
    }
  }
}

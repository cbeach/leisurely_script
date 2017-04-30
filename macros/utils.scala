package org.beachc.leisurely.metaprogrammers

import scala.meta._

object Utils {
  def stringLit(lit: meta.Lit): String = {
    lit match {
      case q"${string: Lit}" => {
        string.value match {
          case v: String => v
          case _ => throw new Exception("Wrong type for literal")
        }
      }
    }
  }
  def getValName(valDefn: meta.Tree): String = { 
    valDefn match {
      case q"..$mods val $patsnel: $tpeopt = $expr" => {
        patsnel match {
          case q"${term: Pat.Var.Term}" => {
            term.name match {
              case q"${n: Term.Name}" => {
                n.value match {
                  case v: String => v
                  case _ => throw new Exception("Wrong type for literal")
                }
              }
            }
          }
        }
      }
    }
  }
  def getValExpr(valDefn: meta.Tree): meta.Tree = { 
    valDefn match {
      case q"..$mods val $patsnel: $tpeopt = $expr" => {
        expr
      }
    }
  }
  def getValNameExpr(valDefn: meta.Tree): (String, meta.Tree) = (getValName(valDefn), getValExpr(valDefn))
  def listContents(l: meta.Tree): Seq[meta.Tree] = decomposeList(l)._2
  def decomposeThing(thingsName: String)(l: meta.Tree): (meta.Term.Name, Seq[meta.Term.Arg]) = {
    val decomposition = decomposeFunctionApplication(l)
    require(decomposition._1.value == thingsName, s"Value is not a ${thingsName}") 
    decomposition
  }
  def decomposeList(l: meta.Tree): (meta.Term.Name, Seq[meta.Term.Arg]) = {
    val decomposition = decomposeFunctionApplication(l)
    require(decomposition._1.value == "List", "Value is not a List") 
    decomposition
  }
  def decomposeFunctionApplication(application: meta.Tree): (meta.Term.Name, Seq[meta.Term.Arg])  = {
    application match {
      case q"${name: Term.Name}(..$aexprssnel)" => {
        (name, aexprssnel)
      }
    }
  }
  def decomposeTuple(arity: Int)(t: meta.Tree): Seq[meta.Tree] = t match {
    case q"(..$exprsnel)" => {
      require(exprsnel.size == arity, s"Incorrect tuple arity. expected: ${arity}, actual: ${exprsnel.size}")
      exprsnel
    }
  }
  def decomposeTuple2(t: meta.Tree) = decomposeTuple(2)(t)
  def decomposeTuple3(t: meta.Tree) = decomposeTuple(3)(t)
  def decomposeTuple4(t: meta.Tree) = decomposeTuple(4)(t)
  def temp(t: String): Unit = ()
}

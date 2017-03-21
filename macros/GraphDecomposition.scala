package org.beachc.leisurely.metaprogrammers

import scala.meta._

object DecomposeGraph {
  def apply(graph: Tree): Any = {
    graph match {
      case q"$expr(..$aexprssnel)"  => {
        expr match {
          case q"${name: Term.Name}" => {
            name match {
              case Term.Name("Graph") => {
                DecomposeNodes(aexprssnel(0))
                DecomposeEdges(aexprssnel(1))
              }
              case _ => {
                println("wat?")
              }
            }
          }
        }
      }
      case _ => {
        println("where's my graph expression?")
      }
    }
  }
}

object DecomposeNodes {
  def apply(nodeList: Tree): Any = {
    nodeList.children.foreach((node) => {
      node match {
        case q"${name: Term.Name}" => {
          println(s"name: ${name.value}")
        }
        // Get the Node's type
        case q"Point($aexprssnel)" => {
          aexprssnel match {
            case q"(..$exprsnel)" => {
              exprsnel.foreach(_ match {
                case q"${lit: Lit}" => {
                  println(s"\ttype: ${lit.value.getClass}")  // :D
                }
              })
            }
          }
        }
      }
    })
  }
}

object DecomposeEdges {
  def apply(nodeList: Tree): Any = {
    println("decomposing edges")             
  }
}

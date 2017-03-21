package org.beachc.leisurely.metaprogrammers

import scala.annotation.StaticAnnotation
import scala.meta._

//class RecursiveGameStateGenerator extends StaticAnnotation {
//  inline def apply(tree: Any): Any = meta {
//
//  }
//}

class GameStateGenerator extends StaticAnnotation {
  inline def apply(tree: Any): Any = meta {
    tree match {
      case q"..$mods val ..$patsnel: $tpeopt = $expr" => {
        //Unwrap GameRuleSet
        expr match {
          case q"$expr(..$aexprssnel)" => {
            expr match {
              case q"${name: Term.Name}" => {
                if (name.value == "GameRuleSet") {
                  aexprssnel match {
                    case Seq(name, players, graph, pieces, eConds) => {
                      DecomposeGraph(graph);
                    }
                  }
                }
              }
            }
          }
          case q"_" => {
            println("wildcard 2")
          }
        }
      }
      case q"_" => {
        println("wildcard 1")
      }
    }
    tree
  }
}


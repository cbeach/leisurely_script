package beachc.metaprogrammers

import java.io.ObjectInputStream
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

import scala.annotation.StaticAnnotation
import scala.meta._

class GameStateGenerator extends StaticAnnotation {
  inline def apply(tree: Tree) = meta {
    tree match {
      case q"..$mods val ..$patsnel: $tpeopt = $expr" => {
        println("definition val")
        //Unwrap GameRuleSet
        expr match {
          case q"$expr(..$aexprssnel)" => {
            println("  .application")
            expr match {
              case q"${name: Term.Name}" => {
                println("    .name")
                if (name.value == "GameRuleSet") {
                  aexprssnel match {
                    case Seq(name, players, graph, pieces, eConds) => {

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


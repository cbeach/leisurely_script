package org.beachc.leisurely.metaprogrammers

import scala.annotation.StaticAnnotation
import scala.meta._

object Sequential
object Antagonistic

class GameStateGenerator extends StaticAnnotation {

  inline def apply(tree: Any): Any = meta {
    val compiler = Compiler()
    compiler parseGRSRootSignature tree
    tree
  }
  inline def parseGRSMembers(tree: Any): Unit = meta {
    tree
  }
}

case class Compiler() {
  var _name: String = ""
  var _players: List[String] = List()
  var _turnStyle = Sequential
  var _playStyle = Antagonistic

  def parseGRSRootSignature(tree: scala.meta.Tree): Unit = {
    tree match {
      case q"..$mods object ${name: Term.Name} extends $template" => {
        template match {
          case template"{ ..$stats } with ..$ctorcalls { $param => ..$members }" => {
            ctorcalls.foreach(_ match {
              // extends GameRuleSet("<gameId>")
              case q"$gRS($gameId)" => {
                gRS match {
                  case ctor"${ctorname: Ctor.Name}" => {
                    //TODO: ctorname.value must be GameRuleSet
                    //println(s"ctorname: ${ctorname.value}")
                  }
                }
                gameId match {
                  case q"${lit: Lit}" => {
                    _name = lit.value match {
                      case v: String => v
                      case _ => throw new Exception("Error!")
                    }
                  }
                }
              }
            })
            members.foreach(parseGRSMembers _)
          }
        }
      }
    }
    tree
  }

  def parseGRSMembers(member: scala.meta.Tree): Unit = {
    Utils.getValName(member) match {
      case "players" => parseGRSPlayers(member)
      case "playStyle" => {}
      case "turnStyle" => {}
      case "graph" => {}
      case "pieces" => {}
      case "legalMoves" => {}
      case "endConditions" => {}
      case "inputs" => {}
    }
  }
  def parseGRSPlayers(players: List[String]): Unit = {
    
  }
}

object Utils {
  def getValName(valDefn: scala.meta.Tree): String = { 
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
}

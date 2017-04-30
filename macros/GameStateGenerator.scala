package org.beachc.leisurely.metaprogrammers

import scala.annotation.StaticAnnotation
import scala.meta._
import Utils._

class GameStateGenerator extends StaticAnnotation {
  inline def apply(tree: Any): Any = meta {
    tokens.GAME_DEFN(tree)
    tree
  }

  inline def parseGRSMembers(tree: Any): Unit = meta {
    tree
  }
}

case class Parser() {
  var _name = ""
  var _players: List[String] = List()
  //var _turnStyle = Sequential
  //var _playStyle = Antagonistic
  var _graph = ()
  var _legalMoves = ()
  var _pieces = ()
  var _endConditions = ()
  var _inputs = ()

  def parseGRSPlayers(players: scala.meta.Tree): Unit = {
    players match {
      case q"..$mods val $patsnel: $tpeopt = $expr" => {
        val playerList = decomposeFunctionApplication(expr)

      }
    }
  }

  def decomposePlayer(players: List[scala.meta.Tree]): List[String] = {
    println(players.map(decomposeFunctionApplication(_)))
    List("a")
  }
}


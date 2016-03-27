package org.leisurely_script.gdl

import org.leisurely_script.gdl.types.GameResultExpression
import org.leisurely_script.implementation.Game
import org.leisurely_script.gdl.GameResultState._


trait PlayerValidator extends {
  def playersValid(game:Game, players:Set[Player]):Boolean
  def playersValid(game:Game, players:ConcretelyKnownPlayer):Boolean = {
    playersValid(game, players.getPlayers)
  }
  def getPlayers(game:Game):Set[Player]
  def wins = GameResultExpression(this, Win)
  def loses = GameResultExpression(this, Lose)
  def ties = GameResultExpression(this, Tie)
}

trait ConcretelyKnownPlayer {
  def getPlayers:Set[Player]
}

case object AllPlayers extends PlayerValidator {
  override def playersValid(game:Game, players:Set[Player]):Boolean = {
    game.all.players.toSet == players
  }
  override def toString:String = {
    "Player(All)"
  }
  override def getPlayers(game:Game):Set[Player] = {
    game.all.players.toSet
  }
}

case object PreviousPlayer extends PlayerValidator {
  override def playersValid(game:Game, players:Set[Player]):Boolean = {
    if (players.size != 1) {
      if (players.size < 1) {
        throw new IllegalPlayerException("Team play and alliances are not supported yet")
      } else {
        throw new IllegalPlayerException("No player was provided for validation.")
      }
    }
    players.head == game.previous.player
  }
  def playerValid(game:Game, player:Player):Boolean = {
    playersValid(game, Set(player))
  }
  override def toString:String = {
    "Player(Previous)"
  }
  override def getPlayers(game:Game):Set[Player] = {
    Set[Player](game.previous.player)
  }
}

case object CurrentPlayer extends PlayerValidator {
  override def playersValid(game:Game, players:Set[Player]):Boolean = {
    if (players.size != 1) {
      if (players.size < 1) {
        throw new IllegalPlayerException("Team play and alliances are not supported yet")
      } else {
        throw new IllegalPlayerException("No player was provided for validation.")
      }
    }
    players.head == game.current.player
  }
  def playerValid(game:Game, player:Player):Boolean = {
    playersValid(game, Set(player))
  }
  override def toString:String = {
    "Player(Current)"
  }
  override def getPlayers(game:Game):Set[Player] = {
    Set[Player](game.current.player)
  }
}

case object NextPlayer extends PlayerValidator {
  override def playersValid(game:Game, players:Set[Player]):Boolean = {
    if (players.size != 1) {
      if (players.size < 1) {
        throw new IllegalPlayerException("Team play and alliances are not supported yet")
      } else {
        throw new IllegalPlayerException("No player was provided for validation.")
      }
    }
    players.head == game.next.player
  }
  def playerValid(game:Game, player:Player):Boolean = {
    playersValid(game, Set(player))
  }
  override def toString:String = {
    "Player(Next)"
  }
  override def getPlayers(game:Game):Set[Player] = {
    Set[Player](game.next.player)
  }
}

case class SomePlayers(validPlayers:Set[Player]) extends PlayerValidator with ConcretelyKnownPlayer {
  override def playersValid(game:Game, players:Set[Player]):Boolean = {
    players.subsetOf(validPlayers.toSet) && players.subsetOf(game.all.players.toSet)
  }
  override def toString:String = {
    s"[${validPlayers map(player => player toString) mkString ", "}]"
  }
  override def getPlayers(game:Game):Set[Player] = {
    validPlayers
  }
  override def getPlayers:Set[Player] = validPlayers
}

case class SubsetOfPlayers(validPlayers:Set[Player]) extends PlayerValidator with ConcretelyKnownPlayer {
  override def playersValid(game:Game, player:Set[Player]):Boolean = {
    validPlayers.subsetOf(game.all.players.toSet)
  }
  override def toString:String = {
    s"[${validPlayers map(player => player toString) mkString ", "}]"
  }
  override def getPlayers(game:Game):Set[Player] = {
    validPlayers
  }
  override def getPlayers:Set[Player] = validPlayers
}

case object NoPlayer extends PlayerValidator with ConcretelyKnownPlayer {
  override def playersValid(game:Game, player:Set[Player]):Boolean = {
    false
  }
  override def toString:String = {
    "Player(None)"
  }
  override def getPlayers(game:Game):Set[Player] = {
    getPlayers
  }
  override def getPlayers:Set[Player] = {
    Set[Player]()
  }
}

case object AnyPlayer extends PlayerValidator {
  override def playersValid(game:Game, players:Set[Player]):Boolean = {
    if (players.size == 0) {
      false
    } else {
      players.map(player => {
        game.all.players.contains(player)
      }).reduce(_ || _)
    }
  }
  def playerValid(game:Game, player:Player):Boolean = {
    playersValid(game, Set(player))
  }
  override def toString:String = {
    "Player(Any)"
  }
  override def getPlayers(game:Game):Set[Player] = {
    game.all.players.toSet
  }
}

case class SpecificPlayer(player:Player) extends PlayerValidator with ConcretelyKnownPlayer {
  override def playersValid(game:Game, players:Set[Player]):Boolean = {
    if (players.size != 1) {
      false
    } else {
      players.contains(player)
    }
  }
  override def playersValid(game:Game, players:ConcretelyKnownPlayer):Boolean = {
    playersValid(game, players.getPlayers)
  }
  def getPlayers(game:Game):Set[Player] = Set(player)
  def getPlayers:Set[Player] = Set(player)
}

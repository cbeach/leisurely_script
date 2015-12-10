package org.leisurely_script.gdl

import org.leisurelyscript.gdl.{Player, Game, PlayerValidator}

/**
  * Created by mcsmash on 12/10/15.
  */
class AllPlayers extends PlayerValidator {
    override def playersValid(game:Game, players:Set[Player]):Boolean = {
        game.players.all.toSet == players
    }
    override def toString:String = {
        "Player(All)"
    }
    override def getPlayers(game:Game):Set[Player] = {
        game.players.all.toSet
    }
}

object AllPlayers {
    def apply:AllPlayers = new AllPlayers
}


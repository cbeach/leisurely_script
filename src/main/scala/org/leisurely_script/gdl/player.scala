package org.leisurelyscript.gdl


case class Player(val name:String = java.util.UUID.randomUUID.toString) extends ConcretelyKnownPlayer with PlayerValidator {
    override def playersValid(game:Game, players:Set[Player]):Boolean = {
        if (players.size != 1) {
            false
        } else {
            players.head == this
        }
    }
    override def getPlayers(game:Game):Set[Player] = Set(this)
    override def getPlayers:Set[Player] = {
        Set(this)
    }
    def valid(game:Game, player:Player):Boolean = {
        player == this
    }
    override def toString:String = {
        s"Player(${name})"
    }
    def get(game:Game):Player = {
        this
    }
}

object Player {
    def apply(other:Player) = {
        new Player(other.name)
    }

    //def apply(name:String = null):Player = {
    //    if (name == null) {
    //        new Player()
    //    } else {
    //        new Player(name)
    //    }
    //}
}

package org.leisurelyscript

class Player(val name:String = java.util.UUID.randomUUID.toString) { 
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

    def apply(name:String = null):Player = {
        if (name == null) {
            new Player()
        } else {
            new Player(name)
        }
    }
}

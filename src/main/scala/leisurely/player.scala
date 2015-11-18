package org.leisurelyscript

class Player(val name:String = java.util.UUID.randomUUID.toString) { 
    def valid(player:Player):Boolean = {
        player == this
    }
    override def toString:String = {
        s"Player(${name})"
    }
}

object Player {
    def apply(name:String = null):Player = {
        if (name == null) {
            new Player()
        } else {
            new Player(name)
        }
    }
}

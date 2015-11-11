package org.leisurelyscript

class Player(val name:String = java.util.UUID.randomUUID.toString) { }

object Player {
    def apply(name:String = null):Player = {
        if (name == null) {
            new Player()
        } else {
            new Player(name)
        }
    }
}

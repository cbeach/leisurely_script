package org.leisurelyscript


trait PlayerValidator {
    def playersValid(game:Game, players:Set[Player]):Boolean
    def playersValid(game:Game, players:ConcretelyKnownPlayer):Boolean = {
        playersValid(game, players.getPlayers)
    }
    def getPlayers(game:Game):Set[Player]
}

trait ConcretelyKnownPlayer { 
    def getPlayers:Set[Player]
}

case class SpecificPlayer(player:Player) extends PlayerValidator with ConcretelyKnownPlayer {
    override def playersValid(game:Game, players:Set[Player]):Boolean = {
        if (players.size != 1) {
            if (players.size < 1) {
                throw new IllegalPlayerException("Team play and alliances are not supported yet")
            } else {
                throw new IllegalPlayerException("No player was provided for validation.")
            }
        }
        players.head == player
    }
    def playerValid(game:Game, player:Player):Boolean = {
        player == this.player
    }
    override def toString:String = {
        "Player(Previous)"
    }
    override def getPlayers(game:Game):Set[Player] = {
        Set(player)
    }
    override def getPlayers:Set[Player] = Set(player)

}

case object Previous extends PlayerValidator {
    override def playersValid(game:Game, players:Set[Player]):Boolean = {
        if (players.size != 1) {
            if (players.size < 1) {
                throw new IllegalPlayerException("Team play and alliances are not supported yet")
            } else {
                throw new IllegalPlayerException("No player was provided for validation.")
            }
        }
        players.head == game.players.previous
    }
    def playerValid(game:Game, player:Player):Boolean = {
        playersValid(game, Set(player))
    }
    override def toString:String = {
        "Player(Previous)"
    }
    override def getPlayers(game:Game):Set[Player] = {
        Set[Player](game.players.previous)
    }
}

case object Current extends PlayerValidator {
    override def playersValid(game:Game, players:Set[Player]):Boolean = {
        if (players.size != 1) {
            if (players.size < 1) {
                throw new IllegalPlayerException("Team play and alliances are not supported yet")
            } else {
                throw new IllegalPlayerException("No player was provided for validation.")
            }
        }
        players.head == game.players.current
    }
    def playerValid(game:Game, player:Player):Boolean = {
        playersValid(game, Set(player))
    }
    override def toString:String = {
        "Player(Current)"
    }
    override def getPlayers(game:Game):Set[Player] = {
        Set[Player](game.players.current)
    }
}

case object Next extends PlayerValidator {
    override def playersValid(game:Game, players:Set[Player]):Boolean = {
        if (players.size != 1) {
            if (players.size < 1) {
                throw new IllegalPlayerException("Team play and alliances are not supported yet")
            } else {
                throw new IllegalPlayerException("No player was provided for validation.")
            }
        }
        players.head == game.players.next
    }
    def playerValid(game:Game, player:Player):Boolean = {
        playersValid(game, Set(player))
    }
    override def toString:String = {
        "Player(Next)"
    }
    override def getPlayers(game:Game):Set[Player] = {
        Set[Player](game.players.next)
    }
}

case class SomePlayers(validPlayers:Set[Player]) extends PlayerValidator with ConcretelyKnownPlayer {
    override def playersValid(game:Game, players:Set[Player]):Boolean = {
        players.subsetOf(validPlayers.toSet) && players.subsetOf(game.players.all.toSet)
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
        validPlayers.subsetOf(game.players.all.toSet) 
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

case object Any extends PlayerValidator {
    override def playersValid(game:Game, players:Set[Player]):Boolean = {
        if (players.size == 0) {
            false 
        } else {
            players.map(player => {
                game.players.all.contains(player)
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
        game.players.all.toSet
    }
}

case object All extends PlayerValidator {
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

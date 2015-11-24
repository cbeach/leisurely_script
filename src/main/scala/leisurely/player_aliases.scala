package org.leisurelyscript


class Previous extends Player() {
    override def valid(game:Game, player:Player):Boolean = {
        player == game.players.previous 
    }
    override def toString:String = {
        "Player(Previous)"
    }
    override def get(game:Game):Player = {
        game.players.previous
    }
}

class Current extends Player() {
    override def valid(game:Game, player:Player):Boolean = {
        player == game.players.current 
    }
    override def toString:String = {
        "Player(Current)"
    }
    override def get(game:Game):Player = {
        game.players.current
    }
}

class Next extends Player() {
    override def valid(game:Game, player:Player):Boolean = {
        player == game.players.next 
    }
    override def toString:String = {
        "Player(Next)"
    }
    override def get(game:Game):Player = {
        game.players.next
    }
}

class SomePlayers(validPlayers:List[Player]) extends Player() {
    def valid(game:Game, players:List[Player]):Boolean = {
        val playerSet = players.toSet
        playerSet.subsetOf(validPlayers.toSet) && playerSet.subsetOf(game.players.all.toSet)
    }
    override def toString:String = {
        s"[${validPlayers map(player => player toString) mkString ", "}]"
    }
}

class SubsetOfPlayers(validPlayers:Set[Player]) extends Player() {
    def valid(game:Game, players:List[Player]):Boolean = {
        validPlayers.subsetOf(game.players.all.toSet) 
    }
    override def toString:String = {
        s"[${validPlayers map(player => player toString) mkString ", "}]"
    }
}

class NoPlayer extends Player() {
    override def valid(game:Game, player:Player):Boolean = {
        false 
    }
    override def toString:String = {
        "Player(None)"
    }
}

class Any extends Player() {
    override def valid(game:Game, player:Player):Boolean = {
        true 
    }
    override def toString:String = {
        "Player(Any)"
    }
}

class All extends Player() {
    def valid(game:Game, players:List[Player]):Boolean = {
        val all = game.players.all
        players.sortWith(all.indexOf(_) < all.indexOf(_)) == all
    }
    override def toString:String = {
        "Player(All)"
    }
}

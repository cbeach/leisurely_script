package org.leisurelyscript


class Players(players:List[Player]) {
    def this(players:Player*) = {
        this(players.toList)
    }

    def this(players:List[Player], currentPlayer:Int) {
        this(players)
        this.currentPlayer = currentPlayer
    }

    var currentPlayer:Int = 0
    def all:List[Player] = players

    def previous:Player = {
        if (currentPlayer != 0) players(currentPlayer - 1) 
            else players(players.length - 1)
    }

    def current:Player = players(currentPlayer)

    def next:Player = {
        if (currentPlayer <= players.length - 1) players(currentPlayer + 1) 
            else players(0)
    }

    def endTurn:Players = {
        val currPlayer = 
            if (currentPlayer < players.length - 1) currentPlayer + 1 
            else 0
        new Players(players, currPlayer)
    }
    
    def +(that:List[Player]):Players = {
        new Players(this.players ::: that)
    }

    class Previous extends Player() {
        override def valid(player:Player):Boolean = {
            player == previous 
        }
        override def toString:String = {
            "Player(Previous)"
        }
    }
    class Current extends Player() {
        override def valid(player:Player):Boolean = {
            player == current 
        }
        override def toString:String = {
            "Player(Current)"
        }
    }
    class Next extends Player() {
        override def valid(player:Player):Boolean = {
            player == next 
        }
        override def toString:String = {
            "Player(Next)"
        }
	}
    class SomePlayers(players:List[Player]) extends Player() {
        val validPlayers = { 
            players.sortWith(all.indexOf(_) < all.indexOf(_))
        }
        def valid(players:List[Player]):Boolean = {
            players.sortWith(validPlayers.indexOf(_) < validPlayers.indexOf(_)) == validPlayers
        }
        override def toString:String = {
            s"[${validPlayers map(player => player toString) mkString ", "}]"
        }
	}
    class NoPlayer extends Player() {
        override def valid(player:Player):Boolean = {
            false 
        }
        override def toString:String = {
            "Player(None)"
        }
	}
    class Any extends Player() {
        override def valid(player:Player):Boolean = {
            true 
        }
        override def toString:String = {
            "Player(Any)"
        }
	}
    class All extends Player() {
        def valid(players:List[Player]):Boolean = {
            players.sortWith(all.indexOf(_) < all.indexOf(_)) == all
        }
        override def toString:String = {
            "Player(All)"
        }
	}
}



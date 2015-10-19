package org.leisurelyscript

import org.scalatest.FunSuite


class GameTree extends FunSuite {
    test("A player can be created with a name") { 
        val fred = new Player("Fred")
        assert(fred.name == "Fred")
    }
    test("Instantiating a player without a name executes successfully") { 
        val noName = new Player() 
    }
    test("Creating a player without a name results in a UUID being generated") { 
        val noName = new Player() 
        val uuidRegex = """^\w{8}-\w{4}-\w{4}-\w{4}-\w{12}$""".r
        uuidRegex findFirstIn noName.name match {
            case None => fail
            case _ => println("Test passes")
        }
    }
    test("Game() should produce a game object without error") { 
        val game = new Game() 
    }
    test("Creating a game without a name results in a UUID being generated") { 
        val noName = new Game() 
        val uuidRegex = """^\w{8}-\w{4}-\w{4}-\w{4}-\w{12}$""".r
        uuidRegex findFirstIn noName.name match {
            case None => fail
            case _ => println("Test passes")
        }
    }
    test("Game.players.all should return all players.") { 
        val game = new Game().add(new Player("Andrew"), new Player("Bill"), new Player("Carol"))
        assert(game.players.all.length == 3)
    }
    test("If the game already has players, then game.add(<players>*) " 
         + "should return a new game with the new players appended") {
        val game = new Game().add(new Player("Andrew"), new Player("Bill")).add(new Player("Carol"))
        assert(game.players.all.length == 3)
    }
}

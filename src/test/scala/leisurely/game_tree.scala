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
            case _ => {} 
        }
    }
    test("Game() should produce a game object without error") { 
        val game = Game() 
    }
    test("Creating a game without a name results in a UUID being generated") { 
        val noName = Game() 
        val uuidRegex = """^\w{8}-\w{4}-\w{4}-\w{4}-\w{12}$""".r
        uuidRegex findFirstIn noName.name match {
            case None => fail
            case _ => {} 
        }
    }
    test("Game.players.all should return all players.") { 
        val game = Game().add(new Player("Andrew"), new Player("Bill"), new Player("Carol"))
        assert(game.players.all.length == 3)
    }

    // This could be added later for convenience, but is not critical and should be skipped for now.
    ignore("If the game already has players, then game.add(<players>*) " 
         + "should return a new game with the new players appended") {
        val game = Game().add(new Player("Andrew"), new Player("Bill")).add(new Player("Carol"))
        assert(game.players.all.length == 3)
    }

    test("A 3x3 board should have nine nodes") {
        import Shape._
        import NeighborType._
        val board = Board(List(3, 3), Square, Indirect, Square) 
        assert(board.nodes.size == 9)
    }

    test("A 3x3 board with indirect neighbors should have 40 edges") {
        import Shape._
        import NeighborType._
        val board = Board(List(3, 3), Square, Indirect, Square) 
        assert(board.graph.edges.length == 40)
    }

    test("Node (0, 0) on a 3x3 board with indirect neighbors should have 3 edges") {
        import Shape._
        import NeighborType._
        val board = Board(List(3, 3), Square, Indirect, Square) 
        assert(board.graph.nodes(Coordinate(0, 0)).edges.length == 3)
    }

    test("A 3x3 board with direct neighbors should have 24 edges") {
        import Shape._
        import NeighborType._
        val board = Board(List(3, 3), Square, Direct, Square) 
        assert(board.graph.edges.length == 24)
    }

    test("A 4x4 board with indirect neighbors should have 84 edges") {
        import Shape._
        import NeighborType._
        val board = Board(List(4, 4), Square, Indirect, Square) 
        assert(board.graph.edges.length == 84)
    }

    test("A 4x4 board with direct neighbors should have 48 edges") {
        import Shape._
        import NeighborType._
        val board = Board(List(4, 4), Square, Direct, Square) 
        assert(board.graph.edges.length == 48)
    }
}

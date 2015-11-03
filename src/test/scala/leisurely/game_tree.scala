package org.leisurelyscript

import scala.util.{Try, Success, Failure}

import org.scalatest.FunSuite

import MoveAction._
import Direction._


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
        val game = Game().add(List(new Player("Andrew"), new Player("Bill"), new Player("Carol")))
        assert(game.players.all.length == 3)
    }

    // This could be added later for convenience, but is not critical and should be skipped for now.
    ignore("If the game already has players, then game.add(<players>*) " 
         + "should return a new game with the new players appended") {
        val game = Game().add(List(new Player("Andrew"), new Player("Bill"), new Player("Carol")))
        assert(game.players.all.length == 3)
    }

    test("You shouldn't be able to add lists of arbitrary types to the game.") {
        val listOfStrings:List[String] = List("1", "2", "3", "4", "5") 
        val game = Game() 
        intercept[IllegalArgumentException] {
            game.add(listOfStrings)
        }
    }

    test("A 3x3 board should have nine nodes") {
        import Shape._
        import NeighborType._
        val board = Board(List(3, 3), Square, Indirect, Square) 
        assert(board.nodes.size == 9)
    }

    test("A 3x3 board with indirect neighbors should have 40 well formed edges") {
        import Shape._
        import NeighborType._
        val board = Board(List(3, 3), Square, Indirect, Square) 
        assert(board.graph.edges.length == 40)
        val edges = board.graph.nodes(Coordinate(0, 0)).edges
        edges.foreach(e => e.direction match {
            case S => if (!(e.boardNodes._1.coord.x == 0 && e.boardNodes._1.coord.y == 0 
                   && e.boardNodes._2.coord.x == 0 && e.boardNodes._2.coord.y == 1)) {
                       fail 
                   }
           case W => if (!(e.boardNodes._1.coord.x == 0 && e.boardNodes._1.coord.y == 0 
                     && e.boardNodes._2.coord.x == 1 && e.boardNodes._2.coord.y == 0)) {
                         fail
                     }
           case SW => if (!(e.boardNodes._1.coord.x == 0 && e.boardNodes._1.coord.y == 0 
                      && e.boardNodes._2.coord.x == 1 && e.boardNodes._2.coord.y == 1)) {
                          fail
                      }
            case _ => fail
        })
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

    test("A new board should be empty") {
        import Shape._
        import NeighborType._
        val board = Board(List(4, 4), Square, Direct, Square) 
        assert(board.empty())
    }

    test("The user can place a piece on the board") {
        import Shape._
        import NeighborType._
        val board = Board(List(3, 3), Square, Direct, Square) 
        val piece = new Piece("token", new Player("1"), List[LegalMove]())
        board.place(piece, Coordinate(0, 0))

        assert(!board.empty() && !board.full())
    }

    test("Board.full should return true when called on a full board") {
        import Shape._
        import NeighborType._
        val board = Board(List(3, 3), Square, Direct, Square) 
        for (node <- board.graph.nodes) {
            board.place(new Piece("token", new Player("1"), List[LegalMove]()), node._1)
        }
        assert(board.full())
    }

    test("Board.nInARow should detect rows that are 3 long") {
        import Shape._
        import NeighborType._
        val player = new Player("1")
        val piece = new Piece("token", player, List[LegalMove]())
        val horizontalBoard = Board(List(3, 3), Square, Indirect, Square)
            .place(piece, Coordinate(0, 0))
            .flatMap(b => b.place(piece, Coordinate(0, 1)))
            .flatMap(b => b.place(piece, Coordinate(0, 2))) getOrElse fail
        val verticalBoard = Board(List(3, 3), Square, Indirect, Square)
            .place(piece, Coordinate(0, 0))
            .flatMap(b => b.place(piece, Coordinate(1, 0)))
            .flatMap(b => b.place(piece, Coordinate(2, 0))) getOrElse fail
        val diagonalBoard = Board(List(3, 3), Square, Indirect, Square)
            .place(piece, Coordinate(0, 0))
            .flatMap(b => b.place(piece, Coordinate(1, 1)))
            .flatMap(b => b.place(piece, Coordinate(2, 2))) getOrElse fail
        val cornerBoard = Board(List(3, 3), Square, Indirect, Square)
            .place(piece, Coordinate(0, 0))
            //.flatMap(b => b.place(piece, Coordinate(1, 1)))
            .flatMap(b => b.place(piece, Coordinate(2, 2))) getOrElse fail
        val emptyBoard = Board(List(3, 3), Square, Indirect, Square) 

        assert(horizontalBoard.nInARow(3, piece).size > 0 )
        assert(verticalBoard.nInARow(3, piece).size > 0 )
        assert(diagonalBoard.nInARow(3, piece).size > 0 )
        assert(cornerBoard.nInARow(3, piece).size == 0 )
        assert(emptyBoard.nInARow(3, piece).size == 0)
    }

    test("A user should be able to get the number of pieces that occupy a board") {
        import Shape._
        import NeighborType._
        val player = new Player("1")
        val piece = new Piece("token", player, List[LegalMove]())
        val board = Board(List(3, 3), Square, Indirect, Square) 
            .place(piece, Coordinate(0, 0))
            .flatMap(b => b.place(piece, Coordinate(0, 1)))
            .flatMap(b => b.place(piece, Coordinate(0, 2))) getOrElse fail
        assert(board.numberOfPieces() == 3)
    }

    test("Performing a place move should return a new game with a board that has 1 piece in it") {
        import Shape._
        import NeighborType._
        val board = Board(List(3, 3), Square, Indirect, Square) 
        val player = new Player("1")
        val piece = new Piece("token", player, List[LegalMove]())
        val game = Game(board=board) 
        assert(game.applyMove(new Move(piece, player, Push, game.board.graph.nodes(Coordinate(0, 1)))) match {
            case Success(newGame) => newGame.board.numberOfPieces() == 1
            case Failure(_) => fail
        })
    }

    ignore("Edge directions must be unique per node. (can't have two North edges on one node)") {

    }
}

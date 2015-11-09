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
        intercept[IllegalGameAttributeException] {
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
            case S => if (!(e.nodes._1.coord.x == 0 && e.nodes._1.coord.y == 0 
                   && e.nodes._2.coord.x == 0 && e.nodes._2.coord.y == 1)) {
                       fail 
                   }
           case W => if (!(e.nodes._1.coord.x == 0 && e.nodes._1.coord.y == 0 
                     && e.nodes._2.coord.x == 1 && e.nodes._2.coord.y == 0)) {
                         fail
                     }
           case SW => if (!(e.nodes._1.coord.x == 0 && e.nodes._1.coord.y == 0 
                      && e.nodes._2.coord.x == 1 && e.nodes._2.coord.y == 1)) {
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

    test("The user can push a piece into a node in the board") {
        import Shape._
        import NeighborType._
        val board = Board(List(3, 3), Square, Direct, Square) 
        val piece = new Piece("token", new Player("1"), List[LegalMove]())
        val newBoard = board.push(piece, Coordinate(0, 0)).get

        assert(!newBoard.empty() && !newBoard.full())
    }

    test("The user can pop a piece out of a node in the board") {
        import Shape._
        import NeighborType._
        val board = Board(List(3, 3), Square, Direct, Square) 
        val piece = new Piece("token", new Player("1"), List[LegalMove]())
        val firstNewBoard = board.push(piece, Coordinate(0, 0)).get
        val secondNewBoard = firstNewBoard.pop(Coordinate(0, 0)).get

        assert(secondNewBoard.empty())
        assert(!(firstNewBoard eq secondNewBoard))
    }

    test("Board.full should return true when called on a full board") {
        import Shape._
        import NeighborType._
        var board = Board(List(3, 3), Square, Direct, Square) 
        for (node <- board.graph.nodes) {
            board = board.push(new Piece("token", new Player("1"), List[LegalMove]()), node._1).get
        }
        assert(board.full())
    }

    test("Board.nInARow should detect rows that are 3 long") {
        import Shape._
        import NeighborType._
        val player = new Player("1")
        val piece = new Piece("token", player, List[LegalMove]())
        val horizontalBoard = Board(List(3, 3), Square, Indirect, Square)
            .push(piece, Coordinate(0, 0))
            .flatMap(b => b.push(piece, Coordinate(0, 1)))
            .flatMap(b => b.push(piece, Coordinate(0, 2))) getOrElse fail
        val verticalBoard = Board(List(3, 3), Square, Indirect, Square)
            .push(piece, Coordinate(0, 0))
            .flatMap(b => b.push(piece, Coordinate(1, 0)))
            .flatMap(b => b.push(piece, Coordinate(2, 0))) getOrElse fail
        val diagonalBoard = Board(List(3, 3), Square, Indirect, Square)
            .push(piece, Coordinate(0, 0))
            .flatMap(b => b.push(piece, Coordinate(1, 1)))
            .flatMap(b => b.push(piece, Coordinate(2, 2))) getOrElse fail
        val cornerBoard = Board(List(3, 3), Square, Indirect, Square)
            .push(piece, Coordinate(0, 0))
            .flatMap(b => b.push(piece, Coordinate(2, 2))) getOrElse fail
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
            .push(piece, Coordinate(0, 0))
            .flatMap(b => b.push(piece, Coordinate(0, 1)))
            .flatMap(b => b.push(piece, Coordinate(0, 2))) getOrElse fail
        assert(board.numberOfPieces() == 3)
    }

    test("Performing a push move should return a new game with a board that has 1 piece in it") {
        import Shape._
        import NeighborType._
        val board = Board(List(3, 3), Square, Indirect, Square) 
        val player = new Player("1")
        val precondition = (game:Game, move:Move)=>game.board.graph.nodes(move.node.coord).empty()
        val legalMove = new LegalMove(player, precondition, Push)
        val piece = new Piece("token", player, List(legalMove))
        val game = Game(pieces=List(piece), board=board) 
        assert(game.applyMove(new Move(piece, player, Push, game.board.graph.nodes(Coordinate(0, 1)))) match {
            case Success(newGame) => newGame.board.numberOfPieces() == 1
            case Failure(ex) => throw(ex)
        })
    }

    test("Edge directions must be unique per node. (can't have two North edges on one node)") {
        import Shape._
        import NeighborType._
        val board = Board(List(3, 3), Square, Indirect, Square) 
        intercept[IllegalBoardEdgeException] {
            board.graph.add(board.graph.edges(0))
        }
    }

    test("The game object should be able to return the previous, current, and next players.") {
        val game = Game().add(List(new Player("A"), new Player("B"), new Player("C")))
        assert(game.players.current.name == "A")
        assert(game.players.next.name == "B")
        assert(game.players.previous.name == "C")
    }

    test("Ending a player's turn should advance the game to the next player.") {
        val game = Game().add(List(new Player("A"), new Player("B"), new Player("C")))
        assert(game.players.current.name == "A")
        game.players.endTurn()
        assert(game.players.current.name == "B")
    }

    test("Making a move should result in a completely new game with a different board") {
        import Shape._
        import NeighborType._
        val board = Board(List(3, 3), Square, Indirect, Square) 
        val player = new Player("1")
        val precondition = (game:Game, move:Move)=>game.board.graph.nodes(move.node.coord).empty()
        val legalMove = new LegalMove(player, precondition, Push)
        val piece = new Piece("token", player, List(legalMove))
        val game = Game(board=board, pieces=List(piece))
        val firstMove = game.applyMove(new Move(piece, player, Push, game.board.graph.nodes(Coordinate(0, 1)))) match {
            case Success(newGame) => newGame
            case Failure(ex) => throw(ex)
        }
        assert(game.board.graph.nodes(Coordinate(0, 1)).equipment.size == 0)
    }

    test("The precondition in LegalMoves should return false for illegal moves") {
        import Shape._
        import NeighborType._
        val precondition = (game:Game, move:Move)=>game.board.graph.nodes(move.node.coord).empty()
        val board = Board(List(3, 3), Square, Indirect, Square) 
        val player = new Player("1")
        val legalMove = new LegalMove(player, precondition, Push)
        val piece = new Piece("token", player, List[LegalMove](legalMove))
        val move = new Move(piece, player, Push, board.graph.nodes(Coordinate(0, 0)))

        val game = Game().add(board).add(List(piece))
        assert(precondition(game, move))

        val firstMove:Game = game.nonValidatedApplyMove(move) getOrElse fail
        assert(!precondition(firstMove, move))
       
        assert(precondition(game, move))
        assert(game.isMoveLegal(move))
        assert(!firstMove.isMoveLegal(move))
    }

    test("game.isMoveLegal should return true if a move is legal, false otherwise") {
        import Shape._
        import NeighborType._
        val precondition = (game:Game, move:Move)=>game.board.graph.nodes(move.node.coord).empty()
        val board = Board(List(3, 3), Square, Indirect, Square) 
        val player = new Player("1")
        val legalMove = new LegalMove(player, precondition, Push)
        val piece = new Piece("token", player, List[LegalMove](legalMove))
        val move = new Move(piece, player, Push, board.graph.nodes(Coordinate(0, 0)))

        val game = Game().add(board).add(List(piece))
        assert(game.isMoveLegal(move))

        val firstMove:Game = game.applyMove(move).get
        assert(!firstMove.isMoveLegal(move))
    }

    test("The game should only allow legal moves to be applied") {
        import Shape._
        import NeighborType._
        val precondition = (game:Game, move:Move)=>game.board.graph.nodes(move.node.coord).empty()
        val board = Board(List(3, 3), Square, Indirect, Square) 
        val player = new Player("1")
        val legalMove = new LegalMove(player, precondition, Push)
        val piece = new Piece("token", player, List[LegalMove](legalMove))
        val move = new Move(piece, player, Push, board.graph.nodes(Coordinate(0, 0)))

        val game = Game().add(board).add(List(piece))
        assert(game.isMoveLegal(move))

        val firstMove:Game = game.applyMove(move).get
        assert(!firstMove.isMoveLegal(move))
        firstMove.applyMove(move) match {
            case Failure(_:IllegalMoveException) => {}
            case _ => fail
        }
    }

    ignore("The game should recognize when an end condition has been met.") {
        
    }
}

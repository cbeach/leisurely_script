package org.leisurelyscript

import scala.util.{Try, Success, Failure}

import org.scalatest.FunSuite

import org.leisurelyscript.test.util.TestGameFactory

import Direction._
import GameStatus._
import GameResultState._
import MoveAction._
import NeighborType._
import Shape._


class GameTreeTests extends FunSuite {
    test("A player can be created with a name") { 
        val fred = Player("Fred")
        assert(fred.name == "Fred")
    }
    test("Instantiating a player without a name executes successfully") { 
        val noName = Player() 
    }
    test("Creating a player without a name results in a UUID being generated") { 
        val noName = Player() 
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
        val game = Game().add(List(Player("Andrew"), Player("Bill"), Player("Carol")))
        assert(game.players.all.length == 3)
    }

    // This could be added later for convenience, but is not critical and should be skipped for now.
    ignore("If the game already has players, then game.add(<players>*) " 
         + "should return a new game with the new players appended") {
        val game = Game().add(List(Player("Andrew"), Player("Bill"), Player("Carol")))
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
        val board = Board(List(3, 3), Square, Indirect, Square) 
        assert(board.nodes.size == 9)
    }

    test("A 3x3 board with indirect neighbors should have 40 well formed edges") {
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
        val board = Board(List(3, 3), Square, Indirect, Square) 
        assert(board.graph.nodes(Coordinate(0, 0)).edges.length == 3)
    }

    test("A 3x3 board with direct neighbors should have 24 edges") {
        val board = Board(List(3, 3), Square, Direct, Square) 
        assert(board.graph.edges.length == 24)
    }

    test("A 4x4 board with indirect neighbors should have 84 edges") {
        val board = Board(List(4, 4), Square, Indirect, Square) 
        assert(board.graph.edges.length == 84)
    }

    test("A 4x4 board with direct neighbors should have 48 edges") {
        val board = Board(List(4, 4), Square, Direct, Square) 
        assert(board.graph.edges.length == 48)
    }

    test("A new board should be empty") {
        val board = Board(List(4, 4), Square, Direct, Square) 
        assert(board.empty())
    }

    test("The user can push a piece into a node in the board") {
        val board = Board(List(3, 3), Square, Direct, Square) 
        val piece = new Piece("token", Player("1"), List[LegalMove]())
        val newBoard = board.push(piece, Coordinate(0, 0)).get

        assert(!newBoard.empty() && !newBoard.full())
    }

    test("The user can pop a piece out of a node in the board") {
        val board = Board(List(3, 3), Square, Direct, Square) 
        val piece = new Piece("token", Player("1"), List[LegalMove]())
        val firstNewBoard = board.push(piece, Coordinate(0, 0)).get
        val secondNewBoard = firstNewBoard.pop(Coordinate(0, 0)).get

        assert(secondNewBoard.empty())
        assert(!(firstNewBoard eq secondNewBoard))
    }

    test("Board.full should return true when called on a full board") {
        var board = Board(List(3, 3), Square, Direct, Square) 
        for (node <- board.graph.nodes) {
            board = board.push(new Piece("token", Player("1"), List[LegalMove]()), node._1).get
        }
        assert(board.full())
    }

    test("Board.nInARow should detect rows that are 3 long") {
        val player1 = Player("1")
        val piece1 = new Piece("token", player1, List[LegalMove]())
        val horizontalBoard = Board(List(3, 3), Square, Indirect, Square)
            .push(piece1, Coordinate(0, 0))
            .flatMap(b => b.push(piece1, Coordinate(0, 1)))
            .flatMap(b => b.push(piece1, Coordinate(0, 2))) getOrElse fail
        val verticalBoard = Board(List(3, 3), Square, Indirect, Square)
            .push(piece1, Coordinate(0, 0))
            .flatMap(b => b.push(piece1, Coordinate(1, 0)))
            .flatMap(b => b.push(piece1, Coordinate(2, 0))) getOrElse fail
        val diagonalBoard = Board(List(3, 3), Square, Indirect, Square)
            .push(piece1, Coordinate(0, 0))
            .flatMap(b => b.push(piece1, Coordinate(1, 1)))
            .flatMap(b => b.push(piece1, Coordinate(2, 2))) getOrElse fail
        val cornerBoard = Board(List(3, 3), Square, Indirect, Square)
            .push(piece1, Coordinate(0, 0))
            .flatMap(b => b.push(piece1, Coordinate(2, 2))) getOrElse fail
        val emptyBoard = Board(List(3, 3), Square, Indirect, Square) 

        val player2 = Player("2")
        val piece2 = new Piece("token", player2, List[LegalMove]())
        val diagonalBoardAlternatingPlayers = Board(List(3, 3), Square, Indirect, Square)
            .push(piece1, Coordinate(0, 0))
            .flatMap(b => b.push(piece2, Coordinate(1, 1)))
            .flatMap(b => b.push(piece1, Coordinate(2, 2))) getOrElse fail

        assert(horizontalBoard.nInARow(3, piece1).size > 0 )
        assert(verticalBoard.nInARow(3, piece1).size > 0 )
        assert(diagonalBoard.nInARow(3, piece1).size > 0 )
        assert(cornerBoard.nInARow(3, piece1).size == 0 )
        assert(emptyBoard.nInARow(3, piece1).size == 0)
        assert(diagonalBoardAlternatingPlayers.nInARow(3, piece1).size == 0)
    }

    test("A user should be able to get the number of pieces that occupy a board") {
        val player = Player("1")
        val piece = new Piece("token", player, List[LegalMove]())
        val board = Board(List(3, 3), Square, Indirect, Square) 
            .push(piece, Coordinate(0, 0))
            .flatMap(b => b.push(piece, Coordinate(0, 1)))
            .flatMap(b => b.push(piece, Coordinate(0, 2))) getOrElse fail
        assert(board.numberOfPieces() == 3)
    }

    test("Performing a push move should return a new game with a board that has 1 piece in it") {
        val game = TestGameFactory.ticTacToe.startGame()
        assert(game.applyMove(new Move(game.pieces(0), game.players.current, Push, 
        game.board.graph.nodes(Coordinate(0, 1)))) match {
            case Success(newGame) => newGame.board.numberOfPieces() == 1
            case Failure(ex) => throw(ex)
        })
    }

    test("Edge directions must be unique per node. (can't have two North edges on one node)") {
        val board = Board(List(3, 3), Square, Indirect, Square) 
        intercept[IllegalBoardEdgeException] {
            board.graph.add(board.graph.edges(0))
        }
    }

    test("The game object should be able to return the previous, current, and next players.") {
        val game = Game().add(List(Player("A"), Player("B"), Player("C")))
        assert(game.players.current.name == "A")
        assert(game.players.next.name == "B")
        assert(game.players.previous.name == "C")
    }

    test("Ending a player's turn should advance the game to the next player.") {
        val game = Game().add(List(Player("A"), Player("B"), Player("C")))
        assert(game.players.current.name == "A")
        val players = game.players.endTurn
        assert(players.current.name == "B")
    }

    test("Making a move should result in a completely new game with a different board") {
        val game:Game = TestGameFactory.ticTacToe.startGame()
        val firstMove:Game = game.applyMove(new Move(game.pieces(0), game.players.current, Push, 
        game.board.graph.nodes(Coordinate(0, 1)))).get

        assert(!(game eq firstMove))
        assert(!(game.board eq firstMove.board))
    }

    test("The precondition in LegalMoves should return false for illegal moves") {
        val precondition = (game:Game, move:Move)=>game.board.graph.nodes(move.node.coord).empty()
        val board = Board(List(3, 3), Square, Indirect, Square) 
        val player = Player("1")
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
        val game = TestGameFactory.ticTacToe.startGame()
        val move = new Move(game.pieces(0), game.players.current, Push, 
            game.board.graph.nodes(Coordinate(0, 1)))

        assert(game.isMoveLegal(move))

        val firstMove:Game = game.applyMove(move).get
        assert(!firstMove.isMoveLegal(move))
    }

    test("The game should only allow legal moves to be applied") {
        val game = TestGameFactory.ticTacToe.startGame()
        val move = new Move(game.pieces(0), game.players.current, Push, 
            game.board.graph.nodes(Coordinate(0, 1)))
        assert(game.isMoveLegal(move))

        val firstMove:Game = game.applyMove(move).get
        assert(!firstMove.isMoveLegal(move))

        intercept[IllegalMoveException] {
            firstMove.applyMove(move).get
        }
    }

    test("The special player objects (Any, All, SomePlayers, NoPlayer, etc.) should condence into the proper player") {
        val precondition = (game:Game, move:Move)=>game.board.graph.nodes(move.node.coord).empty()
        val board = Board(List(3, 3), Square, Indirect, Square) 
        val players = List(Player("1"), Player("2"), Player("3"), Player("4"), Player("5"))
        val player = players(0)
        val legalMove = new LegalMove(player, precondition, Push)
        val piece = new Piece("token", player, List[LegalMove](legalMove))
        val endCondition = EndCondition(Win, player, (game, player) => {
            game.board.nInARow(3, piece).size > 0
        })
        val game = Game().add(board).add(List(piece)).add(List(endCondition)).add(players)

        val aFewPlayers = List(players(1), players(2), players(3))
        val Previous = new Previous()
        val Current = new Current()
        val Next = new Next()
        val Any = new Any()
        val All = new All()
        val SomePlayers = new SomePlayers(aFewPlayers)
        val NoPlayer = new NoPlayer()

        assert(Previous.valid(game, players(4)) == true)
        assert(Previous.valid(game, players(1)) != true)
        assert(Current.valid(game, players(0)) == true)
        assert(Next.valid(game, players(1)) == true)
        assert(Any.valid(game, players(0)) == true)
        assert(All.valid(game, players) == true)
        assert(SomePlayers.valid(game, aFewPlayers) == true)
        assert(NoPlayer.valid(game, players(0)) == false)
    }
    
    test("The game checks for validity and updates the game status accordingly.") {
        val board = Board(List(3, 3), Square, Indirect, Square) 
        val players = new Players(List(Player("X"), Player("O")))
        val legalMove = new LegalMove(new Any(), (game:Game, move:Move) => {
            game.board.graph.nodes(move.node.coord).empty()
        }, Push)
        val piece = new Piece("token", new Any(), List[LegalMove](legalMove))
        val endConditions = List(
            EndCondition(Win, new Previous(), (game:Game, player:Player) => {
                game.board.nInARow(3, piece).size > 0
            }),
            EndCondition(Tie, new All(), (game:Game, player:Player) => {
                game.board.nInARow(3, piece).size == 0 && game.board.full()
            })
        )

        val game = Game()
        val gameWithPlayers = game.add(players)
        val gameWithBoard = gameWithPlayers.add(board)
        val gameWithPieces = gameWithBoard.add(List(piece))
        val gameWithEndCondition = gameWithPieces.add(endConditions)

        intercept[IllegalGameException] {
            game.startGame()
        }
        intercept[IllegalGameException] {
            gameWithPlayers.startGame()
        }
        intercept[IllegalGameException] {
            gameWithBoard.startGame()
        }
        intercept[IllegalGameException] {
            gameWithPieces.startGame()
        }
        gameWithEndCondition.startGame()
    }

    test("New graphs don't change as the game progresses") {
        val move0:Game = TestGameFactory.ticTacToe.startGame()
        val move1 = move0.applyMove(Move(move0.pieces(0).copy(move0.players.current), move0.players.current, Push, move0.board.graph.nodes(Coordinate(0, 0)))).get
        val move2 = move1.applyMove(Move(move1.pieces(0).copy(move1.players.current), move1.players.current, Push, move1.board.graph.nodes(Coordinate(0, 1)))).get
        val move3 = move2.applyMove(Move(move2.pieces(0).copy(move2.players.current), move2.players.current, Push, move2.board.graph.nodes(Coordinate(0, 2)))).get
        val move4 = move3.applyMove(Move(move3.pieces(0).copy(move3.players.current), move3.players.current, Push, move3.board.graph.nodes(Coordinate(1, 0)))).get
        val move5 = move4.applyMove(Move(move4.pieces(0).copy(move4.players.current), move4.players.current, Push, move4.board.graph.nodes(Coordinate(1, 1)))).get
        val move6 = move5.applyMove(Move(move5.pieces(0).copy(move5.players.current), move5.players.current, Push, move5.board.graph.nodes(Coordinate(1, 2)))).get
        val move7 = move6.applyMove(Move(move6.pieces(0).copy(move6.players.current), move6.players.current, Push, move6.board.graph.nodes(Coordinate(2, 0)))).get
        val move8 = move7.applyMove(Move(move7.pieces(0).copy(move7.players.current), move7.players.current, Push, move7.board.graph.nodes(Coordinate(2, 1)))).get
        val move9 = move8.applyMove(Move(move8.pieces(0).copy(move8.players.current), move8.players.current, Push, move8.board.graph.nodes(Coordinate(2, 2)))).get

        val boardToString:(Board) => String = (board:Board) => {
            val strList = for (i <- 0 until 3; j <- 0 until 3) yield {
                val equipment = board.graph.nodes(Coordinate(i, j)).equipment
                if (equipment.size == 0) {
                    "-" 
                } else {
                    equipment(0) match {
                        case piece:Piece => piece.owner.name
                    }
                }
            }
            s"\n ${strList.slice(0, 3).mkString("")} \n ${strList.slice(3, 6).mkString("")} \n ${strList.slice(6, 9).mkString("")}"
        }

        assert(move0.board.nInARow(3, move0.pieces(0).copy(move0.players.all(0))).size == 0)
        assert(move1.board.nInARow(3, move1.pieces(0).copy(move1.players.all(0))).size == 0)
        assert(move2.board.nInARow(3, move2.pieces(0).copy(move2.players.all(0))).size == 0)
        assert(move3.board.nInARow(3, move3.pieces(0).copy(move3.players.all(0))).size == 0)
        assert(move4.board.nInARow(3, move4.pieces(0).copy(move4.players.all(0))).size == 0)
        assert(move5.board.nInARow(3, move5.pieces(0).copy(move5.players.all(0))).size == 0)
        assert(move6.board.nInARow(3, move6.pieces(0).copy(move6.players.all(0))).size == 0)
        assert(move7.board.nInARow(3, move7.pieces(0).copy(move7.players.all(0))).size == 1)
        assert(move8.board.nInARow(3, move8.pieces(0).copy(move8.players.all(0))).size == 1)

        assert(move0.gameResult.get.result == Pending)
        assert(move1.gameResult.get.result == Pending)
        assert(move2.gameResult.get.result == Pending)
        assert(move3.gameResult.get.result == Pending)
        assert(move4.gameResult.get.result == Pending)
        assert(move5.gameResult.get.result == Pending)
        assert(move6.gameResult.get.result == Pending)
        assert(move7.gameResult.get.result == Win)

        val playerX = move0.players.all(0)
        val playerO = move0.players.all(1)

        assert(move0.players.current == playerX)
        assert(move1.players.current == playerO)
        assert(move2.players.current == playerX)
        assert(move3.players.current == playerO)
        assert(move4.players.current == playerX)
        assert(move5.players.current == playerO)
        assert(move6.players.current == playerX)
        assert(move7.players.current == playerO)

        move9.gameResult match {
            case Some(gameResult:GameResult) => {
                info(s"${gameResult.result}")
                assert(gameResult.result == Tie)
            }
            case None => fail
        }

    }
}

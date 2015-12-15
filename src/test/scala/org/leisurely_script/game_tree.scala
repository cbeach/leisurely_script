package org.leisurelyscript.test.suites

import scala.util.{Try, Success, Failure}

import org.scalatest.FunSuite


import org.leisurelyscript.gdl._
import org.leisurelyscript.gdl.ImplicitDefs.Views.Game._
import org.leisurelyscript.test.util.GameUtilities.TicTacToeUtilities._
import org.leisurelyscript.repository.LocalStaticRepository

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
    val player1 = Player("1")
    val piece = new PieceRule("token", player1, List[LegalMove]())
    val newBoard = board.push(piece.getPhysicalPiece(player1), Coordinate(0, 0)).get

    assert(!newBoard.empty() && !newBoard.full())
  }

  test("The user can pop a piece out of a node in the board") {
    val board = Board(List(3, 3), Square, Direct, Square)
    val player1 = Player("1")
    val piece = new PieceRule("token", player1, List[LegalMove]())
    val firstNewBoard = board.push(piece.getPhysicalPiece(player1), Coordinate(0, 0)).get
    val secondNewBoard = firstNewBoard.pop(Coordinate(0, 0)).get

    assert(secondNewBoard.empty())
    assert(!(firstNewBoard eq secondNewBoard))
  }

  test("Board.full should return true when called on a full board") {
    var board = Board(List(3, 3), Square, Direct, Square)
    val player1 = Player("1")
    for (node <- board.graph.nodes) {
      board = board.push(PhysicalPiece("token", player1), node._1).get
    }
    assert(board.full())
  }

  test("Board.nInARow should detect rows that are 3 long") {
    val player1 = Player("1")
    val piece1 = new PhysicalPiece("token", player1)
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
    val piece2 = new PhysicalPiece("token", player2)
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
    val piece = new PhysicalPiece("token", player)
    val board = Board(List(3, 3), Square, Indirect, Square)
      .push(piece, Coordinate(0, 0))
      .flatMap(b => b.push(piece, Coordinate(0, 1)))
      .flatMap(b => b.push(piece, Coordinate(0, 2))) getOrElse fail
    assert(board.numberOfPieces() == 3)
  }

  test("Performing a push move should return a new game with a board that has 1 piece in it") {
    val game = LocalStaticRepository.load("TicTacToe") match {
      case Success(tTT) => tTT.startGame()
      case Failure(ex) => fail
    }
    val xPiece = game.pieces(0).getPhysicalPiece(game.players.all(0))
    assert(game.applyMove(new Move(xPiece, game.players.current, Push,
    game.board.graph.nodes(Coordinate(0, 1)))) match {
      case Success(newGame:Game) => newGame.board.numberOfPieces() == 1
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
    val game:Game = LocalStaticRepository.load("TicTacToe") match {
      case Success(tTT) => tTT.startGame()
      case Failure(ex) => fail
    }
    val xPiece = game.pieces(0).getPhysicalPiece(game.players.all(0))
    val firstMove:Game = game.applyMove(new Move(xPiece, game.players.current, Push,
    game.board.graph.nodes(Coordinate(0, 1)))).get

    assert(!(game eq firstMove))
    assert(!(game.board eq firstMove.board))
  }

  test("The precondition in LegalMoves should return false for illegal moves") {
    val precondition = (game:Game, move:Move)=>game.board.graph.nodes(move.node.coord).empty()
    val board = Board(List(3, 3), Square, Indirect, Square)
    val player = Player("1")
    val legalMove = new LegalMove(player, precondition, Push)
    val piece = new PieceRule("token", player, List[LegalMove](legalMove))
    val move = new Move(piece.getPhysicalPiece(player), player, Push, board.graph.nodes(Coordinate(0, 0)))

    val game = Game().add(board).add(List(piece))
    assert(precondition(game, move))

    val firstMove:Game = game.nonValidatedApplyMove(move) getOrElse fail
    assert(!precondition(firstMove, move))

    assert(precondition(game, move))
    assert(game.isMoveLegal(move))
    assert(!firstMove.isMoveLegal(move))
  }

  test("game.isMoveLegal should return true if a move is legal, false otherwise") {
    val game = LocalStaticRepository.load("TicTacToe") match {
      case Success(tTT) => tTT.startGame()
      case Failure(ex) => fail
    }
    val move = new Move(game.pieces(0).getPhysicalPiece(game.players.current), game.players.current, Push,
      game.board.graph.nodes(Coordinate(0, 1)))

    assert(game.isMoveLegal(move))

    val firstMove:Game = game.applyMove(move).get
    assert(!firstMove.isMoveLegal(move))
  }

  test("The game should only allow legal moves to be applied") {
    val game = LocalStaticRepository.load("TicTacToe") match {
      case Success(tTT) => tTT.startGame()
      case Failure(ex) => fail
    }
    val move = new Move(game.pieces(0).getPhysicalPiece(game.players.current), game.players.current, Push,
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
    val piece = new PieceRule("token", player, List[LegalMove](legalMove))
    val endCondition = EndCondition(Win, player, (game, player) => {
      game.board.nInARow(3, piece.getPhysicalPiece(player)).size > 0
    })
    val game = Game().add(board).add(List(piece)).add(List(endCondition)).add(players)

    val aFewPlayers = Set(players(1), players(2), players(3))
    val previous = PreviousPlayer
    val current = CurrentPlayer
    val next = NextPlayer
    val any = AnyPlayer
    val all = AllPlayers
    val somePlayers = SomePlayers(aFewPlayers)
    val noPlayer = NoPlayer

    assert(previous.playersValid(game, players(4)) == true)
    assert(previous.playersValid(game, players(1)) != true)
    assert(current.playersValid(game, players(0)) == true)
    assert(next.playersValid(game, players(1)) == true)
    assert(any.playersValid(game, players(0)) == true)
    assert(all.playersValid(game, players.toSet) == true)
    assert(somePlayers.playersValid(game, aFewPlayers) == true)
    assert(noPlayer.playersValid(game, players(0)) == false)
  }

  test("The game checks for validity and updates the game status accordingly.") {
    val board = Board(List(3, 3), Square, Indirect, Square)
    val players = new Players(List(Player("X"), Player("O")))
    val legalMove = new LegalMove(AnyPlayer, (game:Game, move:Move) => {
      game.board.graph.nodes(move.node.coord).empty()
    }, Push)
    val piece = new PieceRule("token", AnyPlayer, List[LegalMove](legalMove))
    val endConditions = List(
      EndCondition(Win, PreviousPlayer, (game:Game, player:Player) => {
        game.board.nInARow(3, piece.getPhysicalPiece(player)).size > 0
      }),
      EndCondition(Tie, AllPlayers, (game:Game, player:Player) => {
        game.board.nInARow(3, piece.getPhysicalPiece(player)).size == 0 && game.board.full()
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
    val tie = movesFromTiedGame(None)

    val xPiece = tie(0).pieces(0).getPhysicalPiece(tie(0).players.all(0))
    val oPiece = tie(0).pieces(0).getPhysicalPiece(tie(0).players.all(1))

    assert(tie(0).board.nInARow(3, xPiece).size == 0)
    assert(tie(1).board.nInARow(3, xPiece).size == 0)
    assert(tie(2).board.nInARow(3, xPiece).size == 0)
    assert(tie(3).board.nInARow(3, xPiece).size == 0)
    assert(tie(4).board.nInARow(3, xPiece).size == 0)
    assert(tie(5).board.nInARow(3, xPiece).size == 0)
    assert(tie(6).board.nInARow(3, xPiece).size == 0)
    assert(tie(7).board.nInARow(3, xPiece).size == 0)
    assert(tie(8).board.nInARow(3, xPiece).size == 0)
    assert(tie(9).board.nInARow(3, xPiece).size == 0)

    assert(tie(0).board.nInARow(3, oPiece).size == 0)
    assert(tie(1).board.nInARow(3, oPiece).size == 0)
    assert(tie(2).board.nInARow(3, oPiece).size == 0)
    assert(tie(3).board.nInARow(3, oPiece).size == 0)
    assert(tie(4).board.nInARow(3, oPiece).size == 0)
    assert(tie(5).board.nInARow(3, oPiece).size == 0)
    assert(tie(6).board.nInARow(3, oPiece).size == 0)
    assert(tie(7).board.nInARow(3, oPiece).size == 0)
    assert(tie(8).board.nInARow(3, oPiece).size == 0)
    assert(tie(9).board.nInARow(3, oPiece).size == 0)

    assert(tie(0).gameResult.get.result == Pending)
    assert(tie(1).gameResult.get.result == Pending)
    assert(tie(2).gameResult.get.result == Pending)
    assert(tie(3).gameResult.get.result == Pending)
    assert(tie(4).gameResult.get.result == Pending)
    assert(tie(5).gameResult.get.result == Pending)
    assert(tie(6).gameResult.get.result == Pending)
    assert(tie(7).gameResult.get.result == Pending)
    assert(tie(8).gameResult.get.result == Pending)
    assert(tie(9).gameResult.get.result == Tie)

    val playerX = tie(0).players.all(0)
    val playerO = tie(0).players.all(1)

    assert(tie(0).players.current == playerX)
    assert(tie(1).players.current == playerO)
    assert(tie(2).players.current == playerX)
    assert(tie(3).players.current == playerO)
    assert(tie(4).players.current == playerX)
    assert(tie(5).players.current == playerO)
    assert(tie(6).players.current == playerX)
    assert(tie(7).players.current == playerO)
    assert(tie(8).players.current == playerX)
    assert(tie(9).players.current == playerO)

    tie(9).gameResult match {
      case Some(gameResult:GameResult) => {
        assert(gameResult.result == Tie)
      }
      case None => fail
    }
  }
  test("Legal moves should not be available after the end of the game") {
    val tie = movesFromTiedGame(None)
    assert(tie.last.legalMoves(tie.last.players.current).isEmpty)

    val fastXWin = movesFromFastestXWin(None).last
    assert(fastXWin.legalMoves(fastXWin.players.current).isEmpty)
  }
}

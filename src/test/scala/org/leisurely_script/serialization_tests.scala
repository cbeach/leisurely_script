package org.leisurely_script

import java.io.{ByteArrayOutputStream, ObjectOutputStream}

import org.leisurelyscript.gdl.GameResultState._
import org.leisurelyscript.gdl.ImplicitDefs.TypeClasses.LeisurelyScriptJSONProtocol._
import org.leisurelyscript.gdl.MoveAction._
import org.leisurelyscript.gdl.NeighborType._
import org.leisurelyscript.gdl.Shape._
import org.leisurelyscript.gdl._
import org.scalatest.FunSuite
import spray.json._

/**
  * Created by mcsmash on 12/9/15.
  */
class SerializationTests extends FunSuite {
  test("Player serialization") {
    assert(Player("Frank").toJson.convertTo[Player].name == "Frank")
  }
  test("Serialize anonymous functions") {
    val functionObj:(Int, Int)=>Boolean = (int1:Int, int2:Int) => {
      int1 < int2
    }
    val byteStream = new ByteArrayOutputStream()
    val objStream = new ObjectOutputStream(byteStream).writeObject(functionObj)
    val encoder:sun.misc.BASE64Encoder = new sun.misc.BASE64Encoder()
    val base64String = encoder.encode(byteStream.toByteArray())
  }
  test("json serialization for (Game, Move) => Boolean") {
    val func1:(Game, Move)=>Boolean = (game:Game, move:Move) => {
      true
    }
    val json = func1.toJson
    val func2:(Game, Move)=>Boolean = json.convertTo[(Game, Move)=>Boolean]
    assert(func2(null, null))
  }
  test("PlayerValidator serialization") {
    val playerNames = List[String]("Anne", "Bob", "Carol")
    val players = playerNames.map(name => {
      Player(name)
    })
    var validator:PlayerValidator = AllPlayers
    assert(validator.toJson.convertTo[PlayerValidator] == AllPlayers)

    validator = AnyPlayer
    assert(validator.toJson.convertTo[PlayerValidator] == AnyPlayer)

    validator = PreviousPlayer
    assert(validator.toJson.convertTo[PlayerValidator] == PreviousPlayer)

    validator = CurrentPlayer
    assert(validator.toJson.convertTo[PlayerValidator] == CurrentPlayer)

    validator = NextPlayer
    assert(validator.toJson.convertTo[PlayerValidator] == NextPlayer)

    validator = NoPlayer
    assert(validator.toJson.convertTo[PlayerValidator] == NoPlayer)

    val playerSubset = SubsetOfPlayers(players.toSet.filter(_ != Player("Bob")))
    validator = playerSubset
    val result = validator.toJson.convertTo[PlayerValidator]
    assert(result == playerSubset)

    val somePlayers = SomePlayers(players.toSet)
    validator = somePlayers
    assert(validator.toJson.convertTo[PlayerValidator] == somePlayers)

    players.foreach(player => {
      validator = player
      assert(validator.toJson.convertTo[PlayerValidator] == player)
    })
  }
  test("ConcretelyKnownPlayer serialization") {
    val playerNames = List[String]("Anne", "Bob", "Carol")
    val players = playerNames.map(name => {
      Player(name)
    })

    var validator:ConcretelyKnownPlayer = NoPlayer
    assert(validator.toJson.convertTo[PlayerValidator] == NoPlayer)

    val subsetOfPlayerObjects = players.toSet.filter(_ != Player("Bob"))
    val playerSubset = SubsetOfPlayers(subsetOfPlayerObjects)

    validator = playerSubset
    val result = validator.toJson.convertTo[PlayerValidator]
    assert(result == playerSubset)

    val somePlayers = SomePlayers(players.toSet)
    validator = somePlayers
    assert(validator.toJson.convertTo[PlayerValidator] == somePlayers)

    players.foreach(player => {
      validator = player
      assert(validator.toJson.convertTo[PlayerValidator] == player)
    })
  }
  test("Coordinate serialization") {
    val coordJson = Coordinate(1, 2).toJson
    assert(coordJson.convertTo[Coordinate].x == 1)
    assert(coordJson.convertTo[Coordinate].y == 2)
  }
  test("BoardNode serialization") {
    val node = new BoardNode(Coordinate(0, 0))
    assert(node.toJson.convertTo[BoardNode] == node)
  }
  test("BoardEdge serialization") {
    val node1 = new BoardNode(Coordinate(0, 0))
    val node2 = new BoardNode(Coordinate(0, 0))
    val edge = new BoardEdge((node1, node2), Direction.N)
    assert(edge.toJson.convertTo[BoardEdge] == edge)
  }
  test("Graph serialization") {
    val graph1 = Board(List(3, 3), Square, Indirect, Square).graph
    val graph2 = Board(List(3, 3), Square, Indirect, Square).graph
    assert(graph1 == graph2)
    assert(graph1.toJson.convertTo[Graph] == graph1)
  }
  test("LegalMove serialization") {
    val condition = (game:Game, move:Move) => {
      true
    }
    val legalMove = new LegalMove(AnyPlayer, condition, Push, condition)
    assert(legalMove.toJson.convertTo[LegalMove].owner == legalMove.owner)
    assert(legalMove.toJson.convertTo[LegalMove].precondition(null, null) == legalMove.precondition(null, null))
    assert(legalMove.toJson.convertTo[LegalMove].action == legalMove.action)
    assert(legalMove.toJson.convertTo[LegalMove].postcondition(null, null) == legalMove.postcondition(null, null))

    val ticTacToeLegalMove = new LegalMove(CurrentPlayer, (game:Game, move:Move) => {
      game.board.graph.nodesByCoord(move.node.coord).empty()
    }, Push)
    ticTacToeLegalMove.toJson.convertTo[LegalMove]
  }
  test("PieceRule serialization") {
    val condition = (game:Game, move:Move) => {
      true
    }
    val legalMove = new LegalMove(AnyPlayer, condition, Push, condition)
    val pieceRule = PieceRule("token", AnyPlayer, List(legalMove))
    val converted = pieceRule.toJson.convertTo[PieceRule]
    assert(converted.name == pieceRule.name)
    assert(converted.owner == pieceRule.owner)
    assert(converted.legalMoves(0).owner == legalMove.owner)
    assert(converted.legalMoves(0).precondition(null, null) == legalMove.precondition(null, null))
    assert(converted.legalMoves(0).action == legalMove.action)
    assert(converted.legalMoves(0).postcondition(null, null) == legalMove.postcondition(null, null))
  }
  test("EndCondition serialization") {
    val eC = EndCondition(Win, PreviousPlayer, (game:Game, player:Player) => {
      true
    })
    val converted = eC.toJson.convertTo[EndCondition]
    assert(converted.result == eC.result)
    assert(converted.affectedPlayer == eC.affectedPlayer)
    assert(converted.conditionMet(null, null) == eC.conditionMet(null, null))
    val ticTacToeEndConditions = List(
      EndCondition(Win, PreviousPlayer, (game:Game, player:Player) => {
        game.board.nInARow(3, game.pieces(0).getPhysicalPiece(player)).size > 0
      }),
      EndCondition(Tie, AllPlayers, (game:Game, player:Player) => {
        game.board.nInARow(3, game.pieces(0).getPhysicalPiece(player)).size == 0 && game.board.full()
      })
    )
    ticTacToeEndConditions.toJson
    ticTacToeEndConditions.toJson.convertTo[List[EndCondition]]
  }
  test("Board serialization") {
    val board = Board(List(3, 3), Square, Indirect, Square)
    val converted = board.toJson.convertTo[Board]
    assert(converted == board)
  }
  test("Players serialization") {
    val playerNames = List[String]("Anne", "Bob", "Carol", "Dave", "Fran")
    val playerList = playerNames.map(name => {
      Player(name)
    })
    val players = new Players(playerList)
    val converted = players.toJson.convertTo[Players]
    assert(converted == players)
  }
  test("TicTacToe serialization") {
    import org.leisurelyscript.repository.LocalStaticRepository
    import org.leisurelyscript.repository.LocalStaticRepository.availableGames._
    import org.leisurelyscript.test.util.GameUtilities.TicTacToeUtilities._

    val ticTacToe = LocalStaticRepository.load(TicTacToe).get
    val converted = ticTacToe.toJson.convertTo[Game]
    val tiedGame:List[Game] = movesFromTiedGame(converted)
  }
}

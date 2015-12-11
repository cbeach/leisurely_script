package org.leisurelyscript.test.suites

import java.io.{ObjectOutputStream, ByteArrayOutputStream}
import scala.util.{Try, Success, Failure}
import scala.collection.mutable.Queue

import org.scalatest.FunSuite
import spray.json._
import DefaultJsonProtocol._

import org.leisurelyscript.gdl._
import org.leisurelyscript.gdl.ImplicitDefs.TypeClasses.LeisurelyScriptJSONProtocol._
import GameStatus._
import org.leisurelyscript.test.util.GameUtilities.TicTacToeUtilities._
import org.leisurelyscript.repository.LocalStaticRepository
import org.leisurelyscript.repository.GameFactory.AvailableGames._

/**
  * Created by mcsmash on 12/9/15.
  */
class SerializationTests extends FunSuite {
    test("Test Player serialization") {
        assert(Player("Frank").toJson.convertTo[Player].name == "Frank")
    }
    test("Test Coordinate serialization") {
        val coordJson = Coordinate(1, 1).toJson
        assert(coordJson.convertTo[Coordinate].x == 1)
        assert(coordJson.convertTo[Coordinate].y == 2)
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
    test("Test json serialization for (Game, Move) => Boolean") {
        val func1:(Game, Move)=>Boolean = (game:Game, move:Move) => {
            true
        }
        val json = func1.toJson
        val func2:(Game, Move)=>Boolean = json.convertTo[(Game, Move)=>Boolean]
        assert(func2(null, null))
    }
    test("Test PlayerValidator") {
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
}

package org.leisurely_script.gdl

import org.leisurely_script.implementation.Game

import java.io.PrintWriter

import com.ning.http.client.RequestBuilder

import scala.util.{Try, Success, Failure}
import scala.collection.mutable.Queue
import spray.json._
import DefaultJsonProtocol._
import org.leisurelyscript.gdl.ImplicitDefs.TypeClasses.LeisurelyScriptJSONProtocol._
import dispatch._, Defaults._

import org.leisurely_script.gdl._
import GameStatus._
import org.leisurely_script.repository.LocalStaticRepository
import org.leisurely_script.repository.GameFactory.AvailableGames._


object Main {
  def gameRepositoryService(game:String): Req = {
    host("127.0.0.1", 8080) / game
  }
  def main(args:Array[String]) {
    val ticTacToe = LocalStaticRepository.load(TicTacToe).get.startGame()
    val request:Req = gameRepositoryService("TicTacToe")
      .PUT
      .setContentType("application/json", "UTF-8")
      .setBody(ticTacToe.toJson.toString)
    Http(request OK as.String)
  }
}

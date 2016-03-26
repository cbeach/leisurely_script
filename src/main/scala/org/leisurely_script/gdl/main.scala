package org.leisurely_script.gdl

import java.io.PrintWriter
import com.ning.http.client.RequestBuilder

import dispatch._, Defaults._

import org.leisurely_script.implementation.Game
import org.leisurely_script.repository.LocalStaticRepository
import org.leisurely_script.repository.GameFactory.AvailableGames._


object Main {
  def gameRepositoryService(game:String): Req = {
    host("127.0.0.1", 8080) / game
  }
  def main(args:Array[String]) {
    val ticTacToe = LocalStaticRepository.load(TicTacToe).get
    val request:Req = gameRepositoryService("TicTacToe")
      .PUT
      .setContentType("application/json", "UTF-8")
      //.setBody(ticTacToe.toJson.toString)
    Http(request OK as.String)
  }
}

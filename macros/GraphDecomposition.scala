package beachc.metaprogrammers

import scala.annotation.StaticAnnotation
import scala.meta._

object DecomposeGraph {
  def apply(graph: Tree): Any = {
    graph match {
      case q"$expr(...$aexprssnel)"  => {
        println("graph expression")
        println("expression")
        println(expr)
        println("aexprssnel")
        println(aexprssnel)
      }
      case _ => {
        println("where's my graph expression?")
      }
    }
  }
}

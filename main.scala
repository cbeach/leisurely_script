import beachc.metaprogrammers.GameStateGenerator
import beachc.ast._
import scala.meta._


object astTest {
  def main(args: Array[String]): Unit = {
    val ttt = new java.io.File("./tttImpl.lsrl").parse[Source].get
    def r(children: Seq[Tree]): Unit = {
      children.foreach((child) => {
        child.children match {
          case h :: t => {
            r(h.children)
            r(t)
            h match {
              case Term.Name("GameRuleSet") => println("found the gRS")
              case _ => {}
            }
          }
          case h :: Nil => {
            //println(s"leaf: ${h}")
          }
          case Nil => { }
        }
      })
    }
    r(ttt.children)
  }
}

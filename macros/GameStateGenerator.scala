package beachc.metaprogrammers

import scala.annotation.StaticAnnotation
import scala.meta._

private object StructureRegistry {
  var structures: List[Defn] = List()
  def add(defn: Defn) = {
    structures = defn :: structures
  }
}

class GameStateGenerator extends StaticAnnotation {
  inline def apply(defn: Defn) = meta {
    println(defn.show[Syntax])
    defn
  }
}

//class RegisterStructure extends StaticAnnotation {
//  inline def apply(defn: Defn) = meta {
//    StructureRegistry.add(defn)
//    defn
//  }
//}

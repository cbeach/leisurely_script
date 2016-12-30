package beachc.printers

import scala.annotation.StaticAnnotation
import scala.meta._


class PrintStructure extends StaticAnnotation {
  inline def apply(tree: Any): Any = meta {
    println(tree.show[Structure])
    tree
  }
}

class PrintSyntax extends StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    println(defn.show[Syntax])
    defn
  }
}


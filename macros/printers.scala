package beachc.printers

import scala.annotation.StaticAnnotation
import scala.meta._


class PrintStructure extends StaticAnnotation {
  inline def apply(tree: Tree) = meta {
    println(tree.show[Structure])
    tree
  }
}

class PrintSyntax extends StaticAnnotation {
  inline def apply(defn: Defn) = meta {
    println(defn.show[Syntax])
    defn
  }
}


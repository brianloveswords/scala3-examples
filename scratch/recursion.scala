import scala.annotation.tailrec
import scala.annotation.targetName

enum Tree:
  case Node(value: Int, left: Tree = Leaf, right: Tree = Leaf)
  case Leaf

import Tree.*

val tree = Node(
  value = 10,
  left = Node(
    value = 5,
    left = Node(3),
    right = Node(7)
  ),
  right = Node(
    value = 15,
    left = Node(12),
    right = Node(17)
  )
)

def sumTree(tree: Tree): Int = tree match
  case Leaf                     => 0
  case Node(value, left, right) => value + sumTree(left) + sumTree(right)

enum Field(name: String, typeName: String):
  case Scalar(name: String, typeName: String) extends Field(name, typeName)
  case Struct(name: String, fields: List[Field]) extends Field(name, "struct")

import Field.*

val fields = List(
  Scalar(
    name = "name",
    typeName = "string"
  ),
  Scalar(
    name = "age",
    typeName = "int"
  ),
  Struct(
    name = "address",
    fields = List(
      Scalar(
        name = "street",
        typeName = "string"
      ),
      Scalar(
        name = "number",
        typeName = "int"
      )
    )
  )
)

@main def run() =
  println(fields)

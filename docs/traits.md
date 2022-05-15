# Traits


`traits` are Scala's way of representing interfaces.

```scala
trait Geometry:
  def area: Double
  def perimeter: Double

case class Rectangle(
  width: Double,
  height: Double,
) extends Geometry:
  def area: Double =
    width * height

  def perimeter: Double =
    2 * (width + height)

case class Circle(
  radius: Double,
) extends Geometry:
  def area: Double =
    math.Pi * radius * radius

  def perimeter: Double =
    2 * math.Pi * radius

def measure(shape: Geometry): Unit =
  println(s"Shape: ${shape}")
  println(s"Area: ${shape.area}")
  println(s"Perimeter: ${shape.perimeter}")

val r = Rectangle(width = 10, height = 20)
val c = Circle(radius = 5)

measure(r)
measure(c)
```


## Case class parameters as member implemenations

The `name` fields in the case classes below count as implementations for the
`name` method in the `Humanoid` trait.

```scala
// if we leave off the val, the `emoji`
// field is private and cannot be accessed
// outside of the trait
trait Humanoid(val emoji: String):
  def name: String

  def shoutHello: String =
    s"${emoji}: Hello, I'm ${name}".toUpperCase


case class Person(
  name: String,
  age: Int,
) extends Humanoid("👤")

case class Robot(
  name: String,
) extends Humanoid("🤖")


val p = Person("John", age = 42)
val r = Robot("Bender")

println(p.shoutHello)
println(r.shoutHello)
println(r.emoji)
```

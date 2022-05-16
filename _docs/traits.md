# Traits


`traits` are Scala's tool for describing interfaces, also known as protocols
in Python, and sharing implementations.

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
// If we leave off the val, the `emoji`
// field is private and cannot be accessed
// outside of the trait
trait Humanoid(val emoji: String):
  def name: String

  // We can provide default implementations so
  // long as they only reference properties known
  // to the trait
  def shoutHello: String =
    s"${emoji}: Hello, I'm ${name}".toUpperCase

// We can define companion objects for traits,
// and if we define an `apply` method, we can
// choose a "default" instance for the trait
// and let users instantiate that without having
// to care about the underlying concrete type.
object Humanoid:
  def apply(name: String): Humanoid =
    Robot(name)

case class Person(
  name: String,
  age: Int,
) extends Humanoid("👤")

case class Robot(
  name: String,
) extends Humanoid("🤖")


val p = Person("John", age = 42)
val r = Humanoid("Bender")

println(p.shoutHello)
println(r.shoutHello)
println(r.emoji)
```

## Programming to interfaces

Whenever possible, functions should take traits as parameters, not concrete
implementations. If there is a hierarchy of traits, functions should take the
trait closest to the top of the hierarchy—the one with the least "power"
necessary to perform the task.

We'll come back to `traits` when we talk about implementing services that need
to interact with the outside world, like HTTP and SQL clients. Making sure
that functions that need to use the service take the trait, not the concrete
implementation, will allow us to test our code by supplying fakes during
testing that do not have to use the network.

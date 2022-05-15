// if we leave off the val, the `emoji`
// field remains private
trait Humanoid(emoji: String):
  def name: String

  def shoutHello: String =
    s"${emoji}: Hello, I'm ${name}".toUpperCase

case class Person(
    name: String,
    age: Int
) extends Humanoid("👤")

case class Robot(
    name: String
) extends Humanoid("🤖")

val p = Person("John", age = 42)
val r = Robot("Bender")

println(p.shoutHello)
println(r.shoutHello)
println(r.emoji)

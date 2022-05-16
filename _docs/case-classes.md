# Case Classes

Case classes are named bags of data. They are very similar to structs in Go, and dataclasses in Python.

```scala
// import everything from the `duration` namespace,
// including the `FiniteDuration` type and the methods
// that get attached to numbers that allow us to do e.g.
// `1.second`
import scala.concurrent.duration.*

case class Sample(
  name: String,
  duration: FiniteDuration,
  sampleRate: Int,
  bitDepth: Int,
  channels: Int,
):
  /** Returns the size of the sample in bytes */
  lazy val sizeBytes: Long =
    (duration.toMillis
      * sampleRate / 1000.0 / 8
      * bitDepth
      * channels
    ).toLong

val kick = Sample("kick", 780.milliseconds, 44100, 16, 2)
println(kick.sizeBytes)
```

Case class members should largely represent data that is derivable from the input parameters. For example, you should not have a `saveToDisk` method on a case class because the case class itself should not be responsible for *how* to communicate with the outside world—that is the responsibility of a service that uses the case class.

In the language of [Grokking Simplicity](https://www.manning.com/books/grokking-simplicity), you should only use data and calculations in case classes, and never actions.

When there's more than a few parameters it's useful to name them when you're
instantiating the case class.

```scala
//{
import scala.concurrent.duration.*
case class Sample(
  name: String,
  duration: FiniteDuration,
  sampleRate: Int,
  bitDepth: Int,
  channels: Int,
)
//}

val hat = Sample(
  channels = 2,
  sampleRate = 44100,
  duration = 450.milliseconds,
  bitDepth = 16,
  name = "hat",
)
```

This allows you to refactor the ordering in the case class later without
breaking the code

For example, if we decided to swap the order of `bitDepth` and `sampleRate` in
the case class, the `kick` sample would be broken because the it would be
interpreted as having a sampleRate of 16 and a bit depth of 44,100 bits


## Immutability

Case classes parameters get exposed as members, and are immutable by default.

```scala sc:fail
case class Person(name: String, age: Int)
val me = Person("Brian", 36)
me.age = 37
```

While you _can_ enable mutation of members as part of the public interface, it's not recommended.

```scala
case class Person(name: String, var age: Int)
val me = Person("Brian", 36)
me.age = 37

println(me)
```

All case classes come with a `.copy` method that allows you to create a new instance of the case class with some fields changed.

```scala
case class Person(name: String, age: Int):
  def happyBirthday: Person = copy(age = age + 1)

val meRightNow = Person("Brian", 36)
val meAfterBirthday = meRightNow.happyBirthday

println(meRightNow)
println(meAfterBirthday)

val anotherMe = meRightNow.copy(age = 37)
println(anotherMe == meAfterBirthday) // true
```

This snippet highlights another aspect of case classes: equality is based on the contents of the case class, not the in-memory reference. This means that if you have two case classes of the same type with the same contents, they are considered equal.

Case classes with different nominal types that happen to have the same contents are not considered equal by default.

```scala
case class Person(name: String, age: Int):
  def asRobot: Robot = Robot(name, age)

case class Robot(name: String, age: Int)

val me = Person("Brian", 36)
val robotMe = me.asRobot

println(me)
println(robotMe)
println(me == robotMe) // false
```

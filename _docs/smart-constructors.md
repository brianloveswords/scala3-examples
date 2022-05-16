# Smart Constructors

We showed using things like `map` and `filter` on lists in the previous section:

```scala
val xs = List(1, 2, 3)
println(xs.filter(_ > 1).map(_ + 1))
```

We can also use many of these functions on other containers, like `Option`. By
using these functions on `Option`s, we can code down the "happy" path and
defer when we want to deal with the possibility of `None` until we need the
raw wrapped value.

```scala
def parseVolume(s: String): Option[Byte] =
  s.toByteOption.filter(_ >= 0).filter(_ <= 100)

// theoretically this should only get
// called once we have a parsed volume
def volumeIcon(volume: Byte): String =
  require(volume >= 0 && volume <= 100, "Invalid volume")
  volume match
    case v if v >= 66 => "🔊"
    case v if v >= 33 => "🔉"
    case v if v > 0   => "🔈"
    case v if v == 0  => "🔇"

def businessLogic(volume: Byte): Unit =
  val icon = volumeIcon(volume)
  println(s"[${icon}] setting volume to ${cyan(volume)}")

def program(a: String): Unit = parseVolume(a)
  .map(businessLogic)
  .getOrElse(printError)

def printError: Unit =
  println("[😞] " + red("must be a number between 0 and 100"))

def cyan(s: Any): String =
  Console.CYAN + s.toString + Console.RESET

def red(s: Any): String =
  Console.RED + s.toString + Console.RESET

val inputs = List("0", "8", "36", "75", "101", "nope", "-10")
inputs.foreach(program)
```

`parseVolume` makes sure we have a valid number between 0 and 100, but
unfortunately it keeps that knowledge to itself—there's nothing in the return
type `Option[Byte]` to indicate we have a valid volume. Let's fix that!

```scala
object Volume:
  val MinValue = 0
  val MaxValue = 100

  def parse(s: String): Option[Volume] = s.toByteOption
    .filter(_ >= MinValue)
    .filter(_ <= MaxValue)
    .map(Volume(_))

// the `private` prevents Volume from being constructed
// directly, which forces users to go through `Volume.parse`
case class Volume private (value: Byte):
  // it's useful to keep this assertion for fuzz testing
  // and making sure the internal functions are correct
  require(
    value >= Volume.MinValue && value <= Volume.MaxValue,
    s"Invalid volume: ${value}"
  )

  def icon: String = value match
    case v if v >= 66 => "🔊"
    case v if v >= 33 => "🔉"
    case v if v > 0   => "🔈"
    case v if v == 0  => "🔇"

def businessLogic(volume: Volume): Unit =
  println(s"[${volume.icon}] setting volume to ${cyan(volume.value)}")

def program(a: String): Unit = Volume
  .parse(a)
  .map(businessLogic)
  .getOrElse(printError)

// everything else stays the same

//{
def printError: Unit =
  println("[😞] " + red("must be a number between 0 and 100"))

def cyan(s: Any): String =
  Console.CYAN + s.toString + Console.RESET

def red(s: Any): String =
  Console.RED + s.toString + Console.RESET

val inputs = List("0", "8", "36", "75", "101", "nope", "-10")
inputs.foreach(program)
//}
```

This is better! We now keep track of the fact we've validated the input in the
type system by using `Volume` and making the default constructor private, so
the only path to construction is through `parse` which we know validates.

We also have a `require` assertion in the constructor to make sure that even
internally we can't build an unvalidated `Volume`. This lets `businessLogic`
be absolutely sure that `Volume` can be trusted.

## Improving Errors

This is getting better, but we could improve the error messaging. Right now we
don't know why it failed: we just say "must be a number between 0 and 100".

We can use `Either` instead of `Option` to improve our error messaging.

```scala
enum Error extends Exception:
  case VolumeTooLow(value: Byte)
  case VolumeTooHigh(value: Byte)
  case VolumeNotANumber(value: String)

  override def getMessage: String = this match
    case VolumeTooLow(value)     => s"volume too low: $value"
    case VolumeTooHigh(value)    => s"volume too high: $value"
    case VolumeNotANumber(value) => s"volume not a number: $value"

object Volume:
  val MinValue = 0
  val MaxValue = 100

  def parse(s: String): Either[Error, Volume] = s.toByteOption match
    case None => Left(Error.VolumeNotANumber(s))
    case Some(value) =>
      value match
        case v if v < MinValue => Left(Error.VolumeTooLow(v))
        case v if v > MaxValue => Left(Error.VolumeTooHigh(v))
        case v                 => Right(Volume(v))

def printError(error: Exception): Unit =
  println("[😞] " + red(error.getMessage))

def program(a: String): Unit = Volume.parse(a) match
  case Right(volume) => businessLogic(volume)
  case Left(error)   => printError(error)

// everything else stays the same

//{
case class Volume private (value: Byte):
  require(
    value >= Volume.MinValue && value <= Volume.MaxValue,
    s"Invalid volume: ${value}"
  )

  def icon: String = value match
    case v if v >= 66 => "🔊"
    case v if v >= 33 => "🔉"
    case v if v > 0   => "🔈"
    case v if v == 0  => "🔇"

def businessLogic(volume: Volume): Unit =
  println(s"[${volume.icon}] setting volume to ${cyan(volume.value)}")

def cyan(s: Any): String =
  Console.CYAN + s.toString + Console.RESET

def red(s: Any): String =
  Console.RED + s.toString + Console.RESET

val inputs = List("0", "8", "36", "75", "101", "nope", "-10")
inputs.foreach(program)
//}
```

```text
[🔇] setting volume to 0
[🔈] setting volume to 8
[🔉] setting volume to 36
[🔊] setting volume to 75
[🔊] setting volume to 100
[😞] volume too high: 101
[😞] volume not a number: nope
[😞] volume too low: -10
```

## Using folds

Whenever we are using `match` directly, we should consider whether it's more
readable to use one of standard functions that exist on containers.

For example, when we are transform both arms of the match to the same type,
like we do in `program` and `parse`, that can be represented with a with
`fold` instead of an explicit match.

```scala
object Volume:
  val MinValue = 0
  val MaxValue = 100

  def parse(s: String): Either[Error, Volume] =
    s.toByteOption.fold(Left(Error.VolumeNotANumber(s))) { value =>
      value match
        case v if v < MinValue => Left(Error.VolumeTooLow(v))
        case v if v > MaxValue => Left(Error.VolumeTooHigh(v))
        case v                 => Right(Volume(v))
    }

def program(a: String): Unit =
  Volume.parse(a).fold(printError, businessLogic)

// everything else stays the same

//{
enum Error extends Exception:
  case VolumeTooLow(value: Byte)
  case VolumeTooHigh(value: Byte)
  case VolumeNotANumber(value: String)

  override def getMessage: String = this match
    case VolumeTooLow(value)     => s"volume too low: $value"
    case VolumeTooHigh(value)    => s"volume too high: $value"
    case VolumeNotANumber(value) => s"volume not a number: $value"

case class Volume private (value: Byte):
  require(
    value >= Volume.MinValue && value <= Volume.MaxValue,
    s"Invalid volume: ${value}"
  )

  def icon: String = value match
    case v if v >= 66 => "🔊"
    case v if v >= 33 => "🔉"
    case v if v > 0   => "🔈"
    case v if v == 0  => "🔇"

def businessLogic(volume: Volume): Unit =
  println(s"[${volume.icon}] setting volume to ${cyan(volume.value)}")

def printError(error: Exception): Unit =
  println("[😞] " + red(error.getMessage))

def cyan(s: Any): String =
  Console.CYAN + s.toString + Console.RESET

def red(s: Any): String =
  Console.RED + s.toString + Console.RESET

val inputs = List("0", "8", "36", "75", "101", "nope", "-10")
inputs.foreach(program)
//}
```


And we can reduce one more bit of boilerplate: when an anonymous function goes
right into a match, we can typically remove the `value => match value` part,
and just pass the match arms directly.

```scala
object Volume:
  val MinValue = 0
  val MaxValue = 100

  def parse(s: String): Either[Error, Volume] =
    s.toByteOption.fold(Left(Error.VolumeNotANumber(s))) {
      case v if v < MinValue => Left(Error.VolumeTooLow(v))
      case v if v > MaxValue => Left(Error.VolumeTooHigh(v))
      case v                 => Right(Volume(v))
    }

// everything else stays the same

//{
def program(a: String): Unit =
  Volume.parse(a).fold(printError, businessLogic)

enum Error extends Exception:
  case VolumeTooLow(value: Byte)
  case VolumeTooHigh(value: Byte)
  case VolumeNotANumber(value: String)

  override def getMessage: String = this match
    case VolumeTooLow(value)     => s"volume too low: $value"
    case VolumeTooHigh(value)    => s"volume too high: $value"
    case VolumeNotANumber(value) => s"volume not a number: $value"

case class Volume private (value: Byte):
  require(
    value >= Volume.MinValue && value <= Volume.MaxValue,
    s"Invalid volume: ${value}"
  )

  def icon: String = value match
    case v if v >= 66 => "🔊"
    case v if v >= 33 => "🔉"
    case v if v > 0   => "🔈"
    case v if v == 0  => "🔇"

def businessLogic(volume: Volume): Unit =
  println(s"[${volume.icon}] setting volume to ${cyan(volume.value)}")

def printError(error: Exception): Unit =
  println("[😞] " + red(error.getMessage))

def cyan(s: Any): String =
  Console.CYAN + s.toString + Console.RESET

def red(s: Any): String =
  Console.RED + s.toString + Console.RESET

val inputs = List("0", "8", "36", "75", "101", "nope", "-10")
inputs.foreach(program)
//}
```

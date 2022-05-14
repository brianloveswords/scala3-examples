# Enums

An `enum` in Scala is similar from what you might be used to in languages like
Go or C, but with a lot more expressive power.

In other languages, an enumeration is a set of named constants that point to
concrete values that are already part of the type system. There is usually a
type alias to help with readability.

```go
package main

import "fmt"

type Season int64

const (
  Summer Season = iota
  Autumn
  Winter
  Spring
)

func (s Season) String() string {
  switch s {
  case Summer:
    return "summer"
  case Autumn:
    return "autumn"
  case Winter:
    return "winter"
  case Spring:
    return "Spring"
  }
  return "unknown"
}

func main() {
  x := Spring
  fmt.Println(x)
}
```

The opaque type alias `Season` makes this a bit safer, but internally it is
still an `int64` in disguise.

In Scala, we would model this data with an `enum`. When we do this, we are
*inventing a whole new type that did not exist before*. These are not
internally represented by strings or ints or anything else! We get to invent
terms from whole cloth that are legal within that brand new type.

```scala
enum Season:
  case Summer
  case Autumn
  case Winter
  case Spring

  // We get the `toString` for free, so let's
  // do something different
  def toEmoji: String = this match
    case Summer => "🌞"
    case Autumn => "🍂"
    case Winter => "🌨"
    case Spring => "🌻"

// Create a "companion object" to the Season enum,
// which gives a nice namespace to put related functions
object Season:
  def parse(s: String): Either[String, Season] = s.toLowerCase match
    case "summer" => Right(Summer)
    case "autumn" => Right(Autumn)
    case "winter" => Right(Winter)
    case "spring" => Right(Spring)
    // This will match anything left over
    // and prevent the compiler from warning
    // us about an inexhaustive pattern
    case _ => Left(s"Unknown season: $s")

  def celebrate(input: String): String = parse(input) match
    // Handle the success case
    case Right(season) =>
      val emoji = season.toEmoji
      season match
        case Winter => s"brr bundle up for ${emoji}"
        case _      => s"I love a nice ${season} day ${emoji}!"

    // Handle the failure case
    case Left(error) => error

println(Season.celebrate("autumn"))
```

## Exhausitivty Checking

Using `enum` and pattern matching gives us something called "exhaustiveness
checking". The compiler knows all of the possible values of the `Season` enum,
and can make sure we covered them all in the `match` expression.

This means if we discover a new season, we can add it to the `Season` enum
then let the compiler guide us to what places in code need to change

```scala
enum Season:
  case Summer
  case Autumn
  case Winter
  case Spring
  // newly discovered
  case FifthSeason


// imagine this is in a different file, maybe even
// a different package altogether. the compiler will
// warn us we aren't handling all the cases once we add
// `FifthSeason`
def handleSeasons(season: Season): String = season match
  case Season.Summer => "hot"
  case Season.Autumn => "temperate"
  case Season.Winter => "cold"
  case Season.Spring => "nice"

// this will end the world!
println(handleSeasons(Season.FifthSeason))
```

When we need to make changes, we make changes to the data model *first* and
then change function implementations as guided by the compiler.


## Containers

`enum` is even more powerful. We discussed `Option` and `Either` before, and
both of these types can be represented using `enum`.

```scala

// the `+A` means we want to accept subtypes of A,
// so the type `Maybe[Exception]` allows
// `Just(RuntimeException("whoops"))`
enum Maybe[+A]:
  case Yep(a: A)
  case Nope

// this lets us use `Just` and `Nothing`
// without having to prefix them with `Maybe`
import Maybe.*

val x = Yep("hello")
val result = x match
  case Yep(s) => s"Yep, we got $s"
  case Nope    => "Nope, we got nothin'"

println(result)
```

## Business data modeling

`enum` is useful for modeling business data. For example, maybe we have two
ways of accepting addresses: for international we just need their country and
postal code, but for US we need their full address.

```scala
enum Address(country: String, postalCode: String):
  case UnitedStates(
    street: String,
    city: String,
    state: String,
    zipCode: String,
  ) extends Address("US", zipCode)

  case International(
    country: String,
    postalCode: String,
  ) extends Address(country, postalCode)

object Order:
  def calculateShipping(address: Address): BigDecimal = address match
    // do something fancy with state and zipCode
    // to figure it out
    case Address.UnitedStates(_, _, state, zipCode) =>
      BigDecimal(5)

    // flat rate shipping internationally.
    // the `case _: <type>` form allows us to match on
    // just type if we don't care about what's inside.
    // we could also name the variable, `case x: <type>`
    // if we did need access to it match arm.
    case _: Address.International =>
      BigDecimal(10)


val us = Address.UnitedStates(
  street = "123 Main St",
  city = "Anytown",
  state = "CA",
  zipCode =  "90210",
)

val de = Address.International(
  country = "DE",
  postalCode = "10",
)

val usShipping = Order.calculateShipping(us)
val deShipping = Order.calculateShipping(de)
println(s"${us}: ${usShipping}")
println(s"${de}: ${deShipping}")
```


## Scala 2: `sealed trait`

Prior to Scala 3, enumerations  would have been implemented as a `sealed
trait`. The `Season` enum above would have been represented like this:

```scala
// `trait` lets us invent a new type, and `sealed`
// means it cannot be extended outside of the file
// that defined it. This allows exhaustiveness checking.
sealed trait Season
object Season {
  case object Summer extends Season
  case object Autumn extends Season
  case object Winter extends Season
  case object Spring extends Season
}
```


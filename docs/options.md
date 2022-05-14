# Options

Since Scala is built on Java, `null` will always be a lurking presense
underneath the hood, but in Scala code we do use it and use `Option` to either
represent a computation that can return something or nothing, or a piece of
data that is optional.

Say we were representing an address form where Line 2 was optional. Coming
from languages that embrace `null`, we might do something like this:

```scala
case class Address(
  line1: String,
  line2: String,
  city: String,
  state: String,
  zip: String
):
  lazy val mailingLabel: String =
    s"""
    |${line1.trim}
    |${line2.trim}
    |${city.trim}, ${state.trim} ${zip.trim}
    """.stripMargin

val address = Address(
  line1 = "123 Main St",
  line2 = null,
  city = "Anytown",
  state = "CA",
  zip = "90210"
)

// 💣 : this will throw an exception because `line2` is null
// and cannot be `.trim`d
println(address.mailingLabel)
```

In general, if we can let the type system do the thinking for us, we should.
As human developers with a lot on our minds, we should not have to also keep
track of which things can possibly be `null` and code defensively. The
`Option` type lets us do that.


```scala
case class Address(
  line1: String,
  line2: Option[String],
  city: String,
  state: String,
  zip: String
):
  lazy val mailingLabel: String =
    // pattern match on the Option and extract
    // the wrapped value if it's a `Some`.
    val maybeLine2 = line2 match
      case Some(line2) => s"\n${line2.trim}"
      case None => ""

    s"""
    |${line1.trim}${maybeLine2}
    |${city.trim}, ${state.trim} ${zip.trim}
    """.stripMargin

val address1 = Address(
  line1 = "123 Main St",
  line2 = None,
  city = "Anytown",
  state = "CA",
  zip = "90210"
)

val address2 = Address(
  line1 = "123 Main St",
  line2 = Some("Apt. 1"),
  city = "Anytown",
  state = "CA",
  zip = "90210"
)

println(address1.mailingLabel)
println(address2.mailingLabel)
```

An `Option[A]` represents either `Some[A]`, where `A` is any type at all, or
`None`. We *cannot* use the value inside without making sure it's not `None`.

Pattern matching using a `match` expression is the fundamental way to do this,
but there are many methods on `Option` that allow us to interact with wrapped
values safely. We'll cover these more in later chapters, but if you are
following along using VS Code, please play around and explore what your IDE
suggests!


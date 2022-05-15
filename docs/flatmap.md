# map and flatMap

You might be familiar with `map` from Python, and in Scala we can also use
`map` on lists:

```scala
val xs = List(1, 2, 3)
println(xs.map(_ + 1))
```

But we can also use `map` on other containers, like `Option`:

```scala
val some: Option[Int] = Some(1)
val none: Option[Int] = None

// What do you think these will print?
println(some.map(_ + 1))
println(none.map(_ + 1))
```

When the Option is `None`, it remains `None`, but if there is a value in there
we get a new `Option` with the value transformed by the function we passed to
`map`. This allows us to chain transformations without having to unwrap the
value until we need the actual value.

```scala
case class ValidatedInput private (value: Int)

object ValidatedInput:
  def parse(input: String): Option[ValidatedInput] =
    input.toIntOption
      .filter(_ < 10)
      .map(new ValidatedInput(_))

def businessLogic(validated: ValidatedInput): Int =
  validated.value * 7 - 1

def encode(a: Int): String =
  "~" + a.toString.reverse + "!"

def program(a: String): Option[String] =
  val validated = ValidatedInput.parse(a)
  validated
    .map(businessLogic)
    .map(encode)

val result1 = program("8")
val result2 = program("22")
val result3 = program("nope")

println(result1)
println(result2)
println(result3)
```

Let's take a look at `Either`

```scala
val right: Either[String, Int] = Right(1)
val left: Either[String, Int] = Left("error")

println(right.map(_ + 1))
println(left.map(_ + 1))
```


# flatMap



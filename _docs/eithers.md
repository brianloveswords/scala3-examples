# Eithers

Like `Option[A]`, `Either[A, B]` represents a choice between two things.

- `Left[A]`: generally represents a failure
- `Right[B]`: generally represents a success

Those coming from Go will be familiar with functions that return multiple
values e.g. `(result, error)`. The `Either[A, B]` type in Scala allows us a
structured way to represent this idiom, but in a way that does not allow the
error case to go unchecked.

```scala
def safeDivide(x: Int, y: Int): Either[String, Int] =
  if y == 0
  then Left("Divide by zero")
  else Right(x / y)

println(safeDivide(1, 0))
println(safeDivide(84, 2))
```

Like `Option`, the fundamental way to pull values from an `Either` is through pattern matching:

```scala
val either: Either[String, Int] = Right(42)
val result = either match
  case Left(error) => s"aw dang: ${error}"
  case Right(value) => s"yay! ${value * 2}"

println(result)
```

There are many useful methods on `Either` that allow us to interact with the
inner values without having to use pattern matching. We'll cover those in a
forthcoming section.


## Academic detour

This section is not necessary to understand how to write Scala effectively,
but might be academically intereseting.

Things like `Either[A, B]` and `Option[A]` are referred to as [algebraic data
types](https://en.wikipedia.org/wiki/Algebraic_data_type), specifically sum
types. This is because you can do some math based on the types within the
containers to understand how many possible values are represented by the type.

Using the language of [type theory](https://en.wikipedia.org/wiki/Type_theory), there are two terms that inhabit the `Boolean` type: `true` and `false`. This lets us think of  `Boolean` as a set with a cardinality of 2. If a calculation returns a `Boolean`, we know it's going to return one of two things.

Looking at a larger type, `Byte` has a cardinality of 256: all of the natural
numbers between `-128` to `127`. A function that returns a `Byte` can return
one of 256 values.

The sum type `Either[Boolean, Byte]` has a cardinality of 2 + 256 = 258:

- `Left(true)`
- `Left(false)`
- `Right(-128)`
- `Right(-127)`
- `Right(-126)`
- ⋮


Tuples (and case classes, which can be thought of as named tuples) are **product types**. This is because to understand how many terms inhabity the composite type, we take how many terms inhabit each of the components and multiply them.

Using `(Boolean, Byte)` as our example, we get 2 * 256 = 512 possible values:

- `(true, -128)`
- `(true, -127)`
- ⋮
- `(true, 127)`
- `(false, -128)`
- `(false, -127)`
- ⋮

### Why does this matter?

It's useful when designing programs to use the type that models the problem with the lowest cardinality.

For example, it's possible to represent boolean logic with a `String` by
inferring `f` as falsiness and everything else as truthiness. This tends to
make it harder to reason about the program, though—we as developers have to
keep in our head that mapping.

If we take this approach, we cannot look at a function signature `String =>
Byte` and know whether.

This problem becomes accute important with functions that take and return
`String` because `String` has infinite cardinality. Rather than _inferring_
meaning from `Strings` throughout our program, which relies on developers
keeping track of things in our heads, we should _explicitly parse_ the
`String` into a meaningful type, and even better if we can get into a type
with a lower cardinality. This, fundamentally, is what **parsing** is.

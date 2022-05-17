# Holes

Scala has an expressive type system and it can be useful to think of how all the types of your program fit together before worrying about the implementation.

To skip the implementation of something, you can use `???`. It fits into any type so will allow the program to typecheck, but will blow up when evaluated.

```scala
// file: holes.sc
def calculatePrice(
  quantity: Int,
  listPrice: BigDecimal,
): BigDecimal = ???

calculatePrice(9, BigDecimal(3.99))
```

```shell
$ scala-cli holes.sc
```

```text
Exception in thread "main" scala.NotImplementedError: an implementation is missing
        at scala.Predef$.$qmark$qmark$qmark(Predef.scala:344)
        at holes$.calculatePrice(holes.sc:5)
        at holes$.<clinit>(holes.sc:7)
        at holes_sc$.main(holes.sc:24)
        at holes_sc.main(holes.sc)
```
## Implementation of `???`

While it looks like syntax, `???` is a function defined in the `scala.Predef` object that is always in scope by default for every Scala program. The definition of that function looks like this:

```scala
def ??? : Nothing = throw new NotImplementedError
```

In Scala, `Nothing` is the [bottom type](https://en.wikipedia.org/wiki/Bottom_type) of the type system and it can act like a subtype of anything because it's intended to signify that program execution is gonna get weird and there's not gonna be a value returned from the expression. This lets us use it as a placeholder for anything.

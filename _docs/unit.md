# Unit

Nearly everything in Scala is an expression that returns a value, even the functions we evaluate purely for side-effects like `println`.

What does `println` return?

```scala
// file: unit.sc
val result = println("hmmm")
println(result)
```

```shell
$ scala-cli unit.sc
```

```text
hmmm
()
```


Those funny empty parens `()` are a member of the type `Unit`. In fact, they are the *only* member of the type `Unit`. This type-with-only-one-member is used in cases where a function has nothing meaningful to return.

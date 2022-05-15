# Variables

Scala has two ways of declaring variables


## Prefer immutable variables: `val`

Almost all of the time you should use `val`. Variables declared with `val` cannot be redefined, not even to the same type.

```scala sc:fail
val times: Int = 1
times = 3
```

### Deferred initialization: `lazy val`

It is sometimes useful to declare a variable ahead of time but delay initialization until it is first used. This is done with `lazy val`.

```scala
// file: lazy-val.sc
lazy val times: Int =
  println("initializing")
  8 * 44 / 11 % 13

println("about to use times")
println(times)

// will not print "initializing" again
println("2nd times the charm")
println(times)
```

```shell
$ scala-cli lazy-val.sc
```

```text
about to use times
initializing
6
2nd times the charm
6
```

## Rarely use mutable variables: `var`

As much as possible you should prefer using immutable variables as they are easier to reason about and are safer to use correctly.

Infrequently you may need to write extremely performance sensitive code and mutating an existing variable could possibly to be more efficient.

```scala
var times = 1
times = 3
println("hey" * times)
```

Note, it's possible to use mutable datastructures with a `val` binding. We'll talk more about that when we get to collections.

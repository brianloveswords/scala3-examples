# Variables

Scala has two ways of declaring variables


## Prefer immutable variables: `val`

Almost all of the time you should use `val`. Variables declared with `val` cannot be redefined, not even to the same type.

```scala sc:fail
val times: Int = 1
times = 3
```

## Rarely use mutable variables: `var`

```scala
var times = 1
times = 3
println("hey" * times)
```

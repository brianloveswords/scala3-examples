# Functions

Functions in Scala are defined with the keyword `def`. The result of the last
expression in the function body is the return value.

```scala
def add(x: Int, y: Int): Int =
  x + y

println(add(1, 2))
```

## Function expressions

Functions can also be defined and assigned to variables like any other expression:

```scala
val multiply = (x: Int, y: Int) => x * y

println(multiply(3, 4))
```

## Partial application

It is frequently useful to be able to take a function and bake in one of the parameters, returning a new function that takes the remaining parameter. This is called **partial application**:

```scala
def add(x: Int, y: Int): Int =
  x + y

// when `_` is on the right side of the assignment,
// it acts as a placeholder for parameters
val inc = add(1, _)

println(inc(inc(2)))
```

This will come up a lot more once we start working with collections.

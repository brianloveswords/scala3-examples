# Collections

Scala has a rich collection of data structures that can be used to represent multiple values. Each of these has different performance characteristics that start to matter when you're building & accessing large sequences.

Sequences in the standard library are organized into a hierarchy of types, with the most common type being `Seq`.

## `Seq`: Linked Lists

The `Seq` type is abstract and represents a sequence of items.

```scala
val xs = Seq(1, 2, 3)

// this will print `List(1, 2, 3)`
println(xs)
```

## `IndexedSeq`: Vectors


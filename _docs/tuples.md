# Tuples

Scala has tuples which represent multiple heterogenous types in a single container. You can access members of the tuple by their 1-based index using `._1`, `._2`, etc.

```scala
val xs: (Int, String, Boolean) = (3, "hi", true)

// this will print "hihihi"
println {
  if xs._3
  then xs._2 * xs._1
  else ???
}
```

This illustrates another aspect of Scala that is useful to know: when there is just one formal parameter for a function, you can use `{}` instead of `()`. This is often used with functions that take a function as their sole parameter, like `.map` on sequences.

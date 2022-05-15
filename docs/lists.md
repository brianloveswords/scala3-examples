# Lists

There is a rich set of collections in the Scala standard library, both immutable and mutable. `List`, representing a linked list, is the the most common one.

```scala
val xs = List(1, 2, 3, 2, 1)

// Sums all the elements in the list
val sum = xs.foldLeft(0)((acc, x) => acc + x)
println(s"sum: ${sum}")

// Above can be shortened to this
val sum2 = xs.foldLeft(0)(_ + _)
println(s"sums equal? ${sum == sum2}")

// Returns a new list with all elements doubled.
// Lists are immutable, so for every function
// below, a new list is *always* returned
val doubled = xs.map(_ * 2)
println(s"xs: ${xs}")
println(s"doubled: ${doubled}")

// Filter out all odd numbers
val evens = xs.filter(_ % 2 == 0)
println(s"evens: ${evens}")

// Return a new list with 0 as the new head,
// and the original list as the tail. The speed
// of this operation is constant, does not change
// based on the size of the list
val zeroThenXs = 0 +: xs
println(s"zeroThenXs: ${zeroThenXs}")

// Return a new list with the element to the end of
// the list. Adding to the front is faster because
// the computer has to walk through the entire list
// to add to the back
val xsThenFour = xs :+ 4
println(s"xsThenFour: ${xsThenFour}")

// Returns a new list that is the two lists smashed
// together. This has to walk the first list to find
// the end to attach the second list to
val xsAndYs = xs ++ List(4, 5, 6)

// Returns two new lists in a tuple, with the first
// member of the tuple being a list that contains
// the elements that evaluated true for the predicate
// and the second list containing the rest
val (evens2, odds) = xs.partition(_ % 2 == 0)
println(s"evens equal? ${evens == evens2}")
println(s"odds: ${odds}")

// Returns true if the list has an even number
val hasAnEven = xs.exists(_ % 2 == 0)
println(s"hasAnEven: ${hasAnEven}")

// Return true if all elements are less than 10
val allSingleDigit = xs.forall(_ < 10)
println(s"allSingleDigit? ${allSingleDigit}")

// Take n elements from the front of the list
val firstTwo = xs.take(2)
println(s"firstTwo: ${firstTwo}")

// Drop the first n elements from the list
// and return the rest.
val rest = xs.drop(2)
println(s"rest: ${rest}")

// Take elements while a predicate matches
// and stop when it finds the first element
// that doesn't match
val lt3 = xs.takeWhile(_ < 3)
println(s"lt3: ${lt3}")

// drop elements while a predicate matches
// and stop when it finds the first element
// that doesn't match, or end of list
val gt3 = xs.dropWhile(_ < 3)
println(s"gt3: ${gt3}")
```




## Traits

```scala
// Traits: as of Scala 3.1.2, the default
// concrete implementation for these traits
// is a `List`.
val b = Seq(1, 2, 3)
val c = Iterable(1, 2, 3)
```

Here is the [hierarchy of collections](https://docs.scala-lang.org/overviews/collections-2.13/overview.html):

<img src=https://docs.scala-lang.org/resources/images/tour/collections-immutable-diagram-213.svg width=100%>


- Blue boxes represent `traits` (interfaces).
- Black boxes represent concrete implementations.
- Thick arrows represent default implementations.
- Thin arrows represent other implementations.
- Dotted arrows represent implementation by implicit conversion.


The top of the collection hierarchy is the `Iterable`, and `List` is the
default concrete implementation.

```scala
val xs = Iterable(1, 2, 3)
println(xs) // List(1, 2, 3)
```

## What is a List


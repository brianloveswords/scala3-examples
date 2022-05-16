# Hello World

Let's start with the long version and we'll whittle it down.

```scala
object Main:
  def main(args: Array[String]): Unit =
    val name = args(0)
    System.out.print("hello")
    System.out.print(s" ${name}")
    System.out.println("!")
```

_Note: if you hit the_ &nbsp;▶️ _button above, this will attempt to run, but it will fail because there's no way to pass a command line flag this way. You can edit `name` to be a static string and hit `Cmd+Enter` to run it and it should work._

If you are using `scala-cli` to follow along, put this into a file called `hello-world.scala`.

```
$ scala-cli hello-world.scala -- "your-name-here"
```

A few things about this code:

## Whitespace is active
Whitespace is used to delimit code blocks, like in python.

## Top-level objects
We have a top level object called `Main` that contains the `main` function. We'll talk more about `object` later; for now you can think of it like a `class` but there is only ever one instance and that instance is called `Main`. Alternatively, think of it like a namespace.

In Scala 2, all methods needed to be wrapped in a top-level object but that's not the case anymore in Scala 3.

## String interpolation
The `System.out.println` can be shortened to just `println`—it's assumed you're printing to stdout. If you want to print to stderr, you can use `System.err.println`.
The `s` in front of the string literal is a string interpolator. It will call `.toStrg` on the interpolated object and put it in the string.

Scala runs on the Java Virtual Machine, and in Java everything inherits from capital-O `Object` which comes with a default `.toString` implementation. This means anything can be interpolated whether it's useful or not. Try removing the `(0)` after `args` on line 3 and see what prints.


# Shortened version

```scala sc:nocompile
@main def main(name: String) =
  print("hello")
  print(s" ${name}")
  println("!")
```

- No top-level object: we can define the `main` function directly.
- Removed `System.out` and just call `println` directly.
- No reference to `args`: with the `@main` annotation, the function signature determines what arguments are expected.

# Values

Strings, integers, floats, and booleans

```scala
// file: values.sc
println("sca" + "la")
println(s"1 + 1 = ${1 + 1}")     // signed 32-bit integer
println(s"7.0/3.0 = ${7.0/3.0}") // double-precision floating-point
println(true && false)
println(true || false)
println(!true)
```

```
$ scala-cli values.sc
```

And you should see the following:

```text
scala
1 + 1 = 2
7.0/3.0 = 2.3333333333333335
false
true
false
```

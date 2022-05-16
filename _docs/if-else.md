# If/Else

Predicates do not need to be wrapped in parentheses. `then` and (optionally) `else` are used to delineate the branches.

```scala
if 7 % 2 == 0
then println("seven is even")
else println("seven is odd")
```

`else` is not strictly necessary if there is no alternative.

```scala
if 8 % 4 == 0
then println("eight is divisible by 4")
```

This ends up being fairly rare in Scala: it is an expression-based language and we generally avoid evaluating code purely for side-effects. We also cannot return early from functions in Scala. So in most cases, we tend to fill both arms of the branch.

Speaking of expressions, `if/else` is an expression so it can be assigned to a variable.

```scala
val result =
  val num = 9
  if num < 0
  then s"${num} is negative"
  else if num < 10
  then s"${num} is a single digit"
  else s"${num} has multiple digits"

println(result)
```

`result` is assigned to the result of the whitespace-delimited block nested underneath it. `num` is scoped just to the evaluation of the `if` expression and so would not be available after line 7.

```scala sc:fail
val trap =
  val ghost = "boo"

println(ghost)
```

case class ValidatedInput private (value: Int)

object ValidatedInput:
  def parse(input: String): Option[ValidatedInput] =
    input.toIntOption
      .filter(_ < 10)
      .map(new ValidatedInput(_))

def businessLogic(validated: ValidatedInput): Int =
  validated.value * 7 - 1

def encode(a: Int): String =
  "~" + a.toString.reverse + "!"

def program(a: String): Option[String] =
  val validated = ValidatedInput.parse(a)
  validated
    .map(businessLogic)
    .map(encode)

val result1 = program("8")
val result2 = program("22")
val result3 = program("nope")

println(result1)
println(result2)
println(result3)

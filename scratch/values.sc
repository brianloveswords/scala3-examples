object ZoneA {
  case class NonOpaque(value: Int)

  opaque type Dollars = Int
  object Dollars:
    def apply(x: Int): Dollars = x
    extension (x: Dollars)
      def +(y: Dollars): Dollars = x + y
      def -(y: Dollars): Dollars = x - y
}
import ZoneA.*

val x = Dollars(10)

println(x + Dollars(1))

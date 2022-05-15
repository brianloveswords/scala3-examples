sealed trait Error extends Exception

enum ChannelError extends Error:
  case MissingChannels(value: String)
  case TooManyChannels(value: String, size: Int)
  case InvalidFormat(value: String)
  case InvalidVolume(name: String, cause: VolumeError)

  override def getMessage: String = this match
    case InvalidFormat(value) =>
      s"invalid channel format: ${value} (must be '<name>:<volume>')"
    case InvalidVolume(name, cause) =>
      s"invalid volume for channel '${name}': ${cause.getMessage}"
    case MissingChannels(value) =>
      s"missing channel list"
    case TooManyChannels(value, size) =>
      s"too many channels: ${value} (parsed: ${size}, max: ${ChannelList.MaxSize})"

enum VolumeError extends Error:
  case TooLow(value: Byte)
  case TooHigh(value: Byte)
  case NotANumber(value: String)

  override def getMessage: String = this match
    case TooLow(value) =>
      s"volume too low: $value"
    case TooHigh(value) =>
      s"volume too high: $value"
    case NotANumber(value) =>
      s"volume not a number: $value"

case class Channel(name: String, volume: Volume)
object Channel:
  def parse(s: String): Either[ChannelError, Channel] = s match
    case s"${nameInput}:${volumeInput}" =>
      val name = nameInput.trim
      Volume
        .parse(volumeInput.trim)
        .map(Channel(name, _))
        .left
        .map(ChannelError.InvalidVolume(name, _))

    case _ =>
      Left(ChannelError.InvalidFormat(s))

case class ChannelList(channels: List[Channel]):
  require(
    channels.size <= ChannelList.MaxSize,
    s"too many channels: ${channels.size}"
  )

object ChannelList:
  val MaxSize = 8

  // Returns a list of channels or the first error it encounters. Later
  // sections we'll learn an easier way to collect errors how to collect
  def parse(s: String): Either[ChannelError, ChannelList] =
    val channels = s
      .split(";")
      .toList
      .map(_.trim)
      .filter(_.nonEmpty)
      .map(Channel.parse)

    if channels.sizeIs == 0
    then Left(ChannelError.MissingChannels(s))
    else if channels.size > MaxSize
    then Left(ChannelError.TooManyChannels(s, channels.size))
    else
      // We have a List[Either[Error, Channel]] but we need
      // an Either[Error, List[Channel]]. In a later section
      // we will learn about `traverse`, but for now we handle
      // this manually.
      val zero = Right(List.empty[Channel])
      channels
        .foldLeft[Either[ChannelError, List[Channel]]](zero) {
          case (Right(acc), Right(channel)) => Right(channel :: acc)
          case (Left(err), _)               => Left(err)
          case (_, Left(err))               => Left(err)
        }
        .map(ChannelList(_))

object Volume:
  val MinValue = 0
  val MaxValue = 100

  def parse(s: String): Either[VolumeError, Volume] =
    s.toByteOption.fold(Left(VolumeError.NotANumber(s))) {
      case v if v < MinValue => Left(VolumeError.TooLow(v))
      case v if v > MaxValue => Left(VolumeError.TooHigh(v))
      case v                 => Right(Volume(v))
    }

case class Volume private (value: Byte):
  require(
    value >= Volume.MinValue && value <= Volume.MaxValue,
    s"Invalid volume: ${value}"
  )

  def icon: String = value match
    case v if v >= 66 => "🔊"
    case v if v >= 33 => "🔉"
    case v if v > 0   => "🔈"
    case v if v == 0  => "🔇"

def program(a: String): Unit =
  ChannelList.parse(a).fold(printError, processChannelList)

def processChannelList(channelList: ChannelList): Unit =
  val channels = channelList.channels
  val maxNameSize = channels.map(_.name.size).max
  channels.map(processChannel(maxNameSize, _))
  println("---")

def processChannel(maxNameSize: Int, channel: Channel): Unit =
  val volume = channel.volume
  val name = channel.name.padTo(maxNameSize, ' ')
  val message = s"[${volume.icon}] ${blue(name)} => ${cyan(volume.value)}"
  println(message)

def printError(error: Exception): Unit =
  println("[😞] " + red(error.getMessage))

def cyan(s: Any): String =
  Console.CYAN + s.toString + Console.RESET

def blue(s: Any): String =
  Console.BLUE + s.toString + Console.RESET

def red(s: Any): String =
  Console.RED + s.toString + Console.RESET

val inputs =
  List(
    "main:100",
    "left:12; center:88; right:9",
    ";;;front: 0;;back: 44;;;",
    "",
    "a:1; b:2; c:3; d:4; e:5; f:6; g:7; h:8; i:9;",
    "-10",
    "x:10;y:nope"
  )
inputs.foreach(program)

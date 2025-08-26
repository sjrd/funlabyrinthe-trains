package myfunlaby

import com.funlabyrinthe.core.*
import com.funlabyrinthe.mazes.*
import com.funlabyrinthe.mazes.std.*

import user.sjrd.railways.*

object Trains extends Module:
  override protected def createComponents()(using Universe): Unit =
    val station = new Station
    val whistle = new Whistle
    val trainSwitch = new TrainSwitch
    val closedGate = new ClosedGate
    val openGate = new OpenGate
    val closeGateButton = new CloseGateButton
    val openGateButton = new OpenGateButton
    val centerAccessButton = new CenterAccessButton
  end createComponents

  def station(using Universe): Station = myComponentByID("station")
  def whistle(using Universe): Whistle = myComponentByID("whistle")
  def trainSwitch(using Universe): TrainSwitch = myComponentByID("trainSwitch")
  def closedGate(using Universe): ClosedGate = myComponentByID("closedGate")
  def openGate(using Universe): OpenGate = myComponentByID("openGate")
  def closeGateButton(using Universe): CloseGateButton = myComponentByID("closeGateButton")
  def openGateButton(using Universe): OpenGateButton = myComponentByID("openGateButton")
  def centerAccessButton(using Universe): CenterAccessButton = myComponentByID("centerAccessButton")
end Trains

export Trains.*

class Station(using ComponentInit) extends Ground:
  painter += "Fields/Grass"
  painter += "Filters/NiceSoftLighten"
end Station

class Whistle(using ComponentInit) extends PushButton derives Reflector:
  var newLight: Option[RailsLight] = None

  override def reflect() = autoReflect[Whistle]

  override def execute(context: MoveContext): Unit =
    import context.*
    super.execute(context)
    enabled = false
    pos.map(13, 21, 0) += newLight.get
  end execute
end Whistle

class TrainSwitch(using ComponentInit) extends Switch derives Reflector:
  var railsSwitch: Option[Rails] = None

  override def reflect() = autoReflect[TrainSwitch]

  override def switchOn(context: MoveContext): Unit =
    railsSwitch.get.direction = Some(Direction.North)

  override def switchOff(context: MoveContext): Unit =
    railsSwitch.get.direction = Some(Direction.East)

  override def execute(context: MoveContext): Unit =
    import context.*
    if pos.map.posComponentsBottomUp(Position(6, 6, 0)).isEmpty then
      super.execute(context)
end TrainSwitch

class ClosedGate(using ComponentInit) extends Obstacle:
  painter += "Gates/ClosedPorch"
end ClosedGate

class OpenGate(using ComponentInit) extends Effect:
  painter += "Gates/OpenPorch"
end OpenGate

class CloseGateButton(using ComponentInit) extends PushButton:
  override def buttonDown(context: MoveContext): Unit =
    import context.*
    pos.map(9, 23, 0) = grass + closedGate
    enabled = false
    openGateButton.enabled = true
end CloseGateButton

class OpenGateButton(using ComponentInit) extends PushButton:
  override def buttonDown(context: MoveContext): Unit =
    import context.*
    pos.map(9, 23, 0) = grass + openGate
    enabled = false
    closeGateButton.enabled = true
end OpenGateButton

class CenterAccessButton(using ComponentInit) extends PushButton:
  override def buttonDown(context: MoveContext): Unit =
    import context.*
    enabled = false
    pos.map(7, 3, 0) += noEffect
end CenterAccessButton

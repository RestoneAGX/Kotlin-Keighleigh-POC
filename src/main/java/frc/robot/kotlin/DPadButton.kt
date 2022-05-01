package frc.robot.kotlin

import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj2.command.button.Button

class DPadButton(var controller: XboxController, var direction: Direction) : Button() {
    enum class Direction(var direction: Int) {
        UP(0), RIGHT(90), DOWN(180), LEFT(270);
    }

    override fun get(): Boolean {
        val dPadValue = controller.pov
        return (dPadValue == direction.direction || dPadValue == (direction.direction + 45) % 360 || dPadValue == (direction.direction + 315) % 360)
    }
}
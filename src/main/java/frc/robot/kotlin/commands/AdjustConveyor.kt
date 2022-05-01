package frc.robot.kotlin.commands

import edu.wpi.first.wpilibj2.command.CommandBase
import frc.robot.RobotState
import frc.robot.kotlin.subsystems.Conveyor

class AdjustConveyor(conveyor: Conveyor) : CommandBase() {
    private var conveyor: Conveyor = conveyor

    init {
        addRequirements(conveyor)
    }

    override fun execute() {
        if (RobotState.getEntryValue("Sensors", "Upper Occupied").boolean) //Put in variable if not readable
            conveyor.moveConveyor(true)
    }

    override fun end(interrupted: Boolean) {
        conveyor.stopConveyor()
    }

    override fun isFinished(): Boolean {
        return RobotState.getEntryValue("Sensors", "Upper Occupied").boolean &&
               !RobotState.getEntryValue("Sensors", "Lower Occupied").boolean
    }
}
package frc.robot.kotlin.subsystems

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMax.IdleMode
import com.revrobotics.CANSparkMaxLowLevel
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants.ConveyorConstants

class Conveyor :  SubsystemBase() {
    private val lowerConveyor = CANSparkMax(ConveyorConstants.LOWER_CONVEYOR, CANSparkMaxLowLevel.MotorType.kBrushless)

    init {
        lowerConveyor.idleMode = IdleMode.kCoast
    }

    fun moveConveyor(reversed: Boolean) {
        if (reversed)
            lowerConveyor.set(ConveyorConstants.CONVEYOR_SPEED * -1)
        else
            lowerConveyor.set(ConveyorConstants.CONVEYOR_SPEED)
    }

    fun stopConveyor() {
        lowerConveyor.set(0.0)
    }
}
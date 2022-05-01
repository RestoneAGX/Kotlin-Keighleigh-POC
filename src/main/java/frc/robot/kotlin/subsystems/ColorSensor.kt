package frc.robot.kotlin.subsystems

import com.revrobotics.ColorSensorV3
import edu.wpi.first.wpilibj.I2C
import edu.wpi.first.wpilibj.util.Color
import frc.robot.RobotState

class ColorSensor {
    private var port1 = I2C.Port.kOnboard //Might want to replace with PWM in-direct for I2C

    private var port2 = I2C.Port.kMXP

    private var colorSensorLower = ColorSensorV3(port1)
    private var colorSensorUpper = ColorSensorV3(port2)

    private val proxyCheck = 0.0 //Placeholder value for now

    private fun occupiedLower(): Boolean {
        return colorSensorLower.proximity > proxyCheck
    }

    private fun occupiedUpper(): Boolean {
        return colorSensorUpper.proximity > proxyCheck
    }

    fun getColorUpper(): Color? {
        return colorSensorUpper.color
    }

    fun getColorLower(): Color? {
        return colorSensorLower.color
    }

    fun ColorSensor() {}

    fun periodic() {
        RobotState.setEntryValue("Sensors", "Lower Occupied", occupiedLower())
        RobotState.setEntryValue("Sensors", "Upper Occupied", occupiedUpper())
        RobotState.setEntryValue("Sensors", "Lower Proximity", colorSensorLower.proximity)
        RobotState.setEntryValue("Sensors", "Upper Proximity", colorSensorUpper.proximity)

        //[Note]: Current alliance can be published to NetworkTables
    }
}
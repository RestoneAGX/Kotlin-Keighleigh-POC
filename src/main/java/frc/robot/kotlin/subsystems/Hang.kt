package frc.robot.kotlin.subsystems

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMax.IdleMode
import com.revrobotics.CANSparkMaxLowLevel
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants
import frc.robot.Constants.HangConstants

class Hang : SubsystemBase() {
    private var LeftHangMotor = CANSparkMax(HangConstants.LEFT_HANG_MOTOR, CANSparkMaxLowLevel.MotorType.kBrushless)
    private var RightHangMotor = CANSparkMax(HangConstants.RIGHT_HANG_MOTOR, CANSparkMaxLowLevel.MotorType.kBrushless)

    init {
        LeftHangMotor.idleMode = IdleMode.kBrake
        LeftHangMotor.setSmartCurrentLimit(Constants.CURRENT_LIMIT)

        RightHangMotor.idleMode = IdleMode.kBrake
        RightHangMotor.setSmartCurrentLimit(Constants.CURRENT_LIMIT)

        LeftHangMotor.encoder.position = 0.0
        RightHangMotor.encoder.position = 0.0

        LeftHangMotor.inverted = true
        RightHangMotor.inverted = true
    }

//    fun resetEncoder() {
//        LeftHangMotor.encoder.position = 0.0
//        LeftHangMotor.encoder.position = 0.0
//    }

    fun moveHang(speed: Double){
        RightHangMotor.set(speed)
        LeftHangMotor.set(speed)
    }

    fun stopHang() {
        RightHangMotor.stopMotor()
        LeftHangMotor.stopMotor()
    }

    override fun periodic() {
        SmartDashboard.putNumber("Lift Encoder Position", LeftHangMotor.encoder.position)
    }
}
package frc.robot.kotlin.subsystems

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMax.IdleMode
import com.revrobotics.CANSparkMaxLowLevel
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants
import frc.robot.Constants.ShooterConstants

class Shooter : SubsystemBase() {
    private var flywheel1 = CANSparkMax(ShooterConstants.FLYWHEEL_1, CANSparkMaxLowLevel.MotorType.kBrushless)
    private var flywheel2 = CANSparkMax(ShooterConstants.FLYWHEEL_2, CANSparkMaxLowLevel.MotorType.kBrushless)

    init {
        flywheel1.setSmartCurrentLimit(Constants.CURRENT_LIMIT)
        flywheel2.setSmartCurrentLimit(Constants.CURRENT_LIMIT)

        flywheel1.pidController.p = ShooterConstants.kP
        flywheel2.pidController.p = ShooterConstants.kP

        flywheel1.pidController.ff = 0.00016
        flywheel2.pidController.ff = 0.00016

        flywheel1.idleMode = IdleMode.kCoast
        flywheel2.idleMode = IdleMode.kCoast

        flywheel1.inverted = true
        flywheel2.follow(flywheel1, true)
    }

    fun setRPM(speed: Double) {
        flywheel1.pidController.setReference(speed, CANSparkMax.ControlType.kVelocity)
        flywheel2.pidController.setReference(speed, CANSparkMax.ControlType.kVelocity)
    }

    fun stopShooter() {
        flywheel1.set(0.0)
        flywheel2.set(0.0)
    }

    override fun periodic() {
        SmartDashboard.putNumber("Flywheel1 RPM", flywheel1.encoder.velocity)
        SmartDashboard.putNumber("Flywheel2 RPM", flywheel2.encoder.velocity)
        SmartDashboard.putNumber("flywheel power", flywheel2.busVoltage)
    }
}
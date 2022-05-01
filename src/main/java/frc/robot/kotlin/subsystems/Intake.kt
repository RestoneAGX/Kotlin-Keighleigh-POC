package frc.robot.kotlin.subsystems

import com.revrobotics.CANSparkMax
import com.revrobotics.CANSparkMax.IdleMode
import com.revrobotics.CANSparkMaxLowLevel
import edu.wpi.first.wpilibj.DoubleSolenoid
import edu.wpi.first.wpilibj.PneumaticsModuleType
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants
import frc.robot.Constants.IntakeConstants

class Intake : SubsystemBase() {
    private val intakeSolenoid = DoubleSolenoid(PneumaticsModuleType.REVPH,
            IntakeConstants.FORWARD_CHANNEL_1, IntakeConstants.REVERSE_CHANNEL_1)
    private val intakeSolenoid2 = DoubleSolenoid(PneumaticsModuleType.REVPH,
            IntakeConstants.FORWARD_CHANNEL_2, IntakeConstants.REVERSE_CHANNEL_2)
    private var pistonsForward = false
    private var intake = CANSparkMax(IntakeConstants.INTAKE_MOTOR, CANSparkMaxLowLevel.MotorType.kBrushed)

    init {
        intake.setSmartCurrentLimit(Constants.CURRENT_LIMIT)
        intake.idleMode = IdleMode.kCoast
        intake.inverted = true
    }


    fun moveIntake(reversed: Boolean){
        if (reversed)
            intake.set(IntakeConstants.INTAKE_SPEED * -1)
        else
            intake.set(IntakeConstants.INTAKE_SPEED)
    }

    fun stopIntake() {
        intake.set(0.0)
    }

    fun toggleIntakeState(){
        pistonsForward = !pistonsForward
        if (pistonsForward){
            intakeSolenoid.set(DoubleSolenoid.Value.kReverse)
            intakeSolenoid2.set(DoubleSolenoid.Value.kReverse)
        }
        else{
            intakeSolenoid.set(DoubleSolenoid.Value.kForward)
            intakeSolenoid2.set(DoubleSolenoid.Value.kForward)
        }

    }
}
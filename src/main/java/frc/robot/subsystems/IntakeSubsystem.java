package frc.robot.subsystems;

import frc.robot.Constants.*;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import com.revrobotics.*;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class IntakeSubsystem extends SubsystemBase {

  private DoubleSolenoid intakeSolenoid = new DoubleSolenoid(PneumaticsModuleType.REVPH,
      IntakeConstants.FORWARD_CHANNEL_1, IntakeConstants.REVERSE_CHANNEL_1);
  private DoubleSolenoid intakeSolenoid2 = new DoubleSolenoid(PneumaticsModuleType.REVPH,
      IntakeConstants.FORWARD_CHANNEL_2, IntakeConstants.REVERSE_CHANNEL_2);
  public boolean pistonsForward = false;
  CANSparkMax intake = new CANSparkMax(IntakeConstants.INTAKE_MOTOR, MotorType.kBrushed);

  public IntakeSubsystem() {
    intake.setIdleMode(IdleMode.kCoast);
    intake.setSmartCurrentLimit(Constants.CURRENT_LIMIT);
    intake.setInverted(true);
  }

  public void reverseIntake(double speed) {
    intake.set(speed);
  }

  public void intakeBall(double speed) {
    intake.set(speed);
  }

  public void stopIntake() {
    intake.set(0);
  }

  public void controlIntakeSolenoids() {
    pistonsForward = !pistonsForward;
    if (pistonsForward)
      retractIntake();
    else
      deployIntake();
  }

  public void deployIntake() {
    intakeSolenoid.set(Value.kForward);
    intakeSolenoid2.set(Value.kForward);
  }

  public void retractIntake() {
    intakeSolenoid.set(Value.kReverse);
    intakeSolenoid2.set(Value.kReverse);
  }

  @Override
  public void periodic() {}
}

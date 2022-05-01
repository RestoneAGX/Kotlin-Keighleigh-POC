package frc.robot.subsystems;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.Constants.HangConstants;

public class HangSubsystem extends SubsystemBase {

    CANSparkMax LeftHangMotor = new CANSparkMax(HangConstants.LEFT_HANG_MOTOR, MotorType.kBrushless);
    CANSparkMax RightHangMotor = new CANSparkMax(HangConstants.RIGHT_HANG_MOTOR, MotorType.kBrushless);

    RelativeEncoder leftHangEncoder;
    RelativeEncoder rightHangEncoder;

    public HangSubsystem() {
        LeftHangMotor.setIdleMode(IdleMode.kBrake);
        LeftHangMotor.setSmartCurrentLimit(Constants.CURRENT_LIMIT);
        RightHangMotor.setIdleMode(IdleMode.kBrake);
        RightHangMotor.setSmartCurrentLimit(Constants.CURRENT_LIMIT);

        leftHangEncoder = LeftHangMotor.getEncoder();
        leftHangEncoder.setPosition(0);

        rightHangEncoder = RightHangMotor.getEncoder();
        rightHangEncoder.setPosition(0);

        LeftHangMotor.setInverted(true);
        RightHangMotor.setInverted(true);
    }

    public void resetEncoder() {
        rightHangEncoder.setPosition(0);
        leftHangEncoder.setPosition(0);
    }

    public void ExtendHangSubsystem() {
        RightHangMotor.set(HangConstants.HANG_SPEED);
        LeftHangMotor.set(HangConstants.HANG_SPEED);
        SmartDashboard.putNumber("Lift Encoder Position", leftHangEncoder.getPosition());
    }

    public void RetractHangSubsystem(double speed) {
        RightHangMotor.set(speed);
        LeftHangMotor.set(speed);
    }

    public void StopHang() {
        RightHangMotor.stopMotor();
        LeftHangMotor.stopMotor();
    }

    public double getEncoderPosition() {
        return leftHangEncoder.getPosition();
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Lift Encoder Position", leftHangEncoder.getPosition());
    }
}

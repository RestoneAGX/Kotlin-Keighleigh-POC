// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import frc.robot.Constants;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import frc.robot.Constants.ShooterConstants;

import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.math.controller.SimpleMotorFeedforward;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class ShooterSubsystem extends SubsystemBase {

    CANSparkMax flywheel1 = new CANSparkMax(ShooterConstants.FLYWHEEL_1, MotorType.kBrushless);
    CANSparkMax flywheel2 = new CANSparkMax(ShooterConstants.FLYWHEEL_2, MotorType.kBrushless);
    RelativeEncoder encoder = flywheel1.getEncoder();
    SimpleMotorFeedforward feedforward = new SimpleMotorFeedforward(ShooterConstants.kS, ShooterConstants.kV,
            ShooterConstants.kA);

    public ShooterSubsystem() {
        flywheel1.setInverted(true);

        flywheel1.setSmartCurrentLimit(Constants.CURRENT_LIMIT);
        flywheel2.setSmartCurrentLimit(Constants.CURRENT_LIMIT);

        flywheel1.getPIDController().setP(ShooterConstants.kP);
        flywheel2.getPIDController().setP(ShooterConstants.kP);

        flywheel1.getPIDController().setFF(0.00016);
        flywheel2.getPIDController().setFF(0.00016);

        // flywheel1.setOpenLoopRampRate(.2);
        // flywheel2.setOpenLoopRampRate(.2);

        flywheel1.setIdleMode(IdleMode.kCoast);
        flywheel2.setIdleMode(IdleMode.kCoast);

        flywheel2.follow(flywheel1, true);
    }

    public void setRPM(double speed) {
        flywheel1.getPIDController().setReference(speed, ControlType.kVelocity);
        flywheel2.getPIDController().setReference(speed, ControlType.kVelocity);
    }

    public void rampDownShooter() {
        flywheel1.set(0);
        flywheel2.set(0);
    }

    public void setVoltage(double volts) {
        flywheel1.setVoltage(volts);
        flywheel2.setVoltage(volts);
    }

    public double getVelocity() {
        return encoder.getVelocity();
    }

    public double calculateFeedforward(double rpm) {
        return feedforward.calculate(rpm / 60, (rpm - encoder.getVelocity()) / 60);
    }

    @Override
    public void periodic() {
        SmartDashboard.putNumber("Flywheel1 RPM", flywheel1.getEncoder().getVelocity());
        SmartDashboard.putNumber("Flywheel2 RPM", flywheel2.getEncoder().getVelocity());
        SmartDashboard.putNumber("flywheel power", flywheel2.getBusVoltage());
    }
}

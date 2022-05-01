// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.java.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.kotlin.subsystems.Limelight;
import frc.robot.java.DrivetrainSubsystem;
import frc.robot.Constants.AimbotConstants;

public class AimBotAngle extends CommandBase {

  Limelight limelight;
  DrivetrainSubsystem drivetrainSubsystem;

  public AimBotAngle(Limelight limelight, DrivetrainSubsystem drivetrainSubsystem) {
    addRequirements(limelight, drivetrainSubsystem);
    this.limelight = limelight;
    this.drivetrainSubsystem = drivetrainSubsystem;
  }

  @Override
  public void execute() {
    drivetrainSubsystem.drive(new ChassisSpeeds(
        0,
        0,
        Math.toRadians(limelight.getTargetData().getHorizontalOffset()) * 7.2)
        );
  }

  @Override
  public void end(boolean interrupted) {
    drivetrainSubsystem.drive(new ChassisSpeeds(0, 0, 0));
  }

  @Override
  public boolean isFinished() {
    return (Math.abs(limelight.getTargetData().getHorizontalOffset()) <= AimbotConstants.minimumAdjustment);
  }
}

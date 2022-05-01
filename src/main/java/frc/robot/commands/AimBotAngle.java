// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants.AimbotConstants;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.Limelight;

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
        Math.toRadians(limelight.getTargetData().horizontalOffset) * 7.2)
        );
  }

  @Override
  public void end(boolean interrupted) {
    drivetrainSubsystem.drive(new ChassisSpeeds(0, 0, 0));
  }

  @Override
  public boolean isFinished() {
    return (Math.abs(limelight.getTargetData().horizontalOffset) <= AimbotConstants.minimumAdjustment);
  }
}

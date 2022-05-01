// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.PIDCommand;
import frc.robot.Constants.AimbotConstants;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.Limelight;

public class AimbotPID extends PIDCommand {

  public AimbotPID(Limelight limelight, DrivetrainSubsystem driveSubsystem) {
    super(
        new PIDController(AimbotConstants.Kp, AimbotConstants.Ki, AimbotConstants.Kd),
        // This is how far away we are from the
        () -> -limelight.getTargetData().horizontalOffset,
        // We want the distance from the middle to be 0, therefore we keep it 0
        () -> 0,
        output -> {
          // Use the output here
          double speed = output * AimbotConstants.baseSpeed  *-1;

          driveSubsystem.drive(new ChassisSpeeds(0, 0, speed* DrivetrainSubsystem.MAX_VELOCITY_METERS_PER_SECOND));
        });

    addRequirements(driveSubsystem);
    addRequirements(limelight);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return Math.abs(getController().getPositionError()) < AimbotConstants.minimumAdjustment;
  }
}

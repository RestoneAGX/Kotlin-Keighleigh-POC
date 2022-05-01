// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Autonomous;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DrivetrainSubsystem;

public class TimedAutoDrive extends CommandBase {

  Timer timer = new Timer();

  DrivetrainSubsystem drivetrainSubsystem;
  ChassisSpeeds speeds;
  double desiredTime;
  boolean fieldOriented;

  public TimedAutoDrive(DrivetrainSubsystem drivetrainSubsystem, ChassisSpeeds speeds, double desiredTime) {
    this(drivetrainSubsystem, speeds, desiredTime, true);
  }

  public TimedAutoDrive(DrivetrainSubsystem drivetrainSubsystem, ChassisSpeeds speeds, double desiredTime,
      boolean fieldOriented) {
    addRequirements(drivetrainSubsystem);
    this.drivetrainSubsystem = drivetrainSubsystem;
    this.speeds = speeds;
    this.desiredTime = desiredTime;
    this.fieldOriented = fieldOriented;
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
    timer.reset();
    timer.start();
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    if (fieldOriented) {
      drivetrainSubsystem.drive(
          ChassisSpeeds.fromFieldRelativeSpeeds(
              speeds.vxMetersPerSecond,
              speeds.vyMetersPerSecond,
              speeds.omegaRadiansPerSecond,
              drivetrainSubsystem.getGyroscopeRotation()));
    } else {
      drivetrainSubsystem.drive(
          new ChassisSpeeds(
              speeds.vxMetersPerSecond,
              speeds.vyMetersPerSecond,
              speeds.omegaRadiansPerSecond));
    }

  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrainSubsystem.drive(new ChassisSpeeds(0, 0, 0));
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return timer.hasElapsed(desiredTime);
  }
}

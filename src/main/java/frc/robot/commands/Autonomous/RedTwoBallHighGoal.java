// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands.Autonomous;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AimAndFire;
import frc.robot.commands.ControlIntakeSolenoids;
import frc.robot.commands.IntakeBall;
// import frc.robot.subsystems.ColorSensor;
import frc.robot.subsystems.ConveyorSubsystem;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.ShooterSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class RedTwoBallHighGoal extends SequentialCommandGroup {
  /** Creates a new RedTwoBallHighGoal. */
  public RedTwoBallHighGoal(IntakeSubsystem intakeSubsystem, DrivetrainSubsystem drivetrainSubsystem, ShooterSubsystem shooterSubsystem, Limelight limelight, ConveyorSubsystem conveyorSubsystem) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new ControlIntakeSolenoids(intakeSubsystem),
      new ParallelDeadlineGroup(new TimedAutoDrive(drivetrainSubsystem, new ChassisSpeeds(1, 0, 0), 2, false), new IntakeBall(intakeSubsystem)),
      new ControlIntakeSolenoids(intakeSubsystem),
      new InstantCommand(() -> { if(intakeSubsystem.pistonsForward) intakeSubsystem.retractIntake();}),
      new AimAndFire(shooterSubsystem, conveyorSubsystem, limelight, drivetrainSubsystem)
    );
  }
}

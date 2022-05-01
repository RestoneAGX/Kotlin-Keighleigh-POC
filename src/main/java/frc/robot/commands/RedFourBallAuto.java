// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Constants.TrajectoryPaths;
import frc.robot.commands.Autonomous.RedTwoBallHighGoal;
import frc.robot.subsystems.ConveyorSubsystem;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.ShooterSubsystem;

// NOTE:  Consider using this command inline, rather than writing a subclass.  For more
// information, see:
// https://docs.wpilib.org/en/stable/docs/software/commandbased/convenience-features.html
public class RedFourBallAuto extends SequentialCommandGroup {
  /** Creates a new RedFourBall. */
  public RedFourBallAuto(IntakeSubsystem intakeSubsystem, Limelight limelight, DrivetrainSubsystem drivetrainSubsystem, ShooterSubsystem shooterSubsystem, ConveyorSubsystem conveyorSubsystem) {
    // Add your commands in the addCommands() call, e.g.
    // addCommands(new FooCommand(), new BarCommand());
    addCommands(
      new RedTwoBallHighGoal(intakeSubsystem, drivetrainSubsystem, shooterSubsystem, limelight, conveyorSubsystem),
      new InstantCommand(drivetrainSubsystem::zeroGyroscope),
      new ControlIntakeSolenoids(intakeSubsystem),
      new ParallelDeadlineGroup(new Paths(TrajectoryPaths.FourBallRed_GoToHumanPlayer, drivetrainSubsystem), new IntakeBall(intakeSubsystem)), // drive to le human player
      new WaitCommand(2),
      new ParallelDeadlineGroup(new Paths(TrajectoryPaths.FourBallRed_Little, drivetrainSubsystem), new IntakeBall(intakeSubsystem)),
      new Paths(TrajectoryPaths.FourBallRed_Shoot, drivetrainSubsystem), // drive to the shooting place thing
      new AimAndFire(shooterSubsystem, conveyorSubsystem, limelight, drivetrainSubsystem) // SHOOT AGAIN!!!!! :DDDD
    );
  }
}

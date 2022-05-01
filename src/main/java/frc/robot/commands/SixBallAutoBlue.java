// package frc.robot.commands;

// import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
// import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
// import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
// import frc.robot.Constants;
// import frc.robot.Constants.TrajectoryPaths;
// import frc.robot.subsystems.DrivetrainSubsystem;
// import frc.robot.subsystems.IntakeSubsystem;
// import frc.robot.subsystems.Limelight;
// import frc.robot.subsystems.ShooterSubsystem;
// import frc.robot.Constants;

// public class SixBallAutoBlue extends SequentialCommandGroup {
//     public SixBallAutoBlue(IntakeSubsystem intakeSubsystem, Limelight limelight, DrivetrainSubsystem drivetrainSubsystem, ShooterSubsystem shooterSubsystem) {
//         super(
//         new SequentialCommandGroup(
//                 new Paths(frc.robot.Constants.TrajectoryPaths.BACK_UP_TO_COLLECT_BALL, drivetrainSubsystem),
//                 new IntakeBall(intakeSubsystem),
//                 new AimbotPID(limelight, drivetrainSubsystem),
//                 new AimShotCalculated(shooterSubsystem, limelight),
//                 new Paths(frc.robot.Constants.TrajectoryPaths.TURN_1, drivetrainSubsystem),
//                 new ParallelDeadlineGroup(
//                         new Paths(frc.robot.Constants.TrajectoryPaths.COLLECT_NEAR_HUMAN_PLAYER, drivetrainSubsystem),
//                         new IntakeBall(intakeSubsystem)),
//                 new Paths(frc.robot.Constants.TrajectoryPaths.TURN_2, drivetrainSubsystem),
//                 new AimbotPID(limelight, drivetrainSubsystem),
//                 new AimShotCalculated(shooterSubsystem, limelight),
//                 new Paths(frc.robot.Constants.TrajectoryPaths.TURN_3, drivetrainSubsystem),
//                 new ParallelDeadlineGroup(
//                         new Paths(frc.robot.Constants.TrajectoryPaths.BACK_UP_TO_NEXT_BALL, drivetrainSubsystem),
//                         new IntakeBall(intakeSubsystem)),
//                 new Paths(frc.robot.Constants.TrajectoryPaths.TURN_4, drivetrainSubsystem),
//                 new ParallelDeadlineGroup(
//                         new Paths(frc.robot.Constants.TrajectoryPaths.GET_FINAL_BALL, drivetrainSubsystem),
//                         new IntakeBall(intakeSubsystem)),
//                 new Paths(frc.robot.Constants.TrajectoryPaths.TURN_5, drivetrainSubsystem),
//                 new AimbotPID(limelight, drivetrainSubsystem),
//                 new AimShotCalculated(shooterSubsystem, limelight)));
//     }
// }

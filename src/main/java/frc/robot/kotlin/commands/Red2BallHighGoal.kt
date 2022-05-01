package frc.robot.kotlin.commands

import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.wpilibj2.command.InstantCommand
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import frc.robot.commands.Autonomous.TimedAutoDrive
import frc.robot.subsystems.DrivetrainSubsystem
import frc.robot.subsystems.Limelight
import frc.robot.kotlin.subsystems.*


class RedTwoBallHighGoal(limelight: Limelight, shooter: Shooter, conveyor: Conveyor, intake:Intake, drivetrain: DrivetrainSubsystem) : SequentialCommandGroup() {
    init {
        addCommands(
                InstantCommand(intake::toggleIntakeState),
                ParallelDeadlineGroup(
                        TimedAutoDrive(drivetrain, ChassisSpeeds(1.0, 0.0, 0.0), 2.0, false),
                        InstantCommand({intake.moveIntake(false)}),
                ),
                InstantCommand(intake::toggleIntakeState),
                AimAndFire(limelight, shooter, conveyor, drivetrain),
                //InstantCommand({ if (intakeSubsystem.pistonsForward) intakeSubsystem.retractIntake() }), //Check the pistons
        )
    }
}
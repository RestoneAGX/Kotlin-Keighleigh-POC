package frc.robot.kotlin.commands

import edu.wpi.first.wpilibj2.command.InstantCommand
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup
import edu.wpi.first.wpilibj2.command.WaitCommand
import frc.robot.java.commands.AimBotAngle
import frc.robot.kotlin.subsystems.Conveyor
import frc.robot.kotlin.subsystems.Shooter
import frc.robot.kotlin.subsystems.Limelight
import frc.robot.java.DrivetrainSubsystem

class AimAndFire (limelight: Limelight, shooter:Shooter, conveyor: Conveyor, drivetrain: DrivetrainSubsystem): SequentialCommandGroup(){
    init {
        addCommands(
                ParallelDeadlineGroup(AimBotAngle(limelight, drivetrain)),
                InstantCommand(Runnable { shooter.setRPM(limelight.curveRPM()) }), //TODO: Change the target RPM
                WaitCommand(1.0),
                InstantCommand(Runnable { conveyor.moveConveyor(false) }),
                WaitCommand(0.5),
                InstantCommand(Runnable {
                    shooter.stopShooter()
                    conveyor.stopConveyor()
                })
        )
    }
}
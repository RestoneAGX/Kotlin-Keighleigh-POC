package frc.robot.kotlin.commands

import edu.wpi.first.wpilibj2.command.InstantCommand
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.WaitCommand
import frc.robot.kotlin.subsystems.Limelight
import frc.robot.kotlin.subsystems.Conveyor
import frc.robot.kotlin.subsystems.Shooter

class LowAuto(shooter: Shooter, conveyor: Conveyor, limelight: Limelight) : SequentialCommandGroup() {
    init {
        addCommands(
                InstantCommand({shooter.setRPM(limelight.curveRPM())}), //TODO: Replace CurveRPM if ! optimal
                WaitCommand(1.0),
                InstantCommand({conveyor.moveConveyor(false)}),
                WaitCommand(.5),
                InstantCommand({
                    shooter.stopShooter()
                    conveyor.stopConveyor()
                })
        )
    }
}
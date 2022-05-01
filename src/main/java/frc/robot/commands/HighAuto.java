package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.Deprecated.MoveConveyor;
import frc.robot.commands.Deprecated.ShooterOff;
import frc.robot.subsystems.ConveyorSubsystem;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.ShooterSubsystem;

public class HighAuto extends SequentialCommandGroup {

    public HighAuto(ShooterSubsystem shooterSubsystem, ConveyorSubsystem conveyorSubsystem, Limelight limelight) {
        addCommands(
            new AimShotPID(shooterSubsystem, 3900).withTimeout(2),
            new MoveConveyor(conveyorSubsystem).withTimeout(2),
            new ShooterOff(shooterSubsystem));
    }

}

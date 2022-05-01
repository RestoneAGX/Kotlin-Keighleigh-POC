package frc.robot.commands.Autonomous;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.commands.AimShotPID;
import frc.robot.commands.Deprecated.MoveConveyor;
import frc.robot.commands.Deprecated.ShooterOff;
import frc.robot.subsystems.ConveyorSubsystem;
import frc.robot.subsystems.ShooterSubsystem;

public class LowAuto extends SequentialCommandGroup {
    //running a sequential command group so each command
    //will run one at a time
    public LowAuto(ShooterSubsystem shooterSubsystem, ConveyorSubsystem conveyorSubsystem) {
        addCommands(
            new AimShotPID(shooterSubsystem, 1600).withTimeout(2),
            //the aimshot PID is set to the low scoring rpm
            new MoveConveyor(conveyorSubsystem).withTimeout(2),
            //moves conveyor for two seconds to ensure that the 
            //ball is expelled from the robot
            new ShooterOff(shooterSubsystem)
            //turns off shooter so as not to drain battery
            //uneccessarily
            );
        //the auto should ramp up the shooter to 1600rpm
        //then it should run the conveyor to spit out the preload 
        //into the lower hub.
        //then the robot should stop
    }

}

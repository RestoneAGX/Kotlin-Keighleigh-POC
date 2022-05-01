package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.*;
public class RetractHangSubsystem extends CommandBase{
    private HangSubsystem hangSubsystem;
    private double speed;

    public RetractHangSubsystem(HangSubsystem hangSubsystem, double speed){

        this.hangSubsystem = hangSubsystem;
        this.speed = speed;
      
        addRequirements(hangSubsystem);
    }
    @Override
    public void execute(){
        hangSubsystem.RetractHangSubsystem(speed);
    }

    @Override
    public void end(boolean interrupted) {
        hangSubsystem.StopHang();
    }
}

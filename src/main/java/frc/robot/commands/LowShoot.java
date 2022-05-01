package frc.robot.commands;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ShooterSubsystem;

public class LowShoot extends CommandBase {
    private ShooterSubsystem shooterSubsystem;
    private double speed = .7;
    public LowShoot(ShooterSubsystem shooterSubsystem){
        this.shooterSubsystem = shooterSubsystem;
        addRequirements(shooterSubsystem);
    }
    @Override
    public void execute(){
        shooterSubsystem.setVoltage(speed);
    }
    @Override
    public void end(boolean interrupted){
        shooterSubsystem.rampDownShooter();
    }
}

package frc.robot.commands;
import frc.robot.subsystems.ShooterSubsystem;
import edu.wpi.first.wpilibj2.command.CommandBase;

public class Shoot extends CommandBase {
    ShooterSubsystem shooter;
    private double power;
    
    public Shoot(ShooterSubsystem shooterSubsystem , double power) {
        this.shooter = shooterSubsystem;
        this.power = power;
        addRequirements(shooter);
    }

    @Override
    public void execute() {
        shooter.setRPM(power);
    }

    @Override
    public void end(boolean interrupted){
        shooter.rampDownShooter();
    }
}

package frc.robot.commands;

import edu.wpi.first.math.filter.SlewRateLimiter;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.DrivetrainSubsystem;

import java.util.function.DoubleSupplier;

public class DefaultDriveCommand extends CommandBase {
    private final DrivetrainSubsystem m_drivetrainSubsystem;

    private final DoubleSupplier m_translationXSupplier;
    private final DoubleSupplier m_translationYSupplier;
    private final DoubleSupplier m_rotationSupplier;
    private final SlewRateLimiter xLimiter = new SlewRateLimiter(5.5);
    private final SlewRateLimiter yLimiter = new SlewRateLimiter(5.5);

    public DefaultDriveCommand(DrivetrainSubsystem drivetrainSubsystem,
                               DoubleSupplier translationXSupplier,
                               DoubleSupplier translationYSupplier,
                               DoubleSupplier rotationSupplier) {
        this.m_drivetrainSubsystem = drivetrainSubsystem;
        this.m_translationXSupplier = translationXSupplier;
        this.m_translationYSupplier = translationYSupplier;
        this.m_rotationSupplier = rotationSupplier;

        addRequirements(drivetrainSubsystem);
    }

    @Override
    public void execute() {

        double xSpeed = xLimiter.calculate(m_translationXSupplier.getAsDouble());
        double ySpeed = yLimiter.calculate(m_translationYSupplier.getAsDouble());
        double rotationSpeed = m_rotationSupplier.getAsDouble();

        // double xSpeed = m_translationXSupplier.getAsDouble();
        // double ySpeed = m_translationYSupplier.getAsDouble();
        // You can use `new ChassisSpeeds(...)` for robot-oriented movement instead of field-oriented movement
        
        if(m_drivetrainSubsystem.getFieldOriented()) {
            m_drivetrainSubsystem.drive(ChassisSpeeds.fromFieldRelativeSpeeds(xSpeed, ySpeed, rotationSpeed, Rotation2d.fromDegrees(-m_drivetrainSubsystem.getGyroscopeRotation().getDegrees())));
        } else {
            m_drivetrainSubsystem.drive(new ChassisSpeeds(xSpeed, ySpeed, rotationSpeed));
        }
    }

    @Override
    public void end(boolean interrupted) {
        m_drivetrainSubsystem.drive(new ChassisSpeeds(0.0, 0.0, 0.0));
    }
}

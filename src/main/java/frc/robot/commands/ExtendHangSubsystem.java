package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.HangSubsystem;
import frc.robot.Constants.HangConstants;

public class ExtendHangSubsystem extends CommandBase {

    private HangSubsystem hangSubsystem;

    public ExtendHangSubsystem(HangSubsystem hangSubsystem) {
        this.hangSubsystem = hangSubsystem;
        addRequirements(hangSubsystem);
    }

    @Override
    public void initialize() {
        hangSubsystem.resetEncoder();
    }

    @Override
    public void execute() {
        hangSubsystem.ExtendHangSubsystem();
    }

    @Override
    public boolean isFinished() {
        return Math.abs(hangSubsystem.getEncoderPosition()) > HangConstants.LIFT_HEIGHT;
    }

    @Override
    public void end(boolean interrupted) {
        hangSubsystem.StopHang();
    }
}

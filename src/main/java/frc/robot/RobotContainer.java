// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Button;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import edu.wpi.first.wpilibj2.command.button.Trigger;
import frc.robot.Constants.ControllerConstants;
import frc.robot.Constants.HangConstants;
import frc.robot.Constants.LimelightConstants;
import frc.robot.Constants.ShooterConstants;
import frc.robot.commands.AimAndFire;
import frc.robot.commands.AimShotPID;
import frc.robot.commands.DefaultDriveCommand;
import frc.robot.commands.ExtendHangSubsystem;
import frc.robot.commands.IntakeBall;
import frc.robot.commands.Autonomous.LowAuto;
import frc.robot.commands.Autonomous.RedTwoBallHighGoal;
import frc.robot.commands.RetractHangSubsystem;
import frc.robot.commands.SpitOutBall;
import frc.robot.commands.SwitchPipeline;
import frc.robot.commands.ToggleCameraMode;
import frc.robot.commands.ToggleStreamMode;
import frc.robot.commands.Autonomous.TimedAutoDrive;
import frc.robot.commands.Deprecated.MoveConveyor;
import frc.robot.commands.Deprecated.ReverseIntake;
import frc.robot.subsystems.ConveyorSubsystem;
import frc.robot.subsystems.DrivetrainSubsystem;
import frc.robot.subsystems.HangSubsystem;
import frc.robot.subsystems.IntakeSubsystem;
import frc.robot.subsystems.Limelight;
import frc.robot.subsystems.ShooterSubsystem;
import frc.robot.util.DPadButton;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in
 * the {@link Robot}
 * periodic methods (other than the scheduler calls). Instead, the structure of
 * the robot (including
 * subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  private final DrivetrainSubsystem drivetrainSubsystem = new DrivetrainSubsystem();
  private final ConveyorSubsystem conveyorSubsystem = new ConveyorSubsystem();
  private final IntakeSubsystem intakeSubsystem = new IntakeSubsystem();
  private final HangSubsystem hangSubsystem = new HangSubsystem();
  private final XboxController driverController = new XboxController(0);
  private final XboxController mechanismController = new XboxController(1);
  private final ShooterSubsystem shooterSubsystem = new ShooterSubsystem();
  private final Limelight limelight = new Limelight(LimelightConstants.LedMode.DEFAULT,
      LimelightConstants.CamMode.VISION);
  // private final ColorSensor colorSensor = new ColorSensor();
  private SendableChooser<Command> autonomousChooser = new SendableChooser<Command>();

  /**
   * The container for the robot. Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Set up the default command for the drivetrain.
    // The controls are for field-oriented driving:
    // Left stick Y axis -> forward and backwards movement
    // Left stick X axis -> left and right movement
    // Right stick X axis -> rotation
    drivetrainSubsystem.setDefaultCommand(new DefaultDriveCommand(
        drivetrainSubsystem,
        () -> modifyAxis(driverController.getLeftY()) * DrivetrainSubsystem.MAX_VELOCITY_METERS_PER_SECOND,
        () -> modifyAxis(driverController.getLeftX()) * DrivetrainSubsystem.MAX_VELOCITY_METERS_PER_SECOND,
        () -> modifyAxis(driverController.getRightX()) * DrivetrainSubsystem.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND));
    SmartDashboard.putData("Toggle Camera Mode", new ToggleCameraMode(limelight));
    SmartDashboard.putData("Toggle Stream Mode", new ToggleStreamMode(limelight));
    SmartDashboard.putData("Switch Pipeline", new SwitchPipeline(limelight));
    SmartDashboard.putData("zero gyro", new InstantCommand(drivetrainSubsystem::zeroGyroscope));
    SmartDashboard.putData("reset odometry", new InstantCommand(drivetrainSubsystem::resetOdometry));
    setUpAutonomousChooser();
    // Configure the button bindings
    configureButtonBindings();
    CameraServer.startAutomaticCapture();
  }

  /**
   * Use this method to define your button->command mappings. Buttons can be
   * created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing
   * it to a {@link
   * edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    // set up triggers and such
    Trigger mechRightTrigger = new Trigger(() -> mechanismController
        .getRawAxis(ControllerConstants.RIGHT_TRIGGER) > ControllerConstants.TRIGGER_ACTIVATION_THRESHOLD);
    Trigger mechLeftTrigger = new Trigger(() -> mechanismController
        .getRawAxis(ControllerConstants.LEFT_TRIGGER) > ControllerConstants.TRIGGER_ACTIVATION_THRESHOLD);
    Trigger driverRightTrigger = new Trigger(() -> driverController
        .getRawAxis(ControllerConstants.RIGHT_TRIGGER) > ControllerConstants.TRIGGER_ACTIVATION_THRESHOLD);
    Trigger driverLeftTrigger = new Trigger(() -> driverController
        .getRawAxis(ControllerConstants.LEFT_TRIGGER) > ControllerConstants.TRIGGER_ACTIVATION_THRESHOLD);
    JoystickButton mechA = new JoystickButton(mechanismController, Button.kA.value);
    JoystickButton mechB = new JoystickButton(mechanismController, Button.kB.value);
    JoystickButton mechX = new JoystickButton(mechanismController, Button.kX.value);
    JoystickButton mechY = new JoystickButton(mechanismController, Button.kY.value);
    JoystickButton mechBumperR = new JoystickButton(mechanismController, Button.kRightBumper.value);
    JoystickButton mechBumperL = new JoystickButton(mechanismController, Button.kLeftBumper.value);
    JoystickButton driveBumperL = new JoystickButton(driverController, Button.kLeftBumper.value);
    JoystickButton driveBumperR = new JoystickButton(driverController, Button.kRightBumper.value);
    JoystickButton driverA = new JoystickButton(driverController, Button.kA.value);
    JoystickButton driverB = new JoystickButton(driverController, Button.kB.value);
    // JoystickButton driverY = new JoystickButton(driverController, Button.kY.value);
    JoystickButton driverX = new JoystickButton(driverController, Button.kX.value);
    DPadButton dPadUp = new DPadButton(driverController, DPadButton.Direction.UP);
    DPadButton dPadDown = new DPadButton(driverController, DPadButton.Direction.DOWN);

    mechBumperR.whileHeld(new IntakeBall(intakeSubsystem));
    mechRightTrigger.whileActiveContinuous(new MoveConveyor(conveyorSubsystem));
    mechLeftTrigger.whileActiveContinuous(new SpitOutBall(intakeSubsystem, conveyorSubsystem));
    mechY.whileHeld(new ReverseIntake(intakeSubsystem));

    // mechB.whenHeld(new AimShotPID(shooterSubsystem, 2500)).whenInactive(shooterSubsystem::rampDownShooter);

    // mechB.whenHeld(new InstantCommand(() -> shooterSubsystem.setRPM(limelight.curveRPM())))
    mechB.whileHeld(() -> shooterSubsystem.setRPM(limelight.curveRPM()))
    .whenInactive(shooterSubsystem::rampDownShooter);

    // mechX.whenHeld(new InstantCommand(() -> shooterSubsystem.setRPM(limelight.distanceToRpm())))
    mechX.whileHeld(() -> shooterSubsystem.setRPM(limelight.distanceToRpm()))
    .whenInactive(shooterSubsystem::rampDownShooter);

    // mechX.whenHeld(new AimShotPID(shooterSubsystem, ShooterConstants.LOWER_HUB_RPM), true)
    //     .whenInactive(shooterSubsystem::rampDownShooter);

    mechBumperL.whenPressed(new AimAndFire(shooterSubsystem, conveyorSubsystem, limelight, drivetrainSubsystem));

    mechA.whenPressed(new AimShotPID(shooterSubsystem, ShooterConstants.UPPER_HUB_FALLBACK_RPM))
        .whenInactive(shooterSubsystem::rampDownShooter);

    // driverA.whenPressed(new ControlIntakeSolenoids(intakeSubsystem));
    driverA.whenPressed(intakeSubsystem::controlIntakeSolenoids);

    dPadUp.whileHeld(new RetractHangSubsystem(hangSubsystem, HangConstants.HANG_SPEED)); // slowly make it go up
    dPadDown.toggleWhenActive(new RetractHangSubsystem(hangSubsystem, -0.1)); // hold the robot in position

    driveBumperR.whenPressed(new ExtendHangSubsystem(hangSubsystem));
    driveBumperL.whileActiveContinuous(new RetractHangSubsystem(hangSubsystem, HangConstants.LOWER_SPEED));

    driverB.whenPressed(new InstantCommand(drivetrainSubsystem::toggleFieldOriented)); //Maybe call directly
    driverX.whenPressed(new InstantCommand(drivetrainSubsystem::zeroGyroscope)); //Maybe call directly

    driverRightTrigger.whenActive(drivetrainSubsystem::turboSpeed).whenInactive(drivetrainSubsystem::standardSpeed);
    driverLeftTrigger.whenActive(drivetrainSubsystem::slowSpeed).whenInactive(drivetrainSubsystem::standardSpeed);
  }

  /**
   * Use this to pass thex autonomous command to the main {@link Robot} class.
   * 
   * @return the command to run in autonomous
   */

  public void setUpAutonomousChooser() {
    // autonomousChooser.setDefaultOption("SixBallAuto", new
    // SixBallAutoBlue(intakeSubsystem, limelight, drivetrainSubsystem,
    // shooterSubsystem));
    autonomousChooser.addOption("Low Auto", new LowAuto(shooterSubsystem, conveyorSubsystem));

    autonomousChooser.addOption("Throw it Back", new SequentialCommandGroup(
        new LowAuto(shooterSubsystem, conveyorSubsystem),
        new TimedAutoDrive(drivetrainSubsystem, new ChassisSpeeds(3, 0, 0), 1)));
    autonomousChooser.addOption("High Auto", new SequentialCommandGroup(
        new TimedAutoDrive(drivetrainSubsystem, new ChassisSpeeds(3, 0, 0), 1),
        new AimAndFire(shooterSubsystem, conveyorSubsystem, limelight, drivetrainSubsystem)));
    // autonomousChooser.addOption("Red 4 Ball", new
    // RedFourBallAuto(intakeSubsystem, limelight, drivetrainSubsystem,
    // shooterSubsystem, conveyorSubsystem));
    autonomousChooser.setDefaultOption("2 Ball High Auto",
        new RedTwoBallHighGoal(intakeSubsystem, drivetrainSubsystem, shooterSubsystem, limelight, conveyorSubsystem));
    SmartDashboard.putData("Autonomous Mode", autonomousChooser);
  }

  public Command getAutonomousCommand() {
    System.out.println("getAutonomousCommand");
    return autonomousChooser.getSelected();
  }

  private static double deadband(double value, double deadband) {
    if (Math.abs(value) > deadband) {
      if (value > 0.0)
        return (value - deadband) / (1.0 - deadband);
      return (value + deadband) / (1.0 - deadband);
    }
    return 0.0;
  }

  private static double modifyAxis(double value) {
    value = deadband(value, 0.05); // Deadband
    value = Math.copySign(value * value, value); // Square the axis
    return value;
  }
}

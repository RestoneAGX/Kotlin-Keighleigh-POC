package frc.robot

import edu.wpi.first.cameraserver.CameraServer
import edu.wpi.first.math.kinematics.ChassisSpeeds
import edu.wpi.first.wpilibj.XboxController
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.Command
import edu.wpi.first.wpilibj2.command.InstantCommand
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup
import edu.wpi.first.wpilibj2.command.button.JoystickButton
import edu.wpi.first.wpilibj2.command.button.Trigger
import frc.robot.Constants.*
import frc.robot.java.DrivetrainSubsystem
import frc.robot.java.commands.*
import frc.robot.kotlin.DPadButton
import frc.robot.kotlin.commands.AimAndFire
import frc.robot.kotlin.commands.LowAuto
import frc.robot.kotlin.commands.RedTwoBallHighGoal
import frc.robot.kotlin.subsystems.*
import kotlin.math.abs
import kotlin.math.withSign
@Suppress("NAME_SHADOWING")
class RobotContainer {
    private val drivetrainSubsystem = DrivetrainSubsystem()
    private val conveyor = Conveyor()
    private val shooter = Shooter()
    private val intake = Intake()
    private val hang = Hang()
    private val driverController = XboxController(0)
    private val mechanismController = XboxController(1)
    private val limelight = Limelight(LimelightConstants.LedMode.DEFAULT,
            LimelightConstants.CamMode.VISION)

    // private final ColorSensor colorSensor = new ColorSensor();
    private val autonomousChooser = SendableChooser<Command>()

    init {
        drivetrainSubsystem.defaultCommand = DefaultDriveCommand(
                drivetrainSubsystem,                                                                        // The controls are for field-oriented driving:
                { modifyAxis(driverController.leftY) * DrivetrainSubsystem.MAX_VELOCITY_METERS_PER_SECOND }, // Left stick Y axis -> forward and backwards movement
                { modifyAxis(driverController.leftX) * DrivetrainSubsystem.MAX_VELOCITY_METERS_PER_SECOND }  // Left stick X axis -> left and right movement
        ) { modifyAxis(driverController.rightX) * DrivetrainSubsystem.MAX_ANGULAR_VELOCITY_RADIANS_PER_SECOND } // Right stick X axis -> rotation

        SmartDashboard.putData("Toggle Camera Mode", InstantCommand(limelight::ToggleCameraMode))
        SmartDashboard.putData("Toggle Stream Mode", InstantCommand(limelight::ToggleStreamMode))
        SmartDashboard.putData("Switch Pipeline", InstantCommand(limelight::SwitchPipeline))
        //Add lidar support later

        SmartDashboard.putData("zero gyro", InstantCommand(drivetrainSubsystem::zeroGyroscope))
        // Test if runnable/function can be used to replace instead command in Shuffleboard
        SmartDashboard.putData("reset odometry", InstantCommand(drivetrainSubsystem::resetOdometry))

        setUpAutonomousChooser()
        configureButtonBindings() // Configure the button bindings
        CameraServer.startAutomaticCapture()
    }


    private fun configureButtonBindings() { // set up triggers and such
        val mechRightTrigger = Trigger {
            mechanismController
                    .getRawAxis(ControllerConstants.RIGHT_TRIGGER) > ControllerConstants.TRIGGER_ACTIVATION_THRESHOLD
        }
        val mechLeftTrigger = Trigger {
            mechanismController
                    .getRawAxis(ControllerConstants.LEFT_TRIGGER) > ControllerConstants.TRIGGER_ACTIVATION_THRESHOLD
        }
        val driverRightTrigger = Trigger {
            driverController
                    .getRawAxis(ControllerConstants.RIGHT_TRIGGER) > ControllerConstants.TRIGGER_ACTIVATION_THRESHOLD
        }
        val driverLeftTrigger = Trigger {
            driverController
                    .getRawAxis(ControllerConstants.LEFT_TRIGGER) > ControllerConstants.TRIGGER_ACTIVATION_THRESHOLD
        }
        val mechA = JoystickButton(mechanismController, XboxController.Button.kA.value)
        val mechB = JoystickButton(mechanismController, XboxController.Button.kB.value)
        val mechX = JoystickButton(mechanismController, XboxController.Button.kX.value)
        val mechY = JoystickButton(mechanismController, XboxController.Button.kY.value)
        val mechBumperR = JoystickButton(mechanismController, XboxController.Button.kRightBumper.value)
        val mechBumperL = JoystickButton(mechanismController, XboxController.Button.kLeftBumper.value)

        val driverA = JoystickButton(driverController, XboxController.Button.kA.value)
        val driverB = JoystickButton(driverController, XboxController.Button.kB.value)
        // val driverY = JoystickButton(driverController, XboxController.Button.kY.value)
        val driverX = JoystickButton(driverController, XboxController.Button.kX.value)
        val driveBumperL = JoystickButton(driverController, XboxController.Button.kLeftBumper.value)
        val driveBumperR = JoystickButton(driverController, XboxController.Button.kRightBumper.value)

        val dPadUp = DPadButton(driverController, DPadButton.Direction.UP)
        val dPadDown = DPadButton(driverController, DPadButton.Direction.DOWN)

        // BUTTON BINDINGS
        mechLeftTrigger.whileActiveContinuous(Runnable{
            intake.moveIntake(true)
            conveyor.moveConveyor(true)
        }).whenInactive(Runnable{
            intake.stopIntake()
            conveyor.stopConveyor()
        })

        mechRightTrigger.whileActiveContinuous(Runnable {conveyor.moveConveyor(false)})
                .whenInactive(conveyor::stopConveyor)

        mechBumperL.whenPressed(AimAndFire(limelight, shooter, conveyor, drivetrainSubsystem))

        mechBumperR.whileHeld(Runnable{ intake.moveIntake(false)})
                .whenInactive(intake::stopIntake)

        mechY.whileHeld(Runnable{intake.moveIntake(true)})
                .whenInactive(intake::stopIntake)

        mechB.whileHeld(Runnable { shooter.setRPM(limelight.curveRPM()) })
                .whenInactive(shooter::stopShooter)

        mechX.whileHeld(Runnable { shooter.setRPM(limelight.distanceToRpm()) })
                .whenInactive(shooter::stopShooter)

        mechA.whenPressed(Runnable{ shooter.setRPM(ShooterConstants.UPPER_HUB_FALLBACK_RPM)})
                .whenInactive(shooter::stopShooter)

        dPadUp.whileHeld(Runnable{hang.moveHang(HangConstants.HANG_SPEED)})    // slowly make it go up
        dPadDown.toggleWhenActive(InstantCommand({hang.moveHang(-0.1)})) // hold the robot in position

        driverRightTrigger.whenActive(drivetrainSubsystem::turboSpeed).whenInactive(drivetrainSubsystem::standardSpeed)
        driverLeftTrigger.whenActive(drivetrainSubsystem::slowSpeed).whenInactive(drivetrainSubsystem::standardSpeed)

        driveBumperR.whenPressed(Runnable{ hang.moveHang(HangConstants.HANG_SPEED)}) //TODO:replace with command if neccessary; Remove if permitted
        driveBumperL.whileActiveContinuous(Runnable{hang.moveHang(HangConstants.LOWER_SPEED)}) //TODO: remove if permitted

        driverX.whenPressed(drivetrainSubsystem::zeroGyroscope)
        driverB.whenPressed(drivetrainSubsystem::toggleFieldOriented)
        driverA.whenPressed(intake::toggleIntakeState)
    }

    fun setUpAutonomousChooser() {
        // autonomousChooser.setDefaultOption("SixBallAuto", new
        // SixBallAutoBlue(intakeSubsystem, limelight, drivetrainSubsystem,
        // shooterSubsystem));
        // autonomousChooser.addOption("Red 4 Ball", new
        // RedFourBallAuto(intakeSubsystem, limelight, drivetrainSubsystem,
        // shooterSubsystem, conveyorSubsystem));

        autonomousChooser.addOption("Low Auto", LowAuto(shooter, conveyor, limelight))

        autonomousChooser.addOption("Throw it Back", SequentialCommandGroup(
                LowAuto(shooter, conveyor, limelight),
                TimedAutoDrive(drivetrainSubsystem, ChassisSpeeds(3.0, 0.0, 0.0), 1.0)))

        autonomousChooser.addOption("High Auto", SequentialCommandGroup(
                TimedAutoDrive(drivetrainSubsystem, ChassisSpeeds(3.0, 0.0, 0.0), 1.0),
                AimAndFire(limelight, shooter, conveyor, drivetrainSubsystem)))

        autonomousChooser.setDefaultOption("2 Ball High Auto",
                RedTwoBallHighGoal(limelight, shooter, conveyor, intake, drivetrainSubsystem))
        SmartDashboard.putData("Autonomous Mode", autonomousChooser)
    }

    val autonomousCommand: Command
        get() {
            println("getAutonomousCommand")
            return autonomousChooser.selected
        }

    companion object {
        private fun deadband(value: Double, deadband: Double): Double {
            return if (abs(value) > deadband) {
                if (value > 0.0) (value - deadband) / (1.0 - deadband) else (value + deadband) / (1.0 - deadband)
            } else 0.0
        }

        private fun modifyAxis(value: Double): Double {
            var value = deadband(value, 0.05) // Deadband
            value = (value * value).withSign(value) // Square the axis
            return value
        }
    }
}
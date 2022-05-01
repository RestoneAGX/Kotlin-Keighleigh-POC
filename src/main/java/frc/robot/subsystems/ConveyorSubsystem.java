package frc.robot.subsystems;

import frc.robot.Constants.*;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import com.revrobotics.*;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class ConveyorSubsystem extends SubsystemBase {
    private DigitalInput toplimitSwitch = new DigitalInput(0);
    private CANSparkMax lowerConveyor = new CANSparkMax(ConveyorConstants.LOWER_CONVEYOR, MotorType.kBrushless);
    // private ColorSensor colorSensor = new ColorSensor();

    public ConveyorSubsystem() {
        lowerConveyor.setIdleMode(IdleMode.kCoast);
        // upperConveyor.setIdleMode(IdleMode.kCoast);
    }

    public void moveConveyor(double speed) {
        lowerConveyor.set(speed);
        // upperConveyor.set(speed);
    }

    public void stopConveyor() {
        // upperConveyor.set(0);
        lowerConveyor.set(0);
    }

    public void takeInBall(double speed) {
        if (toplimitSwitch.get()) {
            stopConveyor();
        } else {
            // upperConveyor.set(speed);
            lowerConveyor.set(speed);
        }
    }

   //public int countBalls() {
        // if (colorSensor.occupiedUpper() && colorSensor.occupiedLower()) {
        //     return 2;
        // } else if (colorSensor.occupiedLower() || colorSensor.occupiedUpper()) {
        //     return 1;
        // }
        // return 0;
   // }

    @Override
    public void periodic() {
        // SmartDashboard.putNumber("Number of Balls in Conveyor", countBalls());
    }
}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotState;

import com.revrobotics.ColorSensorV3;

public class ColorSensor extends SubsystemBase {

  I2C.Port port1 = I2C.Port.kOnboard; //Might want to replace with PWM in-direct for I2C
  I2C.Port port2 = I2C.Port.kMXP;

  ColorSensorV3 colorSensorLower = new ColorSensorV3(port1);
  ColorSensorV3 colorSensorUpper = new ColorSensorV3(port2);

  private double proxyCheck = 0; //Placeholder value for now

  public boolean occupiedLower(){
    return (colorSensorLower.getProximity() > proxyCheck);
  }

  public boolean occupiedUpper() {
    return (colorSensorUpper.getProximity() > proxyCheck);
  }

  public Color getColorUpper() {
    return colorSensorUpper.getColor();
  }

  public Color getColorLower() {
    return colorSensorLower.getColor();
  }

  public ColorSensor() {}

  @Override
  public void periodic(){
    //Possibly add a listener to remove the need to constantly use periodic
    RobotState.setEntryValue("Sensors", "Lower Occupied", occupiedLower());
    RobotState.setEntryValue("Sensors", "Upper Occupied", occupiedUpper());

    RobotState.setEntryValue("Sensors", "Lower Proximity", colorSensorLower.getProximity());
    RobotState.setEntryValue("Sensors", "Upper Proximity", colorSensorUpper.getProximity());

    //[Note]: Current alliance can be published to NetworkTables
  }
}
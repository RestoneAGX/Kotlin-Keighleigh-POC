// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotState;
import frc.robot.Constants.ConveyorConstants;
import frc.robot.subsystems.ConveyorSubsystem;

public class AdjustConveyor extends CommandBase {
  ConveyorSubsystem conveyorSubsystem;
  // ColorSensor colorSensor;

  public AdjustConveyor(ConveyorSubsystem conveyorSubsystem) {
    addRequirements(conveyorSubsystem);
    this.conveyorSubsystem = conveyorSubsystem;
    // this.colorSensor = colorSensor;
  }

  @Override
  public void execute() {
    if (RobotState.getEntryValue("Sensors", "Upper Occupied").getBoolean()) // you can turn it into a variable if readablity is a problem [Iida]
      conveyorSubsystem.moveConveyor(ConveyorConstants.CONVEYOR_SPEED * -1);
    // if (colorSensor.occupiedUpper())
    // conveyorSubsystem.moveConveyor(ConveyorConstants.CONVEYOR_SPEED * -1);
  }

  @Override
  public void end(boolean interrupted) {
    conveyorSubsystem.stopConveyor();
  }

  @Override
  public boolean isFinished() {
  // return (colorSensor.occupiedLower() && !colorSensor.occupiedUpper());
  // This definitely is a readability nightmare
  return RobotState.getEntryValue("Sensors", "Upper Occupied").getBoolean() && !RobotState.getEntryValue("Sensors", "Lower Occupied").getBoolean();
  }
}

// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.function.Consumer;
import edu.wpi.first.networktables.EntryNotification;
import edu.wpi.first.networktables.NetworkTableValue;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.NetworkTableInstance;

public class RobotState {
  static NetworkTableInstance mainTable = NetworkTableInstance.getDefault();

  public static void setEntryValue(String Table, String Entry, Object val){
    mainTable.getTable(Table).getEntry(Entry).setValue(val);
  }

  public static NetworkTableValue getEntryValue(String Table, String Entry){
    return mainTable.getTable(Table).getEntry(Entry).getValue();
  }

  public static NetworkTableValue[] getEntriesValues(String Table, String ...Entries){
    NetworkTableValue[] values = new NetworkTableValue[Entries.length];
    for (int i = 0; i < Entries.length; i++)
      values[i] = mainTable.getTable(Table).getEntry(Entries[i]).getValue();
      
    return values;
  }

  public static void setEntryListener(String Table, String Entry, Consumer<EntryNotification> event){
    mainTable.getTable(Table).getEntry(Entry).addListener(event, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
  }
}

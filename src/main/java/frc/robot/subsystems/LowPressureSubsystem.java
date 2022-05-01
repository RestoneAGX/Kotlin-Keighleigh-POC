package frc.robot.subsystems;

// import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PneumaticHub;
// import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.LowPressureConstants;

public class LowPressureSubsystem extends SubsystemBase {

  private DigitalInput pressureSensor = new DigitalInput(LowPressureConstants.PRESSURE_SENSOR_CHANNEL);
  // private Compressor compressor = new Compressor(1, PneumaticsModuleType.REVPH);
  public PneumaticHub ph = new PneumaticHub();

  public LowPressureSubsystem() {
  }

  @Override
  public void periodic() {
    //double pressure = 250.0 * pressureSensor.getVoltage() / 5.0 - 25.0;
    boolean pressure = pressureSensor.get();
    //ph.enableCompressorAnalog(100, 120);
    SmartDashboard.putBoolean("Low Pressure", pressure);
  }
}
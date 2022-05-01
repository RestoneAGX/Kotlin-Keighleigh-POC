package frc.robot.kotlin.subsystems

import edu.wpi.first.networktables.NetworkTable
import edu.wpi.first.networktables.NetworkTableInstance
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard
import edu.wpi.first.wpilibj2.command.SubsystemBase
import frc.robot.Constants.LimelightConstants

class Limelight @JvmOverloads constructor(var ledMode: Int = LimelightConstants.LedMode.DEFAULT, var streamMode: Int = LimelightConstants.CamMode.VISION) : SubsystemBase() {
    // basically a struct that contains all of the targetData we're pulling from the limelight
    class TargetData {
        var hasTargets = false
        var horizontalOffset = 0.0 // Horizontal Offset From Crosshair To Target -29.8 to 29.8 degrees
        var verticalOffset = 0.0 // Vertical Offset From Crosshair To Target -24.85 to 24.85 degrees
        var targetArea = 0.0 // 0% of image to 100% of image
        var skew = 0.0 // -90 degrees to 0 degree
        var latency = 0.0 // The pipeline’s latency contribution (ms) Add at least 11ms for image capture latency.
        var shortSideLength = 0.0 // Sidelength of shortest side of the fitted bounding box (pixels)
        var longSideLength = 0.0 // Sidelength of longest side of the fitted bounding box (pixels)
        var horizontalSideLength = 0.0 // Horizontal sidelength of the rough bounding box (0 - 320 pixels)
        var verticalSideLength = 0.0 // Vertical sidelength of the rough bounding box (0 - 320 pixels)
    }

    // global (sort of) data object with up to date limelight data
    val targetData: TargetData = TargetData()

    override fun periodic() {
        val table = NetworkTableInstance.getDefault().getTable("limelight")
        // final NetworkTableEntry tx = table.getEntry("tx");
        // final NetworkTableEntry ty = table.getEntry("ty");
        // final NetworkTableEntry ta = table.getEntry("ta");

        // // read values periodically
        // double x = tx.getDouble(0.0);
        // double y = ty.getDouble(0.0);
        // double area = ta.getDouble(0.0);
        val stream: String
        val cam: String
        stream = if (streamMode == LimelightConstants.StreamMode.PIP_MAIN) "Main" else "Secondary"
        cam = if (cameraMode == LimelightConstants.CamMode.VISION) "Vision" else "Driver"

        // post to smart dashboard periodically
        // SmartDashboard.putNumber("LimelightX", x);
        // SmartDashboard.putNumber("LimelightY", y);
        // SmartDashboard.putNumber("LimelightArea", area);
        SmartDashboard.putString("Stream Mode", stream)
        SmartDashboard.putString("Camera Mode", cam)
        SmartDashboard.putNumber("Distance", distance)
        //Test these 2 against each other to finally settle the issue
        SmartDashboard.putNumber("Curve RPM", curveRPM())
        SmartDashboard.putNumber("Line RPM", distanceToRpm())
        updateTargetData(table)
    }

    private fun updateTargetData(table: NetworkTable) {
        targetData.hasTargets = table.getEntry("tv").getBoolean(false)
        targetData.horizontalOffset = table.getEntry("tx").getDouble(0.0)
        targetData.verticalOffset = table.getEntry("ty").getDouble(0.0)
        targetData.targetArea = table.getEntry("ta").getDouble(0.0)
        targetData.skew = table.getEntry("ts").getDouble(0.0)
        targetData.latency = table.getEntry("tl").getDouble(0.0)
        targetData.shortSideLength = table.getEntry("tshort").getDouble(0.0)
        targetData.longSideLength = table.getEntry("tlong").getDouble(0.0)
        targetData.horizontalSideLength = table.getEntry("thor").getDouble(0.0)
        targetData.verticalSideLength = table.getEntry("tvert").getDouble(0.0)
    }

    fun distanceToRpm(): Double {
        val distance = distance
        //double squared = distance * distance;
        //double factor = 0.00714 * squared;
        val factor = 2.67 * distance // y = 3.4*x + 2392
        return factor + 2805
    }

    fun curveRPM(): Double {
        val distance = SmartDashboard.getNumber("Distance", 0.0)
        val squared = distance * distance
        return 0.00922451 * squared + -2.11957 * distance + 3450.01
    }// UpperHub height in inches converted to cm

    // would return negative values if the angle was negative
    // This works only for objects that are above or below the robot
    // It's very inaccurate of objects that are same height as the robot
    val distance: Double
        get() {
            val targetData = targetData
            val a2 = targetData.verticalOffset
            val a1 = LimelightConstants.LIMELIGHT_ANGLE
            val h1 = LimelightConstants.LIMELIGHT_HEIGHT
            val h2 = 103 * 2.54 // UpperHub height in inches converted to cm
            val result = h2 - h1
            val radians = Math.toRadians(a1 + a2)
            val distance = result / Math.tan(radians)
            return Math.abs(distance) // would return negative values if the angle was negative
        }

    var cameraMode: Int
        get() = NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").getDouble(0.0).toInt()
        set(newCameraMode) {
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("camMode").setNumber(newCameraMode)
        }
    var pipeline: Int
        get() = NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").getDouble(0.0).toInt()
        set(newPipeline) {
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("pipeline").setNumber(newPipeline)
        }
    var snapshotMode: Int
        get() = NetworkTableInstance.getDefault().getTable("limelight").getEntry("snapshot").getDouble(0.0).toInt()
        set(newSnapshotMode) {
            NetworkTableInstance.getDefault().getTable("limelight").getEntry("snapshot").setNumber(newSnapshotMode)
        }
}
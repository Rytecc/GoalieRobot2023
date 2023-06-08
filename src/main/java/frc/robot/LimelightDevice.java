package frc.robot;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableInstance;

public class LimelightDevice {
    private NetworkTable mainTable;

    public LimelightDevice() {
        mainTable = NetworkTableInstance.getDefault().getTable("limelight");
    }

    public void setCameraLightState(boolean on) {
        mainTable.getEntry("ledMode").setNumber(on ? 3 : 1);
    }

    /**
     * 0 = x
     * 1 = y
     * 2 = area
     */
    public double[] getLimelightCurrentData() {
        return new double[] {
            mainTable.getEntry("tx").getDouble(0),
            mainTable.getEntry("ty").getDouble(0),
            mainTable.getEntry("ta").getDouble(0)
        };
    }
}

package frc.robot;

import java.util.function.Supplier;
import edu.wpi.first.wpilibj.Joystick;

public class Constants {
    /* DASHBOARD CONSTANTS */
    public static final String kDashboardManualACtionAllowed = "Manual Cooldown State";

    /* ROBORIO PORT CONSTANTS */
    public static final int kDriveTopLeft = 0;
    public static final int kDriveBottomLeft = 1;
    public static final int kDriveTopRight = 2;
    public static final int kDriveBottomRight = 3;
    public static final int kLaserEyePWMPort = 4;

    /* MANUAL INPUT CONSTANTS */
    public static final Joystick kMainController = new Joystick(0);

    public static final Supplier<Double> kDriveX = () -> {
        return kMainController.getX();
    };

    public static final Supplier<Double> kDriveY = () -> {
        return kMainController.getY();
    };

    public static final double kTimeoutSeconds = 1.0;
    public static final int kButton1 = 1;
    public static final int kButton2 = 2;
    public static final int kButton3 = 3;
    public static final int kButton4 = 4;
    public static final int kButton5 = 5;
    
    public static final int kAutoSwitch = 6;
    public static final int kFlipManualSwitch = 7;
    public static final int kToggleLaserEyes = 8;
}
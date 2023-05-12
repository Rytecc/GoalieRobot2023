package frc.robot;

import java.util.function.Supplier;
import edu.wpi.first.wpilibj.Joystick;

public class Constants {
    /* DASHBOARD CONSTANTS */
    public static final String kDashboardManualActionAllowed = "Manual Cooldown State";

    /* ROBORIO PORT CONSTANTS */
    public static final int kDriveTopLeft = 0;
    public static final int kDriveBottomLeft = 1;
    public static final int kDriveTopRight = 2;
    public static final int kDriveBottomRight = 3;
    public static final int kLaserEyePWMPort = 4;

    /* MANUAL INPUT CONSTANTS */
    public static final Joystick kMainController = new Joystick(0);

    public static final Supplier<Double> kDriveX = () -> {
        double absAxis = Math.abs(kMainController.getX());
        return absAxis > 0.075 ? kMainController.getX() : 0;
    };

    public static final Supplier<Double> kDriveY = () -> {
        double absAxis = Math.abs(kMainController.getY());
        return absAxis > 0.075 ? kMainController.getY() : 0;
    };

    public static final double kTimeoutSeconds = 0.1;
    public static final int kButton1 = 7;
    public static final int kButton2 = 8;
    public static final int kButton3 = 9;
    public static final int kButton4 = 10;
    public static final int kButton5 = 11;
    public static final int kButton6 = 12;
    
    public static final int kAutoSwitch = 3;
    public static final int kFlipManualSwitch = 5;
    public static final int kToggleLaserEyes = 6;

    /* CODE ROBOT PARAMETER CONSTANTS */
    public static final double kDriveSpeed = 0.6;
}
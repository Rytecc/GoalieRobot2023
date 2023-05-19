package frc.robot;

import java.util.function.Supplier;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.PneumaticsModuleType;
import edu.wpi.first.wpilibj.Solenoid;

public class Constants {
    /* DASHBOARD CONSTANTS */
    public static final String kDashboardIsManualFlipped = "Solenoid Inputs Flipped:";
    public static final String kDashboardIsAutoSwitchOn = "Limelight Auto Mode State:";

    /* ROBORIO PORT CONSTANTS */
    public static final int kDriveTopLeft = 0;
    public static final int kDriveBottomLeft = 1;
    public static final int kDriveTopRight = 2;
    public static final int kDriveBottomRight = 3;
    public static final int kLaserEyePWMPort = 4;

    /* SOLENOID IDs ARE SET UP TO WHERE GOALIE IS VIEWED ON BACKSIDE */
    public static final Solenoid kHead = new Solenoid(PneumaticsModuleType.CTREPCM, 3);
    public static final Solenoid kLeftArm = new Solenoid(PneumaticsModuleType.CTREPCM, 5);
    public static final Solenoid kRightArm = new Solenoid(PneumaticsModuleType.CTREPCM, 4);
    public static final Solenoid kRightKicker = new Solenoid(PneumaticsModuleType.CTREPCM, 1);
    public static final Solenoid kLeftKicker = new Solenoid(PneumaticsModuleType.CTREPCM, 2);

    public static final Solenoid[] kSolenoids = new Solenoid[] {
        kHead, kLeftArm, kRightArm, kLeftKicker, kRightKicker
    };
    /* MANUAL INPUT CONSTANTS */
    public static final Joystick kMainController = new Joystick(0);
    public static final Joystick kCoController = new Joystick(1);

    public static final Supplier<Double> kDriveX = () -> {
        double absAxis = Math.abs(kMainController.getX());
        return absAxis > 0.075 ? kMainController.getX() : 0;
    };

    public static final Supplier<Double> kDriveY = () -> {
        double absAxis = Math.abs(kMainController.getY());
        return absAxis > 0.075 ? kMainController.getY() : 0;
    };

    public static final double kTimeoutSeconds = 0.1;
    public static final int kButton1 = 1;
    public static final int kButton2 = 2;
    public static final int kButton3 = 3;
    public static final int kButton4 = 4;
    public static final int kButton5 = 6;
    
    public static final int kAutoSwitch = 7;
    public static final int kFlipManualSwitch = 8;
    public static final int kToggleLaserEyes = 5;

    /* CODE ROBOT PARAMETER CONSTANTS*/
    public static final double kDriveSpeed = 0.5;
}
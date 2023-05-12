package frc.robot;

import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import static frc.robot.Constants.*;

/*
 * Author: Lucas Soliman
 * Date Created: May 9, 2023
 * 
 * This class contains functionality for robot driving
 */
public class RobotDrive {
    private final DifferentialDrive driveInstance;

    public RobotDrive(int tl, int bl, int tr, int br) {
        MotorControllerGroup leftGroup = new MotorControllerGroup(new PWMSparkMax(tl), new PWMSparkMax(bl));
        MotorControllerGroup rightGroup = new MotorControllerGroup(new PWMSparkMax(tr), new PWMSparkMax(br));

        driveInstance = new DifferentialDrive(leftGroup, rightGroup);
    }

    public void runDrive() {
        driveInstance.arcadeDrive(kMainController.getX() * kDriveSpeed, kMainController.getY() * kDriveSpeed);
    }
}

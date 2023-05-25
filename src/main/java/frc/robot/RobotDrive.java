package frc.robot;

import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;
import edu.wpi.first.wpilibj.motorcontrol.PWMSparkMax;
import static frc.robot.Constants.*;

import java.util.function.Supplier;

/*
 * Author: Lucas Soliman
 * Date Created: May 9, 2023
 * 
 * This class contains functionality for robot driving
 */
public class RobotDrive {
    
    private Supplier<Double> driveX, driveY;
    public RobotDrive(Supplier<Double> driveX, Supplier<Double> driveY) {
        this.driveX = driveX;
        this.driveY = driveY;
    }

    public void runDrive() {
        driveInstance.arcadeDrive(driveX.get() * kDriveSpeed, driveY.get() * kDriveSpeed);
    }
}
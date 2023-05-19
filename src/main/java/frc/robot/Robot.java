package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static frc.robot.Constants.*;
/*
 * Author: Lucas Soliman
 * Date Created: May, 4, 2o23
 * 
 * This is the main robot file that runs the goalie robot for the TSN Funny thing (:
 */
public class Robot extends TimedRobot {
  public ButtonPistonAction[] PistonActions = null;
  public ButtonPistonAction[] FlipPistonActions = null;

  private LaserEyes laserEyes;
  private LimelightDevice limelightCamera;
  private RobotDrive mainDriver;

  @Override
  public void robotInit() {
    mainDriver = new RobotDrive(kDriveTopLeft, kDriveBottomLeft, kDriveTopRight, kDriveBottomRight); //comment to disable driving
    laserEyes = new LaserEyes(kCoController, 4, 2); //comment to disable laser eyes

    PistonActions = new ButtonPistonAction[] {
      new ButtonPistonAction(kMainController, Constants.kButton1, kLeftKicker),
      new ButtonPistonAction(kMainController, Constants.kButton2, kLeftArm),
      new ButtonPistonAction(kMainController, Constants.kButton3, kRightKicker),
      new ButtonPistonAction(kMainController, Constants.kButton4, kRightArm),
      new ButtonPistonAction(kMainController, Constants.kButton5, kHead)
    };

    FlipPistonActions = new ButtonPistonAction[] {
      new ButtonPistonAction(kMainController, Constants.kButton3, kLeftKicker),
      new ButtonPistonAction(kMainController, Constants.kButton4, kLeftArm),
      new ButtonPistonAction(kMainController, Constants.kButton1, kRightKicker),
      new ButtonPistonAction(kMainController, Constants.kButton2, kRightArm),
      new ButtonPistonAction(kMainController, Constants.kButton5, kHead)
    };
  }

  @Override
  public void robotPeriodic() {
    laserEyes.tickEyes(); //comment to disable laser eyes
    SmartDashboard.putBoolean(kDashboardIsAutoSwitchOn, kMainController.getRawButton(kAutoSwitch));
    SmartDashboard.putBoolean(kDashboardIsManualFlipped, kMainController.getRawButton(kFlipManualSwitch));
  }

  @Override
  public void teleopPeriodic() {
    if(kMainController.getRawButton(kAutoSwitch)) {
      cameraGoalie();
    } else {
      manualGoalie();
    }

    mainDriver.runDrive(); //comment to disable driving
  }

  //TODO: TEST THIS FUNCTION
  private void cameraGoalie() {
    double[] data = limelightCamera.getLimelightCurrentData();
    boolean isLeft = data[0] < 0;
    boolean isRight = data[0] > 0;

    boolean isAbove = data[1] > 0;
    boolean isBelow = data[1] < 0;

    System.out.println("{" + isLeft + ":" + isRight + "}");
    System.out.println("{" + isAbove + ":" + isBelow + "}");

    if(isLeft) {
      if(isAbove) {
        disableAllSolenoid(kLeftArm);
        kLeftArm.set(true);
      }

      if(isBelow) {
        disableAllSolenoid(kLeftKicker);
        kLeftKicker.set(true);
      }

    } else if(isRight) {
      if(isAbove) {
        disableAllSolenoid(kRightArm);
        kRightArm.set(true);
      }

      if(isBelow) {
        disableAllSolenoid(kRightKicker);
        kRightKicker.set(true);
      }
    }
  }

  private void manualGoalie() {
    for(ButtonPistonAction action : kMainController.getRawButton(kFlipManualSwitch) ? FlipPistonActions : PistonActions) {
      if(action.buttonPressed()) {
        action.setOn();
      } else {
        action.setOff();
      }
    }
  }

  private void disableAllSolenoid(Solenoid omit) {
    for(Solenoid s : kSolenoids) {
      if(s == omit) {
        continue;
      }

      s.set(false);
    }
  }
}
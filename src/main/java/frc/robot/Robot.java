package frc.robot;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.util.*;

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

  private final boolean debugLimeLight = true;

  private LaserEyes laserEyes;
  private LimelightDevice limelightCamera;
  private RobotDrive mainDriver;
  private RobotDrive coDriver;
  private boolean autoReady = false;
  @Override
  public void robotInit() {
    limelightCamera = new LimelightDevice();
    mainDriver = new RobotDrive(() -> { return kMainController.getY(); }, () -> { return kMainController.getX(); }, kCreepDriveSpeed); //comment to disable driving
    coDriver = new RobotDrive(() -> {return kSideController.getLeftY();}, () -> {return kSideController.getRightX();}, kDriveSpeed);
    laserEyes = new LaserEyes(kSideController, 4, 2); //comment to disable laser eyes

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
    if(!kCompressor.getPressureSwitchValue()) {
      kCompressor.enableDigital();
      System.out.println("Compressor On");
    } else {
      kCompressor.disable();
      System.out.println("Compressor Off");
    }

    if(kMainController.getRawButton(kAutoSwitch)) {
      if(kMainController.getRawButtonPressed(kReadyAutoSwitch)) {
        disableAllSolenoid(null);
        autoReady = true;
      }

      if(autoReady) {
        cameraGoalie();
      } else {
        disableAllSolenoid(null);
      }
    } else {
      manualGoalie();
    }

    mainDriver.runDrive(); //comment to disable driving
    coDriver.runDrive(); //comment to disable copilot driving
  }

  //TODO: REFINE THIS FUNCTION!
  private final double areaThreshold = 0.005;
  private final double boxPaddingX = 5.0;
  private final double boxPaddingY = 3.0;
  private final Vector leftKickerPadding = new Vector(boxPaddingX, boxPaddingY);
  private final Vector rightKickerPadding = new Vector(boxPaddingX, boxPaddingY);
  private final Vector leftArmPadding = new Vector(boxPaddingX, boxPaddingY);
  private final Vector rightArmPadding = new Vector(boxPaddingX, boxPaddingY);

  private final Rect leftKickerRect = new Rect(-30, -20, 30 - leftKickerPadding.x, 20 - leftKickerPadding.y);
  private final Rect rightKickerRect = new Rect(rightKickerPadding.x, -20, 30 - rightKickerPadding.x, 20 - rightKickerPadding.y);
  private final Rect leftArmRect = new Rect(-30, leftArmPadding.y, 30 - leftArmPadding.x, 20 - leftArmPadding.y);
  private final Rect rightArmRect = new Rect(rightArmPadding.x, rightArmPadding.y, 30 - rightArmPadding.x, 20 - rightArmPadding.y);

  private void cameraGoalie() {
    double[] data = limelightCamera.getLimelightCurrentData();
    boolean autoActed = false;
    
    if(debugLimeLight) {
      SmartDashboard.putNumber("LimeLight X: ", data[0]);
      SmartDashboard.putNumber("LimeLight Y: ", data[1]);
      SmartDashboard.putNumber("Limelight A: ", data[2]);
    }
    
    if(data[2] >= areaThreshold) {
      if(leftKickerRect.pointInRect(data[0], data[1])) {
        SmartDashboard.putString("Current Commit Place: ", "Left Kicker");
        disableAllSolenoid(kLeftKicker);
        kLeftKicker.set(true);
        autoActed = true;
      } else if(rightKickerRect.pointInRect(data[0], data[1])) {
        SmartDashboard.putString("Current Commit Place: ", "Right Kicker");
        disableAllSolenoid(kRightKicker);
        kRightKicker.set(true);
        autoActed = true;
      } else if(leftArmRect.pointInRect(data[0], data[1])) {
        SmartDashboard.putString("Current Commit Place: ", "Left Arm");
        disableAllSolenoid(kLeftArm);
        kLeftArm.set(true);
        autoActed = true;
      } else if(rightArmRect.pointInRect(data[0], data[1])) {
        SmartDashboard.putString("Current Commit Place: ", "Right Arm");
        disableAllSolenoid(kRightArm);
        kRightArm.set(true);
        autoActed = true;
      } else {
        SmartDashboard.putString("Current Commit Place: ", "Deadzone");
      }
    }

    if(autoActed) {
      autoReady = false;
    }

    /** Old goalie logic
    boolean isLeft = data[0] < 0;
    boolean isRight = data[0] > 0;

    boolean isAbove = data[1] > 0;
    boolean isBelow = data[1] < 0;

    if(isLeft) {
      if(isAbove) {

      }

      if(isBelow) {

      }

    } else if(isRight) {
      if(isAbove) {

      }

      if(isBelow) {

      }
    }
    */
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
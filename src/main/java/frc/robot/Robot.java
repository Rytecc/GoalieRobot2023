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
  private int autoTimeout = 0;

  @Override
  public void robotInit() {
    limelightCamera = new LimelightDevice();
    mainDriver = new RobotDrive(() -> { return kMainController.getY(); }, () -> { return kMainController.getX(); }); //comment to disable driving
    //coDriver = new RobotDrive(() -> {return kSideController.getX();}, () -> {return kSideController.getY();});
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
    } else {
      kCompressor.disable();
    }

    if(kMainController.getRawButton(kAutoSwitch)) {
      if(kMainController.getRawButtonPressed(kReadyAutoSwitch) && autoTimeout <= 0) {
        disableAllSolenoid(null);
        autoReady = true;
      }

      if(autoReady) {
        cameraGoalie();
      } else {
        if(autoTimeout > 0) {
          autoTimeout--; 
        }

        if(autoTimeout <= 0) {
          disableAllSolenoid(null);
        }
      }

    } else {
      autoReady = false;
      autoTimeout = 0;
      manualGoalie();
    }

    mainDriver.runDrive(); //comment to disable driving
    //coDriver.runDrive(); //comment to disable copilot driving
  }

  //TODO: REFINE THIS FUNCTION!
  private final double areaThreshold = 0.01;

  private final double actionBoxWidth = 16.8;
  private final double actionBoxHeight = 27;
  private final Vector actionBoxDimensions = new Vector(actionBoxWidth, actionBoxHeight);
  private final Rect actionBoxRect = new Rect(-8.8, -24, actionBoxDimensions.x, actionBoxDimensions.y);

  private final double leftBoundX = -10; //-8.8
  private final double rightBoundX = 10; //8
  private final double upperBoundY = 3;
  private final double lowerBoundY = -24;

  private final double leftKickerRectW = 9;
  private final double leftKickerRectH = 8;
  private final Rect leftKickerRect = new Rect(leftBoundX, lowerBoundY, leftKickerRectW, leftKickerRectH);

  private final double rightKickerRectW = 9;
  private final double rightKickerRectH = 8;
  private final Rect rightKickerRect = new Rect(rightBoundX - rightKickerRectW, lowerBoundY, rightKickerRectW, rightKickerRectH);

  private final double leftArmRectW = 9;
  private final double leftArmRectH = 8;
  private final Rect leftArmRect = new Rect(leftBoundX, upperBoundY - leftArmRectH, leftArmRectW, leftArmRectH);
  
  private final double rightArmRectW = 9;
  private final double rightArmRectH = 8;
  private final Rect rightArmRect = new Rect(rightBoundX - rightArmRectW, upperBoundY - rightArmRectH, rightArmRectW, rightArmRectH);

  /*
  private final Vector leftKickerPadding = new Vector(boxPaddingX, boxPaddingY);
  private final Vector rightKickerPadding = new Vector(boxPaddingX, boxPaddingY);
  private final Vector leftArmPadding = new Vector(boxPaddingX, boxPaddingY);
  private final Vector rightArmPadding = new Vector(boxPaddingX, boxPaddingY);

  private final Rect leftKickerRect = new Rect(-30, -20, 30 - leftKickerPadding.x, 20 - leftKickerPadding.y);
  private final Rect rightKickerRect = new Rect(rightKickerPadding.x, -20, 30 - rightKickerPadding.x, 20 - rightKickerPadding.y);
  private final Rect leftArmRect = new Rect(-30, leftArmPadding.y, 30 - leftArmPadding.x, 20 - leftArmPadding.y);
  private final Rect rightArmRect = new Rect(rightArmPadding.x, rightArmPadding.y, 30 - rightArmPadding.x, 20 - rightArmPadding.y);
  */
  
  private void cameraGoalie() {
    double[] data = limelightCamera.getLimelightCurrentData();
    boolean autoActed = false;
    
    if(debugLimeLight) {
      SmartDashboard.putNumber("LimeLight X: ", data[0]);
      SmartDashboard.putNumber("LimeLight Y: ", data[1]);
      SmartDashboard.putNumber("Limelight A: ", data[2]);
    }

    double xP = data[0];
    double yP = data[1];
    boolean isInActionRect = actionBoxRect.pointInRect(xP, yP);
    boolean isInLeftKickerRect = leftKickerRect.pointInRect(xP, yP);
    boolean isInRightKickerRect = rightKickerRect.pointInRect(xP, yP);

    boolean isInLeftArmRect = leftArmRect.pointInRect(xP, yP);
    boolean isInRightArmRect = rightArmRect.pointInRect(xP, yP);

    if(!isInActionRect) {
      return;
    }

    if(data[2] >= areaThreshold) {
      SmartDashboard.putString("Current Commit Place", "In Action Rect and Area");
      if(isInLeftKickerRect) {
        SmartDashboard.putString("Current Commit Place: ", "Left Kicker");
        disableAllSolenoid(kLeftKicker);
        kLeftKicker.set(true);
        autoActed = true;
      } else if(isInRightKickerRect) {
        SmartDashboard.putString("Current Commit Place: ", "Right Kicker");
        disableAllSolenoid(kRightKicker);
        kRightKicker.set(true);
        autoActed = true;
      } else if(isInLeftArmRect) {
        SmartDashboard.putString("Current Commit Place: ", "Left Arm");
        disableAllSolenoid(kLeftArm);
        kLeftArm.set(true);
        autoActed = true;
      } else if(isInRightArmRect) {
        SmartDashboard.putString("Current Commit Place: ", "Right Arm");
        disableAllSolenoid(kRightArm);
        kRightArm.set(true);
        autoActed = true;
      } else {
        SmartDashboard.putString("Current Commit Place: ", "Deadzone");
      }
    }

    if(autoActed) {
      autoTimeout = 150;
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
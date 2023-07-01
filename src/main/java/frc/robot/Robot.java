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

  private final boolean debugLimeLight = true;

  private LaserEyes laserEyes;
  private LimelightDevice limelightCamera;
  private RobotDrive mainDriver;
  private boolean autoReady = false;
  private int autoTimeout = 0;

  private int autoDriveTime = 0;
  private double autoDriveDirection = 0;

  private boolean autoEyesOverride = false;

  @Override
  public void robotInit() {
    //Create new instance of the limelight camera in order to gather x-y-a data
    limelightCamera = new LimelightDevice();

    //Initialise the driver instance of the robot,
    //The value of 0.075 is the determined deadzone of the joysticks.
    mainDriver = new RobotDrive(() -> {
      //Return the main controller X over the Xbox Controller joystick-x
      double mainX = kMainController.getY();
      mainX = Math.abs(mainX) > 0.075 ? mainX : 0;
      if(kMainController.getRawButton(kFlipManualSwitch)) {
        mainX *= -1;
      }

      if(mainX != 0) {
        return -mainX;
      }

      double x = kSideController.getRawAxis(4);
      if(kMainController.getRawButton(kFlipManualSwitch)) {
        x *= -1;
      }

      return Math.abs(x) > 0.075 ? x : 0;
    }, () -> {
      //Return the main controller Y over the Xbox Controller joystick-y
      double mainY = kMainController.getX();
      if(kMainController.getRawButton(kFlipManualSwitch)) {
        mainY *= -1;
      }

      mainY = Math.abs(mainY) > 0.075 ? mainY : 0;
      if(mainY != 0) {
        return mainY;
      }

      double y = kSideController.getRawAxis(0);

      if(kMainController.getRawButton(kFlipManualSwitch)) {
        y *= -1;
      }

      return Math.abs(y) > 0.075 ? -y : 0;
    }, kCoDriveSpeed);

    laserEyes = new LaserEyes(kSideController, 4, 2);

    //Initialize the array of button piston actions based on orientation of robot relative to observer
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

    //Disable the limelight light on initialization
    limelightCamera.setCameraLightState(false);
  }

  @Override
  public void robotPeriodic() {
    if(autoEyesOverride == false){
      laserEyes.tickEyes(); // comment to disable laser eyes
    } else {
      laserEyes.setColor(0, 255, 0);
    }

    if(kSideController.getRawButtonPressed(6)) {
      limelightCamera.setCameraLightState(true);
    } else if(kSideController.getRawButtonPressed(5)) {
      limelightCamera.setCameraLightState(false);
    }

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
      limelightCamera.setCameraLightState(true);
      double[] data = limelightCamera.getLimelightCurrentData();
      if(kMainController.getRawButtonPressed(kReadyAutoSwitch) && autoTimeout <= 0) {
        disableAllSolenoid(null);
        autoReady = true;
      }

      //If the readySwitch is being held, have the robot autoRotate to align with the puck.
      if(kMainController.getRawButton(kReadyAutoSwitch)) {
        double xP = data[0];
        double absX = Math.abs(xP);
        if(absX > deadZoneX + 0.5) {
          double power = 0.55;
          if(absX > deadZoneX + 1.0) { power = 0.75; }
          mainDriver.drive(Math.signum(xP) * power, 0f);
        } else {
          mainDriver.drive(0f, 0f);
        }

        //Return out of the function so the alignment does not conflict with the goalie actions.
        return;
      }

      if(autoReady) {
        cameraGoalie(data);
      } else {
        if(autoTimeout > 0) {
          autoTimeout--; 
        }

        if(autoTimeout <= 0) {
          disableAllSolenoid(null);
          autoEyesOverride = false;
        }

        if(autoDriveTime > 0) {
          autoDriveTime--;
        } else {
          autoDriveDirection = 0;
        }
      }

      mainDriver.drive(0, autoDriveDirection);
    } else {
      if(kMainController.getRawButtonReleased(kAutoSwitch)) {
        limelightCamera.setCameraLightState(false);
      }

      autoReady = false;
      autoTimeout = 0;

      manualGoalie();
      mainDriver.runDrive(); //comment to disable copilot driving
    }
  }

  private final double detectionX = 12; // How many units off of the x-axis (both directions) do we react to a puck
  private final double detectionY = 10; // How many units off of the y-axis do we react to a puck

  private final double deadZoneX = 6; // How many units off of the x axis (both direction) do we not react to a puck when in detectionX
  private final double deadZoneYTop = -8; // How many y-units off of the midpoint of artificial rectangle (up/down) do we not react to a puck when in detectionY
  private final double deadZoneYBottom = -12; //How many y-units off the midpoint of the artificial rectangle do we not react to a puck when in detectionY (make below deadZoneYTop)
  private final double detectionArea = 0.005; //How many area units before the robot is permitted to react to incoming pucks
  
  private void cameraGoalie(double[] data) {
    boolean autoActed = false;
    
    if(debugLimeLight) {
      SmartDashboard.putNumber("LimeLight X: ", data[0]);
      SmartDashboard.putNumber("LimeLight Y: ", data[1]);
      SmartDashboard.putNumber("Limelight A: ", data[2]);
    }

    double xP = data[0];
    double yP = data[1];

    if(data[2] < detectionArea) {
      return;
    }
    
    //Is the object outside of the x Bounds?
    if(xP < -detectionX && xP > detectionX) {
      return;
    }

    //Is the object above the y detection bound?
    if(yP > detectionY) {
      return;
    }

    //Is the point within the center deadzone X?
    if(xP > -deadZoneX && xP < deadZoneX) {
      return;
    }

    //Is the point within the center deadzone Y?
    if(yP < deadZoneYTop && yP > deadZoneYBottom) {
      return;
    }

    //Is the point in the right arm zone?
    if(xP < -deadZoneX && yP > deadZoneYTop) {
      SmartDashboard.putString("Current Commit Place: ", "Left Arm");
      disableAllSolenoid(kLeftArm);
      kLeftArm.set(true);
      autoDriveDirection = 1;
      autoActed = true;
    }

    //Is the point in the right kicker zone?
    if(xP < -deadZoneX && yP < deadZoneYBottom) {
      SmartDashboard.putString("Current Commit Place: ", "Left Kicker");
      disableAllSolenoid(kLeftKicker);
      kLeftKicker.set(true);
      autoDriveDirection = 1;
      autoActed = true;
    }

    //Is the point in the left arm zone?
    if(xP > deadZoneX && yP > deadZoneYTop) {
      SmartDashboard.putString("Current Commit Place: ", "Right Arm");
      disableAllSolenoid(kRightArm);
      kRightArm.set(true);
      autoDriveDirection = -1;
      autoActed = true;
    }

    //Is the point in the left kicker zone?
    if(xP > deadZoneX && yP < deadZoneYBottom) {
      SmartDashboard.putString("Current Commit Place: ", "Right Kicker");
      disableAllSolenoid(kRightKicker);
      kRightKicker.set(true);
      autoDriveDirection = -1;
      autoActed = true;
    }

    if(autoActed) {
      //Set the delay to be 3 seconds (150 20ms delays)
      autoTimeout = 150;
      autoReady = false;
      
      //Override the eyes to turn red,
      //Set the driving time to be 0.3 seconds
      autoDriveTime = 15;
      autoEyesOverride = true;
    } else {
      SmartDashboard.putString("Current Commit Place: ", "Deadzone");
    }
  }

  private void manualGoalie() {
    //Iterate over each button/piston action and check if their corresponding button is being held down.
    //Enable the attached solenoid to the button/piston action if the corresponding button is being held.
    //Disable the attached solenoid to the button/piston action of the corresponding button is NOT being held.
    for(ButtonPistonAction action : kMainController.getRawButton(kFlipManualSwitch) ? FlipPistonActions : PistonActions) {
      if(action.buttonPressed()) {
        action.setOn();
      } else {
        action.setOff();
      }
    }
  }

  private void disableAllSolenoid(Solenoid omit) {
    //Loop over each solenoid currently allocated
    //Disable every solenoid besides the solenoid reference passed in as 'omit'
    for(Solenoid s : kSolenoids) {
      if(s == omit) {
        continue;
      }

      s.set(false);
    }
  }
}
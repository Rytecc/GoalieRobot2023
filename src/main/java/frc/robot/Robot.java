package frc.robot;

import edu.wpi.first.wpilibj.PneumaticsModuleType;
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

  private RoboRIOAddressableLED laserEyes;
  private LimelightDevice limelightCamera;
  private RobotDrive mainDriver;

  private boolean actionPerformed;
  private boolean lightsToggled;

  @Override
  public void robotInit() {
    actionPerformed = false;
    lightsToggled = false;

    //mainDriver = new RobotDrive(kDriveTopLeft, kDriveBottomLeft, kDriveTopRight, kDriveBottomRight); //uncomment to enable driving
    //laserEyes = new RoboRIOAddressableLED(kLaserEyePWMPort, 2); //uncomment to enable laser eyes

    PistonActions = new ButtonPistonAction[] {
      new ButtonPistonAction(kMainController, Constants.kButton1, new Solenoid(PneumaticsModuleType.CTREPCM, 0)),
      new ButtonPistonAction(kMainController, Constants.kButton2, new Solenoid(PneumaticsModuleType.CTREPCM, 1)),
      new ButtonPistonAction(kMainController, Constants.kButton3, new Solenoid(PneumaticsModuleType.CTREPCM, 2)),
      new ButtonPistonAction(kMainController, Constants.kButton4, new Solenoid(PneumaticsModuleType.CTREPCM, 3)),
      new ButtonPistonAction(kMainController, Constants.kButton5, new Solenoid(PneumaticsModuleType.CTREPCM, 4))
    };

    FlipPistonActions = new ButtonPistonAction[] {
      new ButtonPistonAction(kMainController, Constants.kButton5, new Solenoid(PneumaticsModuleType.CTREPCM, 0)),
      new ButtonPistonAction(kMainController, Constants.kButton4, new Solenoid(PneumaticsModuleType.CTREPCM, 1)),
      new ButtonPistonAction(kMainController, Constants.kButton3, new Solenoid(PneumaticsModuleType.CTREPCM, 2)),
      new ButtonPistonAction(kMainController, Constants.kButton2, new Solenoid(PneumaticsModuleType.CTREPCM, 3)),
      new ButtonPistonAction(kMainController, Constants.kButton1, new Solenoid(PneumaticsModuleType.CTREPCM, 4))
    };
  }

  @Override
  public void robotPeriodic() {
    //laserEyes(); //uncomment to enable laser eyes

    SmartDashboard.putBoolean(kDashboardIsAutoSwitchOn, kMainController.getRawButton(kAutoSwitch));
    SmartDashboard.putBoolean(kDashboardIsManualFlipped, kMainController.getRawButton(kFlipManualSwitch));
  }

  @Override
  public void teleopPeriodic() {
    manualGoalie();
    //mainDriver.runDrive(); //uncomment to enable driving
  }

  /** Uncomment to enable auto goalie
  private void cameraGoalie() {
    double[] data = limelightCamera.getLimelightCurrentData();
    boolean isLeft = data[0] < 0;
    boolean isRight = data[0] > 0;

    boolean isAbove = data[1] > 0;
    boolean isBelow = data[1] < 0;

    System.out.println("{" + isLeft + ":" + isRight + "}");
    System.out.println("{" + isAbove + ":" + isBelow + "}");
  }
  */

  /** Uncomment to enable laser eyes
  private void laserEyes() {
    if(kMainController.getRawButtonPressed(kToggleLaserEyes)) {
      lightsToggled = !lightsToggled;
      return;
    }

    laserEyes.setBufferToColor(lightsToggled ? 1.0 : 0.0, 0.0, 0.0);
  }
  */

  private void manualGoalie() {
    for(ButtonPistonAction action : kMainController.getRawButton(kFlipManualSwitch) ? FlipPistonActions : PistonActions) {
      if(action.buttonPressed()) {
        action.setOn();
      } else {
        action.setOff();
      }
    }
  }
}
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

  private long lastCooldownTime;
  private boolean actionPerformed;
  private boolean lightsToggled;

  @Override
  public void robotInit() {
    lastCooldownTime = 0;
    actionPerformed = false;
    lightsToggled = false;

    mainDriver = new RobotDrive(kDriveTopLeft, kDriveBottomLeft, kDriveTopRight, kDriveBottomRight);
    //laserEyes = new RoboRIOAddressableLED(kLaserEyePWMPort, 2); //uncomment to enable laser eyes

    PistonActions = new ButtonPistonAction[] {
      new ButtonPistonAction(kMainController, Constants.kButton1, new Solenoid(PneumaticsModuleType.CTREPCM, 0)),
      new ButtonPistonAction(kMainController, Constants.kButton2, new Solenoid(PneumaticsModuleType.CTREPCM, 1)),
      new ButtonPistonAction(kMainController, Constants.kButton3, new Solenoid(PneumaticsModuleType.CTREPCM, 2)),
      new ButtonPistonAction(kMainController, Constants.kButton4, new Solenoid(PneumaticsModuleType.CTREPCM, 3)),
      new ButtonPistonAction(kMainController, Constants.kButton5, new Solenoid(PneumaticsModuleType.CTREPCM, 4)),
      new ButtonPistonAction(kMainController, Constants.kButton6, new Solenoid(PneumaticsModuleType.CTREPCM, 5)),
    };
  }

  @Override
  public void robotPeriodic() {
    //laserEyes(); uncomment to enable laser eyes //uncomment to enable laser eyes
  }

  @Override
  public void teleopPeriodic() {
    manualGoalie();
    mainDriver.runDrive();
  }

  /* Unused Functions As of First Iteration of Testing Commit
  private void cameraGoalie() {
    double[] data = limelightCamera.getLimelightCurrentData();
    boolean isLeft = data[0] < 0;
    boolean isRight = data[0] > 0;

    boolean isAbove = data[1] > 0;
    boolean isBelow = data[1] < 0;

    System.out.println("{" + isLeft + ":" + isRight + "}");
    System.out.println("{" + isAbove + ":" + isBelow + "}");
  }

  private void laserEyes() {
    if(kMainController.getRawButtonPressed(kToggleLaserEyes)) {
      lightsToggled = !lightsToggled;
      return;
    }

    laserEyes.setBufferToColor(lightsToggled ? 1.0 : 0.0, 0.0, 0.0);
  }
  */

  private void manualGoalie() {
    if(actionPerformed) {
      long timeDiff = System.currentTimeMillis() - lastCooldownTime;

      if(timeDiff > Constants.kTimeoutSeconds * 1000) {
        SmartDashboard.putString(kDashboardManualActionAllowed, "READY");
        actionPerformed = false;
      }

      return;
    }

    for(ButtonPistonAction action : PistonActions) {
      if(action.buttonPressed()) {
        action.toggle();

        System.out.println("WAITING: " + Constants.kTimeoutSeconds + "s");
        lastCooldownTime = System.currentTimeMillis();
        actionPerformed = true;
        break;
      }
    }
  }
}

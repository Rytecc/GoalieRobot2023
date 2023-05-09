// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

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
    laserEyes = new RoboRIOAddressableLED(kLaserEyePWMPort, 2);

    PistonActions = new ButtonPistonAction[] {
      new ButtonPistonAction(kMainController, Constants.kButton1, null, null, false),
      new ButtonPistonAction(kMainController, Constants.kButton2, null, null, false),
      new ButtonPistonAction(kMainController, Constants.kButton3, null, null, false),
      new ButtonPistonAction(kMainController, Constants.kButton4, null, null, false),
      new ButtonPistonAction(kMainController, Constants.kButton5, null, null, false),
    };

    FlipPistonActions = new ButtonPistonAction[] {
      new ButtonPistonAction(kMainController, Constants.kButton5, null, null, false),
      new ButtonPistonAction(kMainController, Constants.kButton4, null, null, false),
      new ButtonPistonAction(kMainController, Constants.kButton3, null, null, false),
      new ButtonPistonAction(kMainController, Constants.kButton2, null, null, false),
      new ButtonPistonAction(kMainController, Constants.kButton1, null, null, false),
    };
  }

  @Override
  public void robotPeriodic() {
    laserEyes();
  }

  @Override
  public void teleopPeriodic() {
    if(kMainController.getRawButton(kAutoSwitch)) {
      cameraGoalie();
    } else {
      manualGoalie();
    }

    mainDriver.runDrive();
  }

  private void cameraGoalie() {

  }

  private void laserEyes() {
    if(kMainController.getRawButtonPressed(kToggleLaserEyes)) {
      lightsToggled = !lightsToggled;
      return;
    }

    laserEyes.setBufferToColor(lightsToggled ? 1.0 : 0.0, 0.0, 0.0);
  }

  private void manualGoalie() {
    if(actionPerformed) {
      long timeDiff = System.currentTimeMillis() - lastCooldownTime;
      SmartDashboard.putString(kDashboardManualACtionAllowed, String.format("%.2f", (timeDiff / 1000.0) + "s"));

      if(timeDiff > Constants.kTimeoutSeconds * 1000) {
        SmartDashboard.putString(kDashboardManualACtionAllowed, "READY");
        actionPerformed = false;
      }

      return;
    }

    for(ButtonPistonAction action : (kMainController.getRawButton(kFlipManualSwitch) ? FlipPistonActions : PistonActions)) {
      if(action.buttonPressed()) {
        System.out.println("RUN SOLENOID ACTION!");
        //action.toggle();

        System.out.println("WAITING: " + Constants.kTimeoutSeconds + "s");
        lastCooldownTime = System.currentTimeMillis();
        actionPerformed = true;
        break;
      }
    }
  }
}

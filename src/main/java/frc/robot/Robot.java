// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/*
 * Author: Lucas Soliman
 * Date Created: May, 4, 2o23
 * 
 * This is the main robot file that runs the goalie robot for the TSN Funny thing (:
 */
public class Robot extends TimedRobot {

  public final Joystick inputDevice = new Joystick(0);
  public final ButtonPistonAct[] PistonActions = new ButtonPistonAct[] {
    new ButtonPistonAct(inputDevice, Constants.kButton1, null, null, false),
    new ButtonPistonAct(inputDevice, Constants.kButton2, null, null, false),
    new ButtonPistonAct(inputDevice, Constants.kButton3, null, null, false),
    new ButtonPistonAct(inputDevice, Constants.kButton4, null, null, false),
    new ButtonPistonAct(inputDevice, Constants.kButton5, null, null, false),
    new ButtonPistonAct(inputDevice, Constants.kButton6, null, null, false),
    new ButtonPistonAct(inputDevice, Constants.kButton7, null, null, false),
    new ButtonPistonAct(inputDevice, Constants.kButton8, null, null, false),
    new ButtonPistonAct(inputDevice, Constants.kButton9, null, null, false)
  };

  private long lastCooldownTime;
  private boolean actionPerformed;
  @Override
  public void robotInit() {
    lastCooldownTime = 0;
    actionPerformed = false;
  }

  @Override
  public void robotPeriodic() {
    if(actionPerformed) {
      if(System.currentTimeMillis() - lastCooldownTime > Constants.kTimeoutSeconds * 1000) {
        actionPerformed = false;
      }

      return;
    }

    for(ButtonPistonAct action : PistonActions) {
      if(action.buttonPressed()) {
        System.out.println("RUN SOLENOID ACTION!");
        //action.runAction();

        System.out.println("RESETTING OTHER SOLENOIDS");
        /*
        for(ButtonPistonAct otherActions : PistonActions) {
          if(otherActions != action) {
            otherActions.reset();
          }
        }
        */

        System.out.println("WAITING: " + Constants.kTimeoutSeconds + "s");
        lastCooldownTime = System.currentTimeMillis();
        actionPerformed = true;
        break;
      }
    }
  }
}

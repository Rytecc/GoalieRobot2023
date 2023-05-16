package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

/*
 * Author: Lucas Soliman
 * Date Created: May 5, 2023
 * 
 * This class encapsulates functions to help with using the solenoids and buttons associated 
 */
public class ButtonPistonAction {
    private final int BUTTON_BIND;
    private final Joystick JOYSTICK;
    private final Solenoid SOLENOID;

    public ButtonPistonAction(Joystick js, int button, Solenoid s) {
        BUTTON_BIND = button;
        JOYSTICK = js;
        SOLENOID = s;
        setOff();
    }

    public boolean buttonPressed() {
        return JOYSTICK.getRawButton(BUTTON_BIND);
    }

    public void toggle() {
        SOLENOID.set(!SOLENOID.get());
    }

    public void setOff() {
        SOLENOID.set(false);
    }

    public void setOn() {
        SOLENOID.set(true);
    }
}

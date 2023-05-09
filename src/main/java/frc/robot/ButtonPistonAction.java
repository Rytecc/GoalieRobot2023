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
    private final Solenoid SOLENOID1;
    private final Solenoid SOLENOID2;
    private final boolean SOLENOID_TARGET_STATE;

    public ButtonPistonAction(Joystick js, int button, Solenoid s1, Solenoid s2, boolean s1ClosedState) {
        BUTTON_BIND = button;
        JOYSTICK = js;
        SOLENOID1 = s1;
        SOLENOID2 = s2;

        SOLENOID_TARGET_STATE = s1ClosedState;
        reset();
    }

    public boolean buttonPressed() {
        return JOYSTICK.getRawButton(BUTTON_BIND);
    }

    public void toggle() {
        SOLENOID1.set(!SOLENOID1.get());
        SOLENOID2.set(!SOLENOID2.get());
    }

    public void reset() {
        SOLENOID1.set(SOLENOID_TARGET_STATE);
        SOLENOID2.set(!SOLENOID_TARGET_STATE);
    }

    public void runAction() {
        SOLENOID1.set(!SOLENOID_TARGET_STATE);
        SOLENOID1.set(SOLENOID_TARGET_STATE);
    }
}

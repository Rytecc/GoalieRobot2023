package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;

public class ButtonPistonAct {
    private final int BUTTON_BIND;
    private final Joystick JOYSTICK;
    private final Solenoid SOLENOID1;
    private final Solenoid SOLENOID2;
    private final boolean SOLENOID_TARGET_STATE;

    public ButtonPistonAct(Joystick js, int button, Solenoid s1, Solenoid s2, boolean openState) {
        BUTTON_BIND = button;
        JOYSTICK = js;
        SOLENOID1 = s1;
        SOLENOID2 = s2;

        SOLENOID_TARGET_STATE = openState;
    }

    public boolean buttonPressed() {
        return JOYSTICK.getRawButton(BUTTON_BIND);
    }

    public void runAction() {
        SOLENOID1.set(SOLENOID_TARGET_STATE);
        SOLENOID2.set(!SOLENOID_TARGET_STATE);
    }

    public void reset() {
        SOLENOID1.set(!SOLENOID_TARGET_STATE);
        SOLENOID1.set(SOLENOID_TARGET_STATE);
    }
}

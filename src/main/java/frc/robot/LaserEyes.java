package frc.robot;

import java.util.HashMap;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.Joystick;
import frc.robot.LightEffects.LightEffect;
import frc.robot.LightEffects.RainbowEffects;
import frc.robot.LightEffects.SetColor;

//TODO: TEST THIS IMPLEMENTATION
public class LaserEyes {

    private final Joystick eyesControllerJoystick;
    private final HashMap<Integer, LightEffect> lightEffects;
    private final AddressableLEDBuffer eyesBuffer;
    private final AddressableLED eyesLED;

    private LightEffect currentEffect;

    public LaserEyes(Joystick lightController, int lightPort, int eyesCount) {
        eyesControllerJoystick = lightController;
        eyesLED = new AddressableLED(lightPort);
        eyesBuffer = new AddressableLEDBuffer(eyesCount);
        eyesLED.setLength(eyesCount);
        
        lightEffects = new HashMap<>();
        lightEffects.put(8, new RainbowEffects(eyesBuffer, 0, 1));
        lightEffects.put(9, new SetColor(eyesBuffer, 255, 0, 0));
        lightEffects.put(10, new SetColor(eyesBuffer, 0, 255, 0));
        lightEffects.put(11, new SetColor(eyesBuffer, 0, 0, 0));

        currentEffect = lightEffects.get(8);
    }

    public void tickEyes() {
        for(Integer key : lightEffects.keySet()) {
            if(eyesControllerJoystick.getRawButtonPressed(key)) {
                currentEffect = lightEffects.get(key);
                System.out.println(currentEffect.getClass().getTypeName());
            }
        }

        eyesLED.setData(currentEffect.tick());
    }
}

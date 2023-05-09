package frc.robot;

import edu.wpi.first.wpilibj.AddressableLED;
import edu.wpi.first.wpilibj.AddressableLEDBuffer;
import edu.wpi.first.wpilibj.util.Color;

/*
 * Author: Lucas Soliman
 * Date Created: May 9, 2023
 * 
 * This class is responsible for allowing the setting of colours for AddressableRGBLEDs for the RoboRIO
 */
public class RoboRIOAddressableLED {
    private AddressableLEDBuffer ledBuffer;
    private AddressableLED led;

    public RoboRIOAddressableLED(int ledPort, int ledLength) {
        led = new AddressableLED(ledPort);
        ledBuffer = new AddressableLEDBuffer(2);
    }

    public void setBufferToColor(double r, double g, double b) {
        for(int i = 0; i < ledBuffer.getLength(); i++) {
            ledBuffer.setLED(i, new Color(r, g, b));
        }
    }

    public void applyBuffer() {
        led.setData(ledBuffer);
    }
}

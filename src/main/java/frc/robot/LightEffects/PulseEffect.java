package frc.robot.LightEffects;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;

public class PulseEffect {
    private final AddressableLEDBuffer buffer;
    private final int r, g, b;

    public PulseEffect(AddressableLEDBuffer targetbuffer, int r, int g, int b) {
          buffer = targetbuffer;
          
          this.r = r;
          this.g = g;
          this.b = b;

     }

     @Override
     public AddressableLEDBuffer tick() {
      return buffer;
     }

}

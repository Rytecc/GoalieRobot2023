package frc.robot.LightEffects;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;

public class FlashEffect implements LightEffect{
     
    private final AddressableLEDBuffer buffer;
    private final int r1, g1, b1, r2, g2, b2;
    private final double startFlashtime;
    private double flashtime;
    private boolean firstColor = true;

    public FlashEffect(AddressableLEDBuffer targetbuffer, int r1, int g1, int b1, int r2, int g2, int b2, double flashtime) {
          buffer = targetbuffer;
          
          this.r1 = r1;
          this.g1 = g1;
          this.b1 = b1;

          this.r2 = r2;
          this.g2 = g2;
          this.b2 = b2;

          this.startFlashtime = flashtime;

          flashtime = startFlashtime;
     }

     @Override
     public AddressableLEDBuffer tick() {
        if(flashtime <= 0) {
           firstColor = !firstColor;
           flashtime = startFlashtime;
        }
        flashtime -= 0.1;

        for(int i = 0; i < buffer.getLength(); i++) {
        if(firstColor){
            buffer.setRGB(i, r1, g1, b1);
        }else{
            buffer.setRGB(i, r2, g2, b2);
        }
    }
        return buffer;
     }
}

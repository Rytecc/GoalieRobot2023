package frc.robot.LightEffects;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;

public class SwapEffect implements LightEffect{
     
    private final AddressableLEDBuffer buffer;
    private final int r1, g1, b1, r2, g2, b2;

    private final double startSwapTime;
    private double swaptime;

    private boolean firstCycle;

    public SwapEffect(AddressableLEDBuffer targetbuffer, int r1, int g1, int b1, int r2, int g2, int b2, double swaptime) {
          buffer = targetbuffer;
          
          this.r1 = r1;
          this.g1 = g1;
          this.b1 = b1;

          this.r2 = r2;
          this.g2 = g2;
          this.b2 = b2;

          this.startSwapTime = swaptime;

          swaptime = startSwapTime;
     }

     @Override
     public AddressableLEDBuffer tick() {
        if(swaptime <= 0) {
           firstCycle = !firstCycle;
           swaptime = startSwapTime;
        }
        swaptime -= 0.1;

        if(firstCycle){
            buffer.setRGB(0, r1, g1, b1);
            buffer.setRGB(1, r2, g2, b2);
        }else{
            buffer.setRGB(0, r2, g2, b2);
            buffer.setRGB(1, r1, g1, b1);
        }

        return buffer;
     }
}


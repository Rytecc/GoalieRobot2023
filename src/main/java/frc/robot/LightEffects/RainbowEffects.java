package frc.robot.LightEffects;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;

public class RainbowEffects implements LightEffect {
    private AddressableLEDBuffer buffer;
    private int rainbowFirstPixelHue;

    private final int start;
    private final int end;

    public RainbowEffects(AddressableLEDBuffer targetBuffer, int start, int end) {
        buffer = targetBuffer;
        this.start = start;
        this.end = end;

        rainbowFirstPixelHue = 0;
    }

    @Override
    public AddressableLEDBuffer tick() {
        for (var i = start; i < end; i++) {
            int hue = (rainbowFirstPixelHue + (i * 180 / buffer.getLength())) % 180;
            buffer.setHSV(i, hue, 255, 128);
        }
        
        rainbowFirstPixelHue += 3;
        rainbowFirstPixelHue %= 180;
        return buffer;
    }
}

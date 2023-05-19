package frc.robot.LightEffects;

import edu.wpi.first.wpilibj.AddressableLEDBuffer;

public class SetColor implements LightEffect {
    private final AddressableLEDBuffer target;
    private final int r;
    private final int g;
    private final int b;

    public SetColor(AddressableLEDBuffer buffer, int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;

        target = buffer;
    }

    @Override
    public AddressableLEDBuffer tick() {
        for(int i = 0; i < target.getLength(); i++) {
            target.setRGB(i, r, g, b);
        }
        
        return target;
    }
}

package audio.rabid.dev.skintherapy;

import java.nio.ByteBuffer;

/**
 * Created by Charles on 7/10/2015.
 */
public class ChainPacket {

    public enum Chain {
        YELLOW(0),
        GREEN(1);
        byte index;
        Chain(int index){
            this.index = (byte) index;
        }
    }

    public enum Shape {
        CONSTANT(0x1),
        TRIANGLE_WAVE(0x2),
        SINE_WAVE(0x3);
        byte id;
        Shape(int id){
            this.id = (byte) id;
        }
    }

    private static final int PACKET_SIZE = 8;

    public static final byte MAX_BRIGHTNESS = (byte) 0xFF;

    private int brightness = MAX_BRIGHTNESS;
    private int period = 10000; //1 second in 100us units
    private Shape waveShape = Shape.CONSTANT;
    private boolean enabled = true;

    public ChainPacket(){

    }

    public byte[] applyTo(Chain... chains){
        ByteBuffer b = ByteBuffer.allocate(PACKET_SIZE);
        byte whichChains = 0;
        for(Chain c : chains){
            whichChains |= c.index;
        }
        b.put(whichChains);
        b.put((byte)(enabled ? 1 : 0));
        b.put((byte) (brightness & 0xFF));
        b.putShort((short) (period & 0xFFFF));
        b.put(waveShape.id);

        return b.array().clone();
    }

    public ChainPacket setBrightness(int brightness) {
        if(brightness<0 || brightness>MAX_BRIGHTNESS)
            throw new IllegalArgumentException("brightness must be between 0 and MAX_BRIGHTNESS: "+brightness);
        this.brightness = brightness;
        return this;
    }

    public ChainPacket setPeriod(int period) {
        this.period = period;
        return this;
    }

    public ChainPacket setWaveShape(Shape waveShape) {
        this.waveShape = waveShape;
        return this;
    }

    public ChainPacket enable() {
        this.enabled = true;
        return this;
    }

    public ChainPacket disable(){
        this.enabled = false;
        return this;
    }

    public static byte[] disableAll(){
        return new ChainPacket().disable().applyTo(Chain.values());
    }
}

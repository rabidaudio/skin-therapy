package audio.rabid.dev.skintherapy.coms;

/**
 * Created by Charles on 7/14/2015.
 */
public class ChainPacket extends Packet.Outgoing {

    public static final int MAX_BRIGHTNESS = 255;

    public enum Chain {
        YELLOW(0),
        GREEN(1);
        byte index;
        Chain(int index){
            this.index = (byte) index;
        }
        static Chain fromIndex(int index){
            for(Chain c : Chain.values()) if(c.index==index) return c;
            return null;
        }
    }

    public enum Shape {
        CONSTANT(0x1),
        TRIANGLE_WAVE(0x2),
        SINE_WAVE(0x4);
        public byte id;
        Shape(int id){
            this.id = (byte) id;
        }
        static Shape fromIndex(int index){
            for(Shape s : Shape.values()) if(s.id==index) return s;
            return null;
        }
    }


    private int brightness = MAX_BRIGHTNESS;
    private int period = 10000; //1 second in 100us units
    private Shape waveShape = Shape.CONSTANT;
    private boolean enabled = true;

    public ChainPacket() {
        super(SET_PACKET, DEFAULT_LENGTH);
    }

    public byte[] applyTo(Chain... chains) {

        byte whichChains = 0;
        for (Chain c : chains) {
            whichChains |= (1 << c.index);
        }
        bytes.put(whichChains);
        bytes.put((byte) (enabled ? 1 : 0));
        bytes.put((byte) brightness);
        bytes.putShort((short) period);
        bytes.put(waveShape.id);

        return getBytes();
    }

    public ChainPacket setBrightness(int brightness) {
        if (brightness < 0 || brightness > MAX_BRIGHTNESS)
            throw new IllegalArgumentException("brightness must be between 0 and " + MAX_BRIGHTNESS + ": " + brightness);
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

    public ChainPacket disable() {
        this.enabled = false;
        return this;
    }

    public static byte[] disableAll() {
        return new ChainPacket().disable().applyTo(Chain.values());
    }
}

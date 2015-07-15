package audio.rabid.dev.skintherapy.coms;

/**
 * Created by Charles on 7/14/2015.
 */
public class StatePacket extends Packet.Incoming {

    public ChainPacket.Chain chain;
    public boolean enabled;
    public int brightness;
    public int period;
    public ChainPacket.Shape shape;

    public StatePacket(byte[] data){
        super(data);
        if(!isValid(STATE_PACKET, DEFAULT_LENGTH)){
            throw new IllegalArgumentException("Not a valid StatePacket: "+toString());
        }

        chain = ChainPacket.Chain.fromIndex(bytes.get());
        enabled = (bytes.get() != 0);
        brightness = bytes.get();
        period = bytes.getShort();
        shape = ChainPacket.Shape.fromIndex(bytes.get());
    }
}

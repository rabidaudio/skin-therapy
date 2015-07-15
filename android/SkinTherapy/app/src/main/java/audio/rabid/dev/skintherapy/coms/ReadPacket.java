package audio.rabid.dev.skintherapy.coms;

/**
 * Created by Charles on 7/14/2015.
 */
public class ReadPacket extends Packet.Outgoing {

    public ReadPacket(ChainPacket.Chain whichChain){
        super(READ_PACKET, DEFAULT_LENGTH);
        bytes.put((byte) whichChain.index);
    }
}

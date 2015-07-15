package audio.rabid.dev.skintherapy.coms;

/**
 * Created by Charles on 7/14/2015.
 */
public class ErrorPacket extends Packet.Incoming {

    public ErrorPacket(byte[] bytes){
        super(bytes);

        if(!isValid(ERROR_PACKET, DEFAULT_LENGTH)){
            throw new IllegalArgumentException("Not an error packet: "+toString());
        }
    }
}
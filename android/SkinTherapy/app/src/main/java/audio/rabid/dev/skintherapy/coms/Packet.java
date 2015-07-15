package audio.rabid.dev.skintherapy.coms;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import audio.rabid.dev.skintherapy.Utils;

/**
 * Created by Charles on 7/10/2015.
 *
 * Defines protocol
 *
 * TODO add support for requesting the state of a particular packet
 */
public abstract class Packet {

    protected static final byte SET_PACKET = 0x57;
    protected static final byte READ_PACKET = 0x52;

    protected static final byte STATE_PACKET = 0x53;
    protected static final byte ERROR_PACKET = 0x45;

    protected static final int DEFAULT_LENGTH = 8;


    protected byte cid;
    protected int length;
    protected ByteBuffer bytes;

    private Packet(byte cid, int length){
        this.cid = cid;
        this.length = length;
    }

    public String toString(){
        return Utils.bytesToHex(bytes.array());
    }

    protected static abstract class Outgoing extends Packet {

        protected Outgoing(byte cid, int length){
            super(cid, length);
            bytes = ByteBuffer.allocate(length);
            bytes.order(ByteOrder.LITTLE_ENDIAN);
        }

        private boolean used = false;

        public byte[] getBytes(){
            if(used) throw new IllegalArgumentException("Can't reuse packet! Make a new one instead.");
            used = true;
            return bytes.array();
        }
    }

    protected static abstract class Incoming extends Packet {

        protected Incoming(byte[] data){
            super(data[0], data.length);
            bytes = ByteBuffer.wrap(data);
            cid = bytes.get();
        }

        public boolean isValid(byte expectedCid, int expectedLength){
            return (expectedCid==cid && expectedLength == length);
        }
    }









}

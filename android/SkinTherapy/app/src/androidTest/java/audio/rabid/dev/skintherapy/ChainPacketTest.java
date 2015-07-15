package audio.rabid.dev.skintherapy;

import junit.framework.TestCase;

import audio.rabid.dev.skintherapy.coms.ChainPacket;
import audio.rabid.dev.skintherapy.coms.ErrorPacket;
import audio.rabid.dev.skintherapy.coms.ReadPacket;

public class ChainPacketTest extends TestCase {

    public void testCanSetValues(){
        byte[] result = new ChainPacket()
                .setBrightness(128)
                .setPeriod(12345)
                .setWaveShape(ChainPacket.Shape.SINE_WAVE)
                .disable()
                .applyTo(ChainPacket.Chain.GREEN);

        assertEquals("the length is correct", 8, result.length);
        assertEquals("the packet is applied to the correct chains", (byte)0x2, result[1]);
        assertTrue("is disabled", result[2]==0);
        assertEquals("the brightness is correct", (byte)0x80, result[3]);
        assertEquals("the second byte of period is correct", (byte)0x39, result[4]);
        assertEquals("the first byte of period is correct", (byte)0x30, result[5]);
        assertEquals("the wave shape is correct", ChainPacket.Shape.SINE_WAVE.id, result[6]);
    }

    public void testDefaultValues(){
        byte[] result = new ChainPacket().applyTo();

        assertEquals("the length is correct", 8, result.length);
        assertEquals("not applied to any chains", 0, result[1]);
        assertTrue("is enabled", result[2]!=0);
        assertEquals("defaults to full brightness", (byte) ChainPacket.MAX_BRIGHTNESS, result[3]);
        assertEquals("period is 1 second", (byte)0x10, result[4]);
        assertEquals("period is 1 second", (byte)0x27, result[5]);
        assertEquals("the shape is constant", ChainPacket.Shape.CONSTANT.id, result[6]);
    }

    public void testDisableAll(){
        byte[] result = ChainPacket.disableAll();

        assertEquals("the length is correct", 8, result.length);

        assertEquals("applied to all chains", (byte)0x3, result[1]);
        assertTrue("sets disabled", result[2]==0);
    }

    public void testErrorPacket(){
        byte[] valid = "E.......".getBytes();
        byte[] invalid = "XXXXXXXX".getBytes();
        new ErrorPacket(valid); //shouldn't throw exception

        try{
            new ErrorPacket(invalid);
            fail("invalid packet did not throw exception");
        }catch (Exception e){
            //expected
        }
    }

    public void testReadPacket(){
        ReadPacket p = new ReadPacket(ChainPacket.Chain.GREEN);
        byte[] data = p.getBytes();

        assertEquals(ChainPacket.Chain.GREEN.index, data[1]);

        try{
            byte[] data2 = p.getBytes();
            fail("reading twice did not throw exception");
        }catch (Exception e){
            //expected
        }
    }
}

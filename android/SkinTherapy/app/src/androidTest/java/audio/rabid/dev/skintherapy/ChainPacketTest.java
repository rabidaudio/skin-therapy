package audio.rabid.dev.skintherapy;

import junit.framework.TestCase;

/**
 * Created by Charles on 7/10/2015.
 */
public class ChainPacketTest extends TestCase {

    public void testCanSetValues(){
        byte[] result = new ChainPacket()
                .setBrightness(128)
                .setPeriod(12345)
                .setWaveShape(ChainPacket.Shape.SINE_WAVE)
                .applyTo(ChainPacket.Chain.GREEN);

        assertEquals("the length is correct", 8, result.length);

        assertEquals("the brightness is correct", 128, result[2]);
        assertEquals("the period is correct", 12345, result[3]<<8 & result[4]);
        assertEquals("the wave shape is correct", ChainPacket.Shape.SINE_WAVE.id, result[5]);
        assertEquals("the packet is applied to the correct chains", 0x2, result[0]);
    }

    public void testDefaultValues(){

    }

    public void testDisableAll(){
        byte[] result = ChainPacket.disableAll();

        assertEquals("the length is correct", 8, result.length);
    }
}

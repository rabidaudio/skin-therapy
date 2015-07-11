package audio.rabid.dev.skintherapy;

import junit.framework.TestCase;

public class ChainPacketTest extends TestCase {

    public void testCanSetValues(){
        byte[] result = new ChainPacket()
                .setBrightness(128)
                .setPeriod(12345)
                .setWaveShape(ChainPacket.Shape.SINE_WAVE)
                .disable()
                .applyTo(ChainPacket.Chain.GREEN);

        assertEquals("the length is correct", 8, result.length);
        assertEquals("the packet is applied to the correct chains", (byte)0x2, result[0]);
        assertTrue("is disabled", result[1]==0);
        assertEquals("the brightness is correct", (byte)0x80, result[2]);
        assertEquals("the second byte of period is correct", (byte)0x39, result[3]);
        assertEquals("the first byte of period is correct", (byte)0x30, result[4]);
        assertEquals("the wave shape is correct", ChainPacket.Shape.SINE_WAVE.id, result[5]);
    }

    public void testDefaultValues(){
        byte[] result = new ChainPacket().applyTo();

        assertEquals("the length is correct", 8, result.length);
        assertEquals("not applied to any chains", 0, result[0]);
        assertTrue("is enabled", result[1]!=0);
        assertEquals("defaults to full brightness", (byte)ChainPacket.MAX_BRIGHTNESS, result[2]);
        assertEquals("period is 1 second", (byte)0x10, result[3]);
        assertEquals("period is 1 second", (byte)0x27, result[4]);
        assertEquals("the shape is constant", ChainPacket.Shape.CONSTANT.id, result[5]);
    }

    public void testDisableAll(){
        byte[] result = ChainPacket.disableAll();

        assertEquals("the length is correct", 8, result.length);

        assertEquals("applied to all chains", (byte)0x3, result[0]);
        assertTrue("sets disabled", result[1]==0);
    }
}

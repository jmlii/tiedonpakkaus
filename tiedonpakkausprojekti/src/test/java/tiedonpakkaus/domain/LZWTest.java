package tiedonpakkaus.domain;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;


public class LZWTest {
    LZW lzw;
    String testString;

    @Before
    public void setUp() {
        testString = "Testikirjoitus testausta varten.";
        lzw = new LZW();
    }
    
    @Test
    public void encodeReturnsByteArrayWithCorrectLength() {
        byte[] bytes = lzw.encode(testString);
        assertEquals(34, bytes.length);        
    }
    
    @Test
    public void decodeReturnsSameTextAsOriginalFile() {
        byte[] bytes = lzw.encode(testString);
        String decoded = lzw.decode(bytes);
        assertEquals(decoded, testString);
    }
    
}
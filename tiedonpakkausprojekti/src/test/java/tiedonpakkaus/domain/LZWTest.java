package tiedonpakkaus.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class LZWTest {
    LZW lzw;
    String testString;
    String testStringUnknownChar;
    String testStringLineFeed;

    @Before
    public void setUp() {
        testString = "Testikirjoitus testausta varten.";
        testStringUnknownChar = "đ";
        testStringLineFeed = "\n\n\n";
        lzw = new LZW();
    }
    
    @Test
    public void encodeReturnsByteArrayWithCorrectLength() {
        byte[] bytes = lzw.encode(testString);
        assertEquals(34, bytes.length);        
    }
    
    @Test
    public void encodeChangesUnknownCharToTilde() {
        // Koodilistaan tulee vain tilde-merkin koodi 126, bitteinä 1111110, pituus 7 bittiä
        // Tavutaulukossa bittien alkuun tulee 1 kpl 0-bittiä, sisällöksi 01111110
        // Tavuna bittijono on arvoltaan 126
        
        byte[] bytes = lzw.encode(testStringUnknownChar);        
        assertEquals(126, bytes[2]);
    }
    
    @Test
    public void decodeReturnsSameTextAsOriginalFile() {
        byte[] bytes = lzw.encode(testString);
        String decoded = lzw.decode(bytes);
        assertEquals(testString, decoded);
    }
    
    @Test
    public void decodeChangesUnknownCodeToTilde() {
        // Pakataan merkkijono "eđađa", jossa ohjelmalle tuntematon merkki "đ"
        byte[] bytes = new byte[7];
        bytes[0] = (byte) 4;
        bytes[1] = (byte) 9;
        bytes[2] = (byte) 3;
        bytes[3] = (byte) 44;
        bytes[4] = (byte) 68;
        bytes[5] = (byte) 195;
        bytes[6] = (byte) 1;
        String testStringUnrecognized = lzw.decode(bytes);
        assertEquals("e~a~a", testStringUnrecognized);
    }
    
    @Test 
    public void decodeHandlesSituationWhereCodeIsNextToAddToDictionary() {
        byte[] bytes = lzw.encode(testStringLineFeed);
        String decoded = lzw.decode(bytes);
        assertEquals(testStringLineFeed, decoded);
    }
    
}
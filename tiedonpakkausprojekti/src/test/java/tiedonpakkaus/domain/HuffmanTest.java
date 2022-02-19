package tiedonpakkaus.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class HuffmanTest {
    
    Huffman huffman;
    String testString;
    String testStringOneChar;
    String testStringUnknownChar;

    @Before
    public void setUp() {
        testString = "Testikirjoitus testausta varten.";
        testStringOneChar = "a";
        testStringUnknownChar = "ƞ";
        huffman = new Huffman();
    }

    @Test
    public void encodeReturnsByteArrayWithCorrectLength() {
        // Testikirjoituksessa on 15 eri merkkiä; 
        // puun bittijonoon 14 kpl 0-bittiä siirtymille (merkkilkm-1), 
        // ja 15 kpl 9 bitin jonoa merkeille = 149 bittiä;
        // tekstin koodaavaan bittijonoon tulee 118 merkkiä;
        // yhteensä 267 bittiä + 5 ylimääräistä 0-bittiä tasaamaan = 272 bittiä;
        // lisäksi 2*8 bittiä infotavuihin;
        // tavutaulukkoon tallennetaan 288 bittiä -> 288/8 = 36 tavua
        byte[] bytes = huffman.encode(testString);
        assertEquals(36, bytes.length);        
    }

    @Test
    public void encodeReturnsByteArrayWithCorrectLengthForOneCharText() {
        // Testikirjoituksessa yksi merkki;
        // puun bittijonoon 1 kpl 9 bitin jono, tekstin bittijonoon vain koodi "0",
        // 6 ylimääräistä 0-bittiä tasaamaan, ja 2*8 bittiä infotavuihin;
        // tavutaulukkoon tallennetaan 32 bittiä -> 32/8 = 4 tavua
        byte[] bytes = huffman.encode(testStringOneChar);
        assertEquals(4, bytes.length);     
    }
    
    @Test
    public void encodeChangesUnknownCharToTilde() {
        // Puun bittijonon tulisi sisältää bitti 1 ja tilden desimaaliarvo 126 bitteinä 01111110
        // Puun bitit ovat tavutaulukossa indekseissä 2 ja 3, alun 6 ylimääräisen 0:n jälkeen
        byte[] bytes = huffman.encode(testStringUnknownChar);
        int byte2 = bytes[2];
        int byte3 = bytes[3];
        String byte2str = String.format("%8s", Integer.toBinaryString(byte2 & 0xFF))
                    .replace(" ", "0");
        String byte3str = String.format("%8s", Integer.toBinaryString(byte3 & 0xFF))
                    .replace(" ", "0");
        String treeBits = (byte2str + byte3str).substring(6, 15);
        assertEquals("101111110", treeBits);
    }

    /*    
    @Test
    public void decodeReturnsSameTextAsOriginalFile() {
        byte[] bytes = huffman.encode(testString);
        String decoded = huffman.decode(bytes);
        assertEquals(decoded, testString);
    }
    */
}

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
        // lisäksi 3*8 bittiä infotavuihin;
        // tavutaulukkoon tallennetaan 296 bittiä -> 296/8 = 37 tavua
        byte[] bytes = huffman.encode(testString);
        assertEquals(37, bytes.length);        
    }

    @Test
    public void encodeReturnsByteArrayWithCorrectLengthForOneCharText() {
        // Testikirjoituksessa yksi merkki;
        // puun bittijonoon 1 kpl 9 bitin jono, tekstin bittijonoon vain koodi "0",
        // 6 ylimääräistä 0-bittiä tasaamaan, ja 3*8 bittiä infotavuihin;
        // tavutaulukkoon tallennetaan 40 bittiä -> 40/8 = 5 tavua
        byte[] bytes = huffman.encode(testStringOneChar);
        assertEquals(5, bytes.length);     
    }
    
    @Test
    public void encodeChangesUnknownCharToTilde() {
        // Puun bittijonon tulisi sisältää bitti 1 ja tilden desimaaliarvo 126 bitteinä 01111110
        // Puun bitit ovat tavutaulukossa indekseissä 3 ja 4, alun 6 ylimääräisen 0:n jälkeen
        byte[] bytes = huffman.encode(testStringUnknownChar);
        int byte2 = bytes[3];
        int byte3 = bytes[4];
        String byte2str = String.format("%8s", Integer.toBinaryString(byte2 & 0xFF))
                    .replace(" ", "0");
        String byte3str = String.format("%8s", Integer.toBinaryString(byte3 & 0xFF))
                    .replace(" ", "0");
        String treeBits = (byte2str + byte3str).substring(6, 15);
        assertEquals("101111110", treeBits);
    }

    
    @Test
    public void decodeReturnsSameTextAsOriginalFile() {
        byte[] bytes = huffman.encode(testString);
        String decoded = huffman.decode(bytes);
        assertEquals(testString, decoded);
    }
    
    @Test
    public void decodeForOneCharTextBehavesCorrectly() {
        byte[] bytes = huffman.encode(testStringOneChar);
        String decoded = huffman.decode(bytes);
        assertEquals(testStringOneChar, decoded);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void decodeThrowsExceptionIfIllegalNumberOfExtraZeros() {
        // luodaan purettava tavutaulukko, jonka ensimmäisen tavun ilmoittama poistettavien
        // nollien määrä ei ole sallittu (yli 8)
        byte[] bytes = new byte[5];
        bytes[0] = (byte) 9;
        bytes[1] = (byte) 0;
        bytes[2] = (byte) 7;
        bytes[3] = (byte) 7;
        bytes[4] = (byte) 7;
        
        // Virheilmoitukseen päättyvä kutsu
        huffman.decode(bytes);
    }
}

package tiedonpakkaus.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class DecompressorTest {
   
    File lzwDecompressedTestFile = new File("LzwDecompressedTestFile");
    File huffmanDecompressedTestFile = new File("HuffmanDecompressedTestFile");
    File decompressedEmptyTestFile = new File("decompressedEmptyTestFile");
    File decompressedThreeByteTestFile = new File("decompressedThreeByteTestFile");
    File decompressedFourByteTestFile = new File("decompressedFourByteTestFile");
    
    /**
     * Luo Decompressor-luokan testien käyttämät tiedostot ja suorittaa pakkaamiset ja purkamiset
     * @throws IOException
     */
    @BeforeClass
    public static void setUpClass() throws IOException {
        
        FileWriter writerTest = new FileWriter("testfile.txt");
        writerTest.write("Testikirjoitus testausta varten.");
        writerTest.close();
        FileWriter writerEmpty = new FileWriter("emptytestfile.txt");
        writerEmpty.write("");
        writerEmpty.close();
        FileWriter writerFourBytes = new FileWriter("threebytetestfile.txt");
        writerFourBytes.write("aaa");    
        writerFourBytes.close();
        FileWriter writerFiveBytes = new FileWriter("fourbytetestfile.txt");
        writerFiveBytes.write("aaaa");    
        writerFiveBytes.close();
        
        Compressor compressor = new Compressor();
        Decompressor decompressor = new Decompressor();
        
        compressor.compress("LZW", "testfile.txt", "LzwCompressedTestFile");
        compressor.compress("Huffman", "testfile.txt", "HuffmanCompressedTestFile");

        decompressor.decompress("LZW", "LzwCompressedTestFile", "LzwDecompressedTestFile");
        decompressor.decompress("Huffman", "HuffmanCompressedTestFile", 
                "HuffmanDecompressedTestFile");
        
        decompressor.decompress("decompressor", "emptytestfile.txt", 
                "decompressedEmptyTestFile");        
        
        decompressor.decompress("LZW", "threebytetestfile.txt", 
                "LzwDecompressedThreeByteTestFile");
        decompressor.decompress("Huffman", "fourbytetestfile.txt", 
                "HuffmanDecompressedFourByteTestFile");
    }
    
    @Test
    public void lzwDecompressorCreatesDecompressedFile() {
        assertTrue(lzwDecompressedTestFile.exists());
    }
    
    @Test
    public void huffmanDecompressorCreatesDecompressedFile() {
        assertTrue(huffmanDecompressedTestFile.exists());
    }
    
    @Test
    public void noDecompressionIfFileIsEmpty() throws IOException {
        assertTrue(!decompressedEmptyTestFile.exists());        
    }

    @Test
    public void lzwNoDecompressionIfFileIsTooShort() throws IOException {
        assertTrue(!decompressedThreeByteTestFile.exists());        
    }
    
    @Test
    public void huffmanNoDecompressionIfFileIsTooShort() throws IOException {
        assertTrue(!decompressedFourByteTestFile.exists());        
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void exceptionThrownIfCompressorNotRecognized() throws IOException {
        Decompressor nonDecompressor = new Decompressor();
        
        // Virheilmoitukseen päättyvä kutsu, jossa pakkaaja jota ei tunnisteta
        nonDecompressor.decompress("NonDecompressor", "LzwDecompressedTestFile", 
                "NoDecompressedTestFile");
    }
    
    @Test
    public void lzwDecompressorReturnsCorrectTextInFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(lzwDecompressedTestFile));
        String textFromDecompressedFile = "";
        String line;
        while ((line = br.readLine()) != null) {
            textFromDecompressedFile += line + "\n";
        } 
        textFromDecompressedFile = textFromDecompressedFile.substring(0, 
                textFromDecompressedFile.length() - 1);

        assertEquals("Testikirjoitus testausta varten.", textFromDecompressedFile);
    }   
        
    @Test
    public void huffmanDecompressorReturnsCorrectTextInFile() throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(huffmanDecompressedTestFile));
        String textFromDecompressedFile = "";
        String line;
        while ((line = br.readLine()) != null) {
            textFromDecompressedFile += line + "\n";
        } 
        textFromDecompressedFile = textFromDecompressedFile.substring(0, 
                textFromDecompressedFile.length() - 1);

        assertEquals("Testikirjoitus testausta varten.", textFromDecompressedFile);
    } 
    

    /**
     * Poistaa testiluokan luomat tiedostot.
     * @throws IOException
     */
    @AfterClass
    public static void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get("testfile.txt"));
        Files.deleteIfExists(Paths.get("emptytestfile.txt"));
        Files.deleteIfExists(Paths.get("threebytetestfile.txt"));
        Files.deleteIfExists(Paths.get("fourbytetestfile.txt"));
        Files.deleteIfExists(Paths.get("LzwCompressedTestFile"));
        Files.deleteIfExists(Paths.get("HuffmanCompressedTestFile"));
        Files.deleteIfExists(Paths.get("LzwDecompressedTestFile"));
        Files.deleteIfExists(Paths.get("HuffmanDecompressedTestFile"));        
        Files.deleteIfExists(Paths.get("decompressedEmptyTestFile")); 
        Files.deleteIfExists(Paths.get("LzwDecompressedThreeByteTestFile"));        
        Files.deleteIfExists(Paths.get("HuffmanDecompressedFourByteTestFile"));

        
    }
    
}

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
    File lzwDecompressedFaultyTestFile = new File("lzwDecompressedFaultyTestFile");
    File huffmanDecompressedFaultyTestFile = new File("huffmanDecompressedFaultyTestFile");
    File noDecompressedTestFile = new File("noDecompressedTestFile");
    
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
        FileWriter writerFaultyFile = new FileWriter("faultytestfile.txt");
        writerFaultyFile.write("a");    
        writerFaultyFile.close();

        Compressor compressor = new Compressor();
        Decompressor decompressor = new Decompressor();
        
        compressor.compress("LZW", "testfile.txt", "LzwCompressedTestFile");
        compressor.compress("Huffman", "testfile.txt", "HuffmanCompressedTestFile");

        decompressor.decompress("LZW", "LzwCompressedTestFile", "LzwDecompressedTestFile");
        decompressor.decompress("Huffman", "HuffmanCompressedTestFile", 
                "HuffmanDecompressedTestFile");
        
        decompressor.decompress("decompressor", "emptytestfile.txt", 
                "decompressedEmptyTestFile");        
        
        decompressor.decompress("LZW", "faultytestfile.txt", "LzwDecompressedFaultyTestFile");
        decompressor.decompress("Huffman", "faultytestfile.txt", 
                "HuffmanDecompressedFaultyTestFile");
    }
    
    @Test
    public void noDecompressionWhenDecompressorNotRecognized() throws IOException {       
        Decompressor nonDecompressor = new Decompressor();
        String s = nonDecompressor.decompress("NonDecompressor", "LzwDecompressedTestFile", 
                "noDecompressedTestFile");
        assertEquals("faulty", s);
        assertTrue(!noDecompressedTestFile.exists());
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
    public void lzwNoDecompressionIfFileIsFaulty() throws IOException {
        assertTrue(!lzwDecompressedFaultyTestFile.exists());        
    }
    
    @Test
    public void huffmanNoDecompressionIfFileIsFaulty() throws IOException {
        assertTrue(!huffmanDecompressedFaultyTestFile.exists());        
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
        Files.deleteIfExists(Paths.get("faultytestfile.txt"));         
        Files.deleteIfExists(Paths.get("LzwCompressedTestFile"));
        Files.deleteIfExists(Paths.get("HuffmanCompressedTestFile"));
        Files.deleteIfExists(Paths.get("LzwDecompressedTestFile"));
        Files.deleteIfExists(Paths.get("HuffmanDecompressedTestFile"));        
        Files.deleteIfExists(Paths.get("decompressedEmptyTestFile")); 
        Files.deleteIfExists(Paths.get("LzwDecompressedFaultyTestFile"));        
        Files.deleteIfExists(Paths.get("HuffmanDecompressedFaultyTestFile"));
        Files.deleteIfExists(Paths.get("noDecompressedTestFile"));        
    }
    
}

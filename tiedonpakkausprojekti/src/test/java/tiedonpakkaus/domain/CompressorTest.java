package tiedonpakkaus.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class CompressorTest {
    
    File lzwCompressedTestFile = new File("LzwCompressedTestFile");
    File huffmanCompressedTestFile = new File("HuffmanCompressedTestFile");
    File lzwCompressedEmptyTestFile = new File("LzwCompressedEmptyTestFile");
    File huffmanCompressedEmptyTestFile = new File("HuffmanCompressedEmptyTestFile");
    File noCompressedTestFile = new File("NoCompressedTestFile");
    
    /**
     * Luo Compressor-luokan testien käyttämät tiedostot ja suorittaa pakkaamiset
     * @throws IOException
     */
    @BeforeClass
    public static void setUp() throws IOException {

        FileWriter writerTest = new FileWriter("testfile.txt");
        writerTest.write("Testikirjoitus testausta varten.");
        writerTest.close();
        FileWriter writerEmpty = new FileWriter("emptytestfile.txt");
        writerEmpty.write("");
        writerEmpty.close();
        
        Compressor compressor = new Compressor();       
        compressor.compress("LZW", "testfile.txt", "LzwCompressedTestFile");
        compressor.compress("LZW", "emptytestfile.txt", "LzwCompressedEmptyTestFile");
        compressor.compress("Huffman", "testfile.txt", "HuffmanCompressedTestFile");
        compressor.compress("Huffman", "emptytestfile.txt", "HuffmanCompressedEmptyTestFile");
    }
    
    @Test
    public void noCompressionIfCompressorNotRecognized() throws IOException {
        Compressor nonCompressor = new Compressor();
        String s = nonCompressor.compress("NonCompressor", "testfile.txt", "NoCompressedTestFile");
        assertEquals("faulty", s);
        assertTrue(!noCompressedTestFile.exists());
    }
    
    @Test
    public void lzwCompressorCreatesCompressedFile() {
        assertTrue(lzwCompressedTestFile.exists());
    }
    
    @Test
    public void huffmanCompressorCreatesCompressedFile() throws IOException {
        assertTrue(huffmanCompressedTestFile.exists());
    }
    
    @Test
    public void lzwCompressedFileIsNotEmptyIfOriginalIsNotEmpty() throws IOException {
        assertTrue(lzwCompressedTestFile.length() != 0);
    }
    
    @Test
    public void huffmanCompressedFileIsNotEmptyIfOriginalIsNotEmpty() throws IOException {
        assertTrue(huffmanCompressedTestFile.length() != 0);       
    }
    
    @Test 
    public void lzwNoCompressionIfOriginalIsEmpty() throws IOException {
        assertTrue(!lzwCompressedEmptyTestFile.exists());
    }
    
    @Test 
    public void huffmanNoCompressionIfOriginalIsEmpty() throws IOException {
        assertTrue(!huffmanCompressedEmptyTestFile.exists());
    }    
    
    /**
     * Poistaa testiluokan luomat tiedostot, jos ne on luotu
     * @throws IOException
     */
    @AfterClass
    public static void tearDown() throws IOException {
        Files.deleteIfExists(Paths.get("testfile.txt"));
        Files.deleteIfExists(Paths.get("emptytestfile.txt"));
        Files.deleteIfExists(Paths.get("LzwCompressedTestFile"));
        Files.deleteIfExists(Paths.get("LzwCompressedEmptyTestFile"));
        Files.deleteIfExists(Paths.get("HuffmanCompressedTestFile"));
        Files.deleteIfExists(Paths.get("HuffmanCompressedTestFile"));
        Files.deleteIfExists(Paths.get("NoCompressedTestFile"));
    }
    
}


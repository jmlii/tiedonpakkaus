package tiedonpakkaus.domain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class LZWDecompressorTest {
   
    LZWDecompressor lzwD;
    LZWCompressor lzwC;
    File testfile = null;
    File emptyTestfile = null;
    File compressedTestfile = null;
    File decompressedTestfile = null;
    String pathToTestFile = "";
    String pathToEmptyTestFile = "";
    String pathToCompressedTestFile = "";
    String pathToDecompressedTestFile = "";
    String testString = "";

    @Before
    public void setUp() throws IOException {
        pathToTestFile = "testfile.txt";
        pathToEmptyTestFile = "emptytestfile.txt";
        pathToCompressedTestFile = "testfileCompressed.bin";
        pathToDecompressedTestFile = "testfileDecompressed.txt";
        testString = "Testikirjoitus testausta varten.";
        try {
            FileWriter writer = new FileWriter(pathToTestFile);
            writer.write(testString);
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not write a testfile.");
        }
        try {
            FileWriter writer = new FileWriter(pathToEmptyTestFile);
            writer.write("");
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not write a testfile.");
        }
        
        testfile = new File(pathToTestFile);
        emptyTestfile = new File(pathToEmptyTestFile);
        
        lzwD = new LZWDecompressor();
        lzwC = new LZWCompressor();
        lzwC.compress(pathToTestFile, pathToCompressedTestFile);
        compressedTestfile = new File(pathToCompressedTestFile);
    }
    
    @Test
    public void LZWDecompressorCreatesADecompressedFile() throws IOException {
        String pathToDecompressed = lzwD.decompress(pathToCompressedTestFile, pathToDecompressedTestFile);
        decompressedTestfile = new File(pathToDecompressed);
        assertTrue(decompressedTestfile.exists());
    }
    
    @Test
    public void noDecompressionIfFileIsEmpty() throws IOException {
        String pathToDecompressed = lzwD.decompress(pathToEmptyTestFile, pathToDecompressedTestFile);
        decompressedTestfile = new File(pathToDecompressed);
        assertTrue(!decompressedTestfile.exists());
    }
    
    @Test
    public void LZWDecompressorReturnsCorrectTextInFile() throws IOException {
        String pathToDecompressed = lzwD.decompress(pathToCompressedTestFile, pathToDecompressedTestFile);
        decompressedTestfile = new File(pathToDecompressed);

        BufferedReader br = new BufferedReader(new FileReader(decompressedTestfile));
        String textFromDecompressedFile = "";
        String line;
        while ((line = br.readLine()) != null) {
            textFromDecompressedFile += line + "\n";
        } 
        textFromDecompressedFile = textFromDecompressedFile.substring(0, textFromDecompressedFile.length()-1);

        assertEquals(testString, textFromDecompressedFile);
    }
    
    @After
    public void tearDown() {
        testfile.delete();
        emptyTestfile.delete();
        compressedTestfile.delete();
        decompressedTestfile.delete();
    }
}

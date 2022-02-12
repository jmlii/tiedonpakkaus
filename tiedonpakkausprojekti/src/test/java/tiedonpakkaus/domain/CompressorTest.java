package tiedonpakkaus.domain;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.After;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class CompressorTest {
    
    Compressor compressor;
    File testfile = null;
    File emptyTestfile = null;
    File compressedTestfile = null;
    String pathToTestFile = "";
    String pathToEmptyTestFile = "";
    String pathToCompressedTestFile = "";
    String compressorAlgorithm = "";

    @Before
    public void setUp() {
        pathToTestFile = "testfile.txt";
        pathToEmptyTestFile = "emptytestfile.txt";
        pathToCompressedTestFile = "testfileCompressed.bin";
        compressorAlgorithm = "LZW";
        try {
            FileWriter writer = new FileWriter(pathToTestFile);
            writer.write("Testikirjoitus testausta varten.");
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
        compressor = new Compressor();
    }

    @Test
    public void LZWCompressorCreatesACompressedFile() throws IOException {
        String pathToCompressed = compressor.compress(compressorAlgorithm, pathToTestFile, pathToCompressedTestFile);
        compressedTestfile = new File(pathToCompressed);
        assertTrue(compressedTestfile.exists());
    }
    
    @Test
    public void compressedFileIsNotEmptyIfOriginalIsNotEmpty() throws IOException {
        String pathToCompressed = compressor.compress(compressorAlgorithm, pathToTestFile, pathToCompressedTestFile);
        compressedTestfile = new File(pathToCompressed);
        assertTrue(compressedTestfile.length() != 0);
    }
    
    @Test 
    public void noCompressionIfOriginalIsEmpty() throws IOException {
        String pathToCompressed = compressor.compress(compressorAlgorithm, pathToEmptyTestFile, pathToCompressedTestFile);
        compressedTestfile = new File(pathToCompressed);
        assertTrue(!compressedTestfile.exists());
    }
    
    @After
    public void tearDown() {
        testfile.delete();
        emptyTestfile.delete();
        compressedTestfile.delete();
    }
    
}


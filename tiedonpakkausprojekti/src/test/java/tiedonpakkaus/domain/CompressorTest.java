package tiedonpakkaus.domain;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.After;
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

    @Test(expected = IllegalArgumentException.class)
    public void exceptionThrownIfCompressorNotRecognized() throws IOException {
        compressorAlgorithm = "NoCompressor";
        
        // Luodaaan mock-tiedosto, jotta joka testin jälkeen tehtävä tiedoston poisto onnistuu
        FileOutputStream writer = new FileOutputStream(pathToCompressedTestFile);
        writer.write(1);
        writer.close();
        compressedTestfile = new File(pathToCompressedTestFile);
        
        // Virheilmoitukseen päättyvä kutsu
        compressor.compress(compressorAlgorithm, pathToTestFile, pathToCompressedTestFile);
    }
    
    @Test
    public void lzwCompressorCreatesCompressedFile() throws IOException {
        compressorAlgorithm = "LZW";
        String pathToCompressed = compressor.compress(compressorAlgorithm, pathToTestFile, 
                pathToCompressedTestFile);
        compressedTestfile = new File(pathToCompressed);
        assertTrue(compressedTestfile.exists());
    }
    
    @Test
    public void huffmanCompressorCreatesCompressedFile() throws IOException {
        compressorAlgorithm = "Huffman";
        String pathToCompressed = compressor.compress(compressorAlgorithm, pathToTestFile, 
                pathToCompressedTestFile);
        compressedTestfile = new File(pathToCompressed);
        assertTrue(compressedTestfile.exists());
    }
    
    @Test
    public void lzwCompressedFileIsNotEmptyIfOriginalIsNotEmpty() throws IOException {
        compressorAlgorithm = "LZW";
        String pathToCompressed = compressor.compress(compressorAlgorithm, pathToTestFile, 
                pathToCompressedTestFile);
        compressedTestfile = new File(pathToCompressed);
        assertTrue(compressedTestfile.length() != 0);
    }
    
    @Test
    public void huffmanCompressedFileIsNotEmptyIfOriginalIsNotEmpty() throws IOException {
        compressorAlgorithm = "Huffman";
        String pathToCompressed = compressor.compress(compressorAlgorithm, pathToTestFile, 
                pathToCompressedTestFile);
        compressedTestfile = new File(pathToCompressed);
        assertTrue(compressedTestfile.length() != 0);       
    }
    
    @Test 
    public void lzwNoCompressionIfOriginalIsEmpty() throws IOException {
        compressorAlgorithm = "LZW";
        String pathToCompressed = compressor.compress(compressorAlgorithm, pathToEmptyTestFile, 
                pathToCompressedTestFile);
        compressedTestfile = new File(pathToCompressed);
        assertTrue(!compressedTestfile.exists());
    }
    
    @Test 
    public void huffmanNoCompressionIfOriginalIsEmpty() throws IOException {
        compressorAlgorithm = "Huffman";
        String pathToCompressed = compressor.compress(compressorAlgorithm, pathToEmptyTestFile, 
                pathToCompressedTestFile);
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


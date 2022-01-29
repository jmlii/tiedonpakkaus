package tiedonpakkaus.domain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;


public class LZWTest {
    LZW lzw;
    File file = null;

    @Before
    public void setUp() {
        String pathToTestFile = "testfile.txt";
        try {
            FileWriter writer = new FileWriter(pathToTestFile);
            writer.write("Testikirjoitus testitiedostoon testausta varten.");
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not write a testfile.");
        }
        file = new File(pathToTestFile);
        lzw = new LZW();
    }

    @Test
    public void encodedListFirstCharHasCorrectIntValue() throws FileNotFoundException {
        int tCapital = 84;
        int firstCharFromFile = lzw.encode(file).get(1);
        assertEquals(tCapital, firstCharFromFile);
    }

    
    @After
    public void tearDown() {
        file.delete();
    }
    
}

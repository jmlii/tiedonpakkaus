package tiedonpakkaus.domain;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Palveluluokka pakatun tiedoston purkamiselle.
 */
public class Decompressor {
     
    /**
     * Purkaa pakatun tiedoston tekstitiedostoksi
     * @param algorithm käyttäjän ilmoittama pakkausalgoritmi
     * @param pathInput käyttäjän osoittama purettavan tiedoston osoite
     * @param pathOutput käyttäjän antama osoite puretulle tiedostolle
     * @return puretun tiedoston osoite
     * @throws IOException 
     */
    public String decompress(String algorithm, String pathInput, String pathOutput) 
            throws IOException {
        
        LZW lzw = new LZW(); 
        Huffman h = new Huffman();

        byte[] bytes = Files.readAllBytes(Paths.get(pathInput));
        if (bytes.length == 0) {
            return "empty";
        } 
        
        String text = "";
        
        try {
            if (algorithm.equals("LZW")) { 
                text = lzw.decode(bytes);
            } else if (algorithm.equals("Huffman")) {
                text = h.decode(bytes);
            } else {
                throw new IllegalArgumentException("Haluttua menetelmää ei tunnistettu.");
            } 
        } catch (Exception ex) {
            return ("faulty");
        }
        
        String decompressed = textToFile(text, pathOutput);
        return decompressed;
    }
    
    /**
     * Tallentaa tekstin tiedostoon.
     * @param text tallennettava teksti merkkijonona
     * @param filePath tallennettavan tiedoston osoite
     * @return tallennettavan tiedoston osoite
     * @throws IOException 
     */
    private String textToFile(String text, String filePath) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
        bw.write(text);
        bw.close();
        
        return filePath;
    }
        
}

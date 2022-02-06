package tiedonpakkaus.domain;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * Palveluluokka LZW-algoritmin mukaiselle tiedoston purkamiselle.
 */
public class LZWDecompressor {
     
    /**
     * Purkaa pakatun tiedoston tekstitiedostoksi
     * @param pathToDecompress käyttäjän osoittama purettavan tiedoston osoite
     * @param pathToDecompressedFile käyttäjän antama osoite puretulle tiedostolle
     * @return puretun tiedoston osoite
     * @throws IOException 
     */
    public String decompress(String pathToDecompress, String pathToDecompressedFile) 
            throws IOException {
        LZW lzw = new LZW();  

        byte[] bytes = Files.readAllBytes(Paths.get(pathToDecompress));
        if (bytes.length == 0) {
            return "null (purettavaksi annettu tiedosto on tyhjä, purkua ei suoritettu)";
        }
        
        String text = lzw.decode(bytes);
        
        String decompressed = textToFile(text, pathToDecompressedFile);
        return decompressed;
    }
    
    /**
     * Tallentaa tekstin tiedostoon.
     * @param text tallennettava teksti merkkijonona
     * @param fileName tallennettavan tiedoston osoite
     * @return tallennettavan tiedoston osoite
     * @throws IOException 
     */
    private String textToFile(String text, String fileName) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
        bw.write(text);
        bw.close();
        
        return fileName;
    }
        
}

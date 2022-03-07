package tiedonpakkaus.domain;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Palveluluokka tiedoston pakkaamiselle.
 */
public class Compressor {
    
    /**
     * Lukee tiedoston ja pakkaa sen tekstin pienemmäksi tiedostoksi.
     * @param compressor käyttäjän valitsema pakkausalgoritmi
     * @param pathInput käyttäjän osoittaman pakattavan tiedoston osoite
     * @param pathOutput käyttäjän antama osoite pakatulle tiedostolle
     * @return pakatun tiedoston osoite
     * @throws java.io.IOException jos tiedostoa ei löydy tai siihen ei päästä käsiksi
     */
    public String compress(String compressor, String pathInput, String pathOutput) 
            throws IOException  {
        LZW lzw = new LZW();
        Huffman h = new Huffman();
        
        // Luetaan pakattavan tiedoston sisältö merkkijonoksi
        String text = readFile(pathInput);

        // Tarkistetaan, että tiedostossa oli tekstisisältöä
        if (text.isEmpty()) {
            return "empty";
        }

        // Pakataan tiedoston sisältö
        // Mahdolliset suorituksen aikaiset virheet keskeyttävät pakkaamisen
        byte[] bytes;
        
        try {
            if (compressor.equals("LZW")) { 
                bytes = lzw.encode(text);
            } else if (compressor.equals("Huffman")) {
                bytes = h.encode(text);
            } else {
                throw new IllegalArgumentException("Haluttua pakkausmenetelmää ei tunnistettu.");
            }
        } catch (Exception ex) {
            return ("faulty");
        }
               
        // Pakatun tiedon tallennus tiedostoksi käyttäjän ilmoittamaan paikkaan
        String compressed = bytesToFile(bytes, pathOutput);
        return compressed;
    }
    
    /**
     * Lukee tekstin annetusta tiedostosta.
     * @param file käyttäjän osoittama tiedosto
     * @return teksti merkkijonona
     */
    private String readFile(String file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        StringBuilder textFromFile = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            textFromFile.append(line).append("\n");
        } 
        
        return textFromFile.toString();
    }
    
    /**
     * Tallentaa tavutaulukon tiedostoksi. 
     * @param bytes tavutaulukko
     * @param filePath pakatulle tiedostolle annettava nimi (polku ja tiedostonimi)
     * @return pakatun tiedoston polku
     * @throws java.io.IOException
     */
    private String bytesToFile(byte[] bytes, String filePath) throws IOException {
        
        BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(filePath));
        for (byte b : bytes) {
            writer.write(b);
        } 
        
        writer.close();
        return filePath;
    }

}

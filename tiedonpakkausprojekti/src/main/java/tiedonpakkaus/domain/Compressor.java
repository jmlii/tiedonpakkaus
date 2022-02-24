package tiedonpakkaus.domain;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Palveluluokka tiedoston pakkaamiselle.
 */
public class Compressor {
    
    /**
     * Lukee tiedoston ja pakkaa sen tekstin pienemmäksi tiedostoksi.
     * @param compressor käyttäjän valitsema pakkausalgoritmi
     * @param pathToCompress käyttäjän osoittaman pakattavan tiedoston osoite
     * @param pathToCompressedFile käyttäjän antama osoite pakatulle tiedostolle
     * @return pakatun tiedoston osoite
     * @throws java.io.IOException jos tiedostoa ei löydy tai siihen ei päästä käsiksi
     */
    public String compress(String compressor, String pathToCompress, String pathToCompressedFile) 
            throws IOException  {
        LZW lzw = new LZW();
        Huffman h = new Huffman();
        
        
        // Luetaan pakattavan tiedoston sisältö merkkijonoksi
        String text = readFile(pathToCompress);

        // Tarkistetaan, että tiedostossa oli tekstisisältöä,
        // ja ilmoitetaan käyttäjälle, jos tiedosto oli tyhjä
        if (text.isEmpty()) {
            return "null (pakattavaksi annettu tiedosto on tyhjä, pakkausta ei suoritettu)";
        }

        // Pakataan tiedoston sisältö
        byte[] bytes;
        if (compressor.equals("LZW")) { 
            bytes = lzw.encode(text);
        } else if (compressor.equals("Huffman")) {
            bytes = h.encode(text);
        } else {
            throw new IllegalArgumentException("Haluttua pakkausmenetelmää ei tunnistettu.");
        }
        
        String encodedFileName = pathToCompressedFile;
        
        // Pakatun tiedon tallennus tiedostoksi käyttäjän ilmoittamaan paikkaan
        String compressed = bytesToFile(bytes, encodedFileName);
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
     * @param fileName pakatulle tiedostolle annettava nimi (polku ja tiedostonimi)
     * @return pakatun tiedoston polku
     * @throws java.io.IOException
     */
    private String bytesToFile(byte[] bytes, String fileName) throws IOException {
        
        BufferedOutputStream writer = new BufferedOutputStream(new FileOutputStream(fileName));
        for (byte b : bytes) {
            writer.write(b);
        } 
        
        writer.close();
        return fileName;
    }

}

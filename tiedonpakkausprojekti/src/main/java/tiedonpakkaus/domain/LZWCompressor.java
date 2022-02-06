package tiedonpakkaus.domain;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

/**
 * Palveluluokka LZW-algoritmin mukaiselle tiedoston pakkaamiselle.
 */
public class LZWCompressor {
    
    /**
     * Lukee tiedoston ja pakkaa sen tekstin pienemmäksi tiedostoksi.
     * @param pathToCompress käyttäjän osoittaman pakattavan tiedoston osoite
     * @param pathToCompressedFile käyttäjän antama osoite pakatulle tiedostolle
     * @return pakatun tiedoston osoite
     */
    public String compress(String pathToCompress, String pathToCompressedFile) throws IOException  {
       
        File inputFile = new File(pathToCompress);
        LZW lzw = new LZW();
        
        String text = readFile(inputFile);

        if (text.isEmpty()) {
            return "null (pakattavaksi annettu tiedosto on tyhjä, pakkausta ei suoritettu)";
        }

        byte[] bytes = lzw.encode(text);
        
        String encodedFileName = pathToCompressedFile;
        
        String compressed = bytesToFile(bytes, encodedFileName);
        return compressed;
    }
    
    /**
     * Lukee tekstin annetusta tiedostosta.
     * @param file käyttäjän osoittama tiedosto
     * @return teksti merkkijonona
     */
    private String readFile(File file) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(file));
        String textFromFile = "";
        String line;
        while ((line = br.readLine()) != null) {
            textFromFile += line + "\n";
        } 
        // poistetaan lopusta tiedostoa lukiessa tuleva ylimääräinen rivinvaihto
        if (!textFromFile.isEmpty()) {
            textFromFile = textFromFile.substring(0, textFromFile.length() - 1);
        }
        return textFromFile;
    }
    
    /**
     * Tallentaa tavutaulukon tiedostoksi. 
     * @param bytes tavutaulukko
     * @param fileName pakatulle tiedostolle annettava nimi (polku ja tiedostonimi)
     * @return pakatun tiedoston polku
     * @throws java.io.IOException
     */
    private String bytesToFile(byte[] bytes, String fileName) throws IOException {
        FileOutputStream writer = null;
        String name = fileName;
           
        writer = new FileOutputStream(name);
        for (byte b : bytes) {
            writer.write(b);
        } 
        
        writer.close();
        return name;
    }

}

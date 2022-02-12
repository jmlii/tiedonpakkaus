package tiedonpakkaus.ui;

import java.io.IOException;
import java.util.Scanner;
import tiedonpakkaus.domain.Compressor;
import tiedonpakkaus.domain.Decompressor;

public class UserInterface {
    
    private final Scanner scanner;
    
    /**
     * Käyttöliittymän konstruktori.
     */
    public UserInterface() {   
        scanner = new Scanner(System.in);
    }
    
    /**
     * Ohjelman käynnistys.
     */
    public void start() {
        
        System.out.println("***** Tekstitiedoston pakkaus ja purku *****");      
         
        /* Tiedoston osoitteen kysely: 
        System.out.println("Anna pakattavan tiedoston osoite:");
        
        String pathToCompress = scanner.nextLine();
        */
        

        String pathToCompress = "files/f.txt";
        String pathToCompressedFile = "files/file_compressed.bin";
        String pathToDecompress = "files/file_compressed.bin";
        String pathToDecompressedFile = "files/file_decompressed.txt";
        String compressorAlgorithm = "LZW";
        String decompressorAlgorithm = "LZW";
        
        Compressor compressor = new Compressor();
        Decompressor decompressor = new Decompressor();
        
        String path = "";
        
        try {
            path = compressor.compress(compressorAlgorithm, pathToCompress, pathToCompressedFile);
            System.out.println("Pakattu tiedosto tallennettu osoitteeseen " + path);            
        } catch (IOException ex) {
            System.out.println("Tiedostoa ei löydy, tarkista osoite");
        }
        
        try {
            path = decompressor.decompress(decompressorAlgorithm, pathToDecompress, 
                    pathToDecompressedFile);
            System.out.println("Purettu tiedosto tallennettu osoitteeseen " + path);
        } catch (IOException ex) {
            System.out.println("Purettavaa tiedostoa ei löydy, tarkista osoite");
        }

    }
}

package tiedonpakkaus;

import java.io.IOException;
import tiedonpakkaus.domain.LZWCompressor;
import tiedonpakkaus.domain.LZWDecompressor;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        // Scanner inputScanner = new Scanner(System.in);
        
        System.out.println("***** Tekstitiedoston pakkaus ja purku *****");
        
        /* Tiedoston osoitteen kysely: 
        System.out.println("Anna pakattavan tiedoston osoite:");
        
        String pathToCompress = inputScanner.nextLine();
        */
        

        String pathToCompress = "files/f.txt";
        String pathToCompressedFile = "files/file_compressed.bin";
        String pathToDecompress = "files/file_compressed.bin";
        String pathToDecompressedFile = "files/file_decompressed.txt";
        
        LZWCompressor lzwC = new LZWCompressor();
        LZWDecompressor lzwD = new LZWDecompressor();
        
        String path = "";
        
        try {
            path = lzwC.compress(pathToCompress, pathToCompressedFile);
            System.out.println("Pakattu tiedosto tallennettu osoitteeseen " + path);            
        } catch (IOException ex) {
            System.out.println("Tiedostoa ei löydy, tarkista osoite");
        }
        
        try {
            path = lzwD.decompress(pathToDecompress, pathToDecompressedFile);
            System.out.println("Purettu tiedosto tallennettu osoitteeseen " + path);
        } catch (IOException ex) {
            System.out.println("Purettavaa tiedostoa ei löydy, tarkista osoite");
        }
        
    }    
}

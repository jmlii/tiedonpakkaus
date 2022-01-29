package tiedonpakkaus;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import tiedonpakkaus.domain.LZW;

public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        
        LZW lzw = new LZW();
        Scanner inputScanner = new Scanner(System.in);
        
        System.out.println("***** Tekstitiedoston pakkaus ja purku *****");
        
        /* Tiedoston osoitteen kysely: 
        System.out.println("Anna pakattavan tiedoston osoite:");
        
        String fileAddress = inputScanner.nextLine();
        File file = new File(fileAddress);
      
        try {
            List<Integer> compressedIntList = lzw.encode(file);
            System.out.println(compressedIntList);
        } catch (FileNotFoundException e) {
            System.out.println("Tiedostoa ei löydy, tarkista osoite");
        }
        */
        

        String fileAddress = "files/f.txt";
        File file = new File(fileAddress);
      
        List<Integer> compressedIntList = new ArrayList<>();
        
        try {
            compressedIntList = lzw.encode(file);
            System.out.println(compressedIntList);
        } catch (FileNotFoundException e) {
            System.out.println("Tiedostoa ei löydy, tarkista osoite");
        }
        String bitsAsString = lzw.codesToBits(compressedIntList);
        System.out.println(bitsAsString);
    
    }    
}

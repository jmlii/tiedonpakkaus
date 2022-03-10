package tiedonpakkaus.ui;

import java.io.IOException;
import java.util.Scanner;
import tiedonpakkaus.domain.Compressor;
import tiedonpakkaus.domain.Decompressor;
import tiedonpakkaus.util.PerformanceTester;

public class UserInterface {
        
    private String[] commandDescriptions = {
        "1  -  Pakkaa tiedosto",
        "2  -  Pura pakattu tiedosto",
        "3  -  Aja suorituskykytestit",
        "4  -  Sulje ohjelma"
    };

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
    public void start() throws IOException {
        
        System.out.println("***** Tekstitiedoston pakkaus ja purku *****");      
        
        while (true) {
            printCommands();
            String input = scanner.nextLine();

            if (input.equals("1")) {
                execute(1);
            } else if (input.equals("2")) {
                execute(2);
            } else if (input.equals("3")) {
                runPerformanceTests();                
            } else if (input.equals("4")) {
                System.exit(0);
            } else {
                System.out.println("Valitse toiminto 1, 2, 3 tai 4");
            }
            System.out.println("");
        }
    }
        
    /**
     * Tulostaa toimintovalikon.
     */
    private void printCommands() {
        System.out.println("Valitse toiminto:");
        for (String command : commandDescriptions) {
            System.out.println(command);            
        }
        System.out.println("");
    }

    /**
     * Käynnistää tiedoston pakkaamisen tai purkamisen käyttäjän valinnan mukaisesti
     * @param input Käyttäjän valitsema toiminto
     */
    private void execute(int input) {
        
        System.out.println("\n** Palaa päävalikkoon komennolla R **");
        
        System.out.println("Anna käsiteltävän tiedoston osoite:");
        String pathInput = scanner.nextLine();
        if (pathInput.equalsIgnoreCase("R")) {
            return;
        }
        
        System.out.println("Anna osoite käsitellylle tiedostolle:");
        String pathOutput = scanner.nextLine();
        if (pathOutput.equalsIgnoreCase("R")) {
            return;
        }  else if (pathOutput.equals(pathInput)) {
            System.out.println("Ei voida tallentaa alkuperäisen tiedoston päälle.");
            if (input == 1) {
                System.out.println("Osoitteeksi muutettu " + pathInput + "_compressed.bin");
                pathOutput = pathInput + "_compressed.bin";
            } else {
                System.out.println("Osoitteeksi muutettu " + pathInput + "_decompressed.txt");
                pathOutput = pathInput + "_decompressed.txt";
            }
        } else if (pathOutput.equals("")) {
            if (input == 1) {
                System.out.println("Osoitteeksi asetettu " + pathInput + "_compressed.bin");
                pathOutput = pathInput + "_compressed.bin";
            } else {
                System.out.println("Osoitteeksi asetettu " + pathInput + "_decompressed.txt");
                pathOutput = pathInput + "_decompressed.txt";
            }
        }
        
        String algorithm = askAlgorithm();
        if (algorithm.equalsIgnoreCase("R")) {
            return;
        }
                
        if (input == 1) {
            // Kun toiminnoksi valittiin pakkaaminen
            Compressor compressor = new Compressor();
            String path = "";
            try {
                path = compressor.compress(algorithm, pathInput, pathOutput);
                if (path.equals("empty")) {
                    System.out.println("Pakattavaksi annettu tiedosto on tyhjä, "
                            + "pakkausta ei suoritettu.");
                } else if (path.equals("faulty")) {
                    System.out.println("Pakkaaminen epäonnistui.");
                } else {
                    System.out.println("Pakattu tiedosto tallennettu osoitteeseen " + path);
                }
            } catch (IOException ex) {
                System.out.println("Käsiteltävää tiedostoa tai haluttua tallennuspaikkaa ei löydy, "
                        + "tarkista osoitteet.");
            }
        } else if (input == 2) {
            // Kun toiminnoksi valittiin purkaminen
            Decompressor decompressor = new Decompressor(); 
            String path = "";
            try {
                path = decompressor.decompress(algorithm, pathInput, pathOutput);
                if (path.equals("empty")) {
                    System.out.println("Purettavaksi annettu tiedosto on tyhjä, "
                            + "purkua ei suoritettu.");
                } else if (path.equals("faulty")) {
                    System.out.println("Purkaminen epäonnistui.");
                } else {
                    System.out.println("Purettu tiedosto tallennettu osoitteeseen " + path);
                }
            } catch (IOException ex) {
                System.out.println("Käsiteltävää tiedostoa tai haluttua tallennuspaikkaa ei löydy,"
                        + " tarkista osoitteet.");
            }
        } else {
            // Ohjelman ei pitäisi ikinä saapua tähän
            System.out.println("Mitään operaatiota ei suoritettu.");
        }
    }
    
    /**
     * Tiedoston pakkaamisessa tai purkamisessa käytettävän pakkausalgoritmin valinta
     * @return Käyttäjän valinta algoritmiksi tai paluukomento
     */
    private String askAlgorithm() {
        while (true) {
            System.out.println("Pakkausmenetelmä: L = Lempel-Ziv-Welch, H = Huffman.");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("L")) {
                return "LZW";
            } else if (input.equalsIgnoreCase("H")) {
                return "Huffman";
            } else if (input.equalsIgnoreCase("R")) {
                return "R";
            } else {
                System.out.println("Annettu pakkausmenetelmä ei kelpaa.");
            }
        }
    }

    /**
     * Suorituskykytestien suorittaminen.
     */
    private void runPerformanceTests() throws IOException {
        
        System.out.println("\n** Palaa päävalikkoon komennolla R **");
        
        System.out.println("Anna testattavan tiedoston osoite tai tiedostot sisältävän hakemiston "
                + "osoite:");
        String pathToTest = scanner.nextLine();
        if (pathToTest.equalsIgnoreCase("R")) {
            return;
        }
        System.out.println("Anna hakemisto tulostiedostojen tallettamiselle "
                + "tai jätä tyhjäksi jos haluat tulokset ruudulle:");
        String pathToResultDir = scanner.nextLine();
        if (pathToResultDir.equalsIgnoreCase("R")) {
            return;
        }
        
        PerformanceTester tester = new PerformanceTester();
        String output = tester.testPerformance(pathToTest, pathToResultDir);
        if (output == null) {
            System.out.println("Testaus ei onnistunut. Tarkista testattavien tiedostojen osoite.");
            return;
        } else if (!output.equals("")) {
            System.out.println("Suorituskykytestien tulokset talletettu osoitteisiin " + output);
        }
       
        while (true) {
            System.out.println("Poistetaanko suorituskykytestauksen aikana muodostuneet "
                    + "pakkaus- ja purkutiedostot?");
            System.out.println("Y = kyllä N = ei");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("Y")) {
                tester.removeTestOutputFiles();
                return;
            } else if (input.equalsIgnoreCase("N")) {
                return;
            } else {
                System.out.println("Valitse Y tai N");
            }
        }
        
    }

}

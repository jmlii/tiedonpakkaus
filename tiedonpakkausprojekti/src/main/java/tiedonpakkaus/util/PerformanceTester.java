package tiedonpakkaus.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import tiedonpakkaus.domain.Compressor;
import tiedonpakkaus.domain.Decompressor;

/**
 * Tekstitiedostojen pakkaamisen ja purkamisen suorituskykytestit.
 * 
 */
public class PerformanceTester {
    
    List<String> compressables = new ArrayList<>();
    List<String> results = new ArrayList<>();
    List<String> resultsMd = new ArrayList<>();
    
    String[] algorithms = {
        "LZW",
        "Huffman"
    };
    
    Compressor compressor = new Compressor();
    Decompressor decompressor = new Decompressor();
        
    /**
     * Testaa algoritmien suorituskykyä.
     * @param pathToTest Testattavan tiedoston tai hakemiston osoite
     * @param pathToResultDir Hakemisto tulostiedostojen tallettamiselle
     * @return Testaustulokset sisältävien tiedostojen osoitteet
     */
    public String testPerformance(String pathToTest, String pathToResultDir) {

        // Lisätään testattavat tiedostonimet listalle
        // Jos polku on virheellinen tai sitä ei voida käsitellä, ilmoitetaan käyttäjälle
        File path = new File(pathToTest);
        
        if (path.isFile()) {
            compressables.add(path.getPath());
        } else  {
            try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(pathToTest))) {
                for (Path pathInDir : stream) {
                    if (!Files.isDirectory(pathInDir)) {
                        compressables.add(pathInDir.toString());
                        
                    }
                } 
            } catch (IOException ex) {
                System.out.println("Tiedostoa tai hakemistoa ei löydy tai ei voida käsitellä.");
                return null;
            }
        }     
        
        // Järjestetään tiedostojen nimet aakkosjärjestykseen
        Collections.sort(compressables);
 
        // Aloitetaan testaus
        System.out.println("Ajetaan suorituskykytestejä...");
        
        for (int i = 0; i < compressables.size(); i++) {
            
            String algorithm = "";
            
            String pathInput = compressables.get(i);
            String pathCompressOutput = compressables.get(i) + "_compressed.bin";
            String pathDecompressOutput = compressables.get(i) + "_decompressed.txt";
            
            long[] compressTimes = new long[9];
            long[] decompressTimes = new long[9];
            
            File fileToCompress = new File(pathInput);
            long inputFileSize = fileToCompress.length();
            
            String pathToCompressed = "";
            String pathToDecompressed = "";
            
            long compressStart; 
            long compressFinish;
            long decompressStart;
            long decompressFinish;
            long compressTime = 0;
            long decompressTime = 0;
            double compressTimeMedian = 0;
            double decompressTimeMedian = 0;
            
            // Suoritetaan testaus kummallakin algoritmilla
            for (int j = 0; j < algorithms.length; j++) {
                algorithm = algorithms[j];
                             
                // Suoritetaan pakkaaminen ja purkaminen 9 kertaa kullekin tiedostolle
                
                // Pakkaaminen ja ajan mittaus
                for (int k = 0; k < 9; k++) {
                    compressStart = System.nanoTime();
                    try {
                        pathToCompressed = compressor.compress(algorithm, pathInput,
                                pathCompressOutput);
                    } catch (IOException ex) {
                        System.out.println("Pakattavaa tiedostoa " + pathInput + " ei löydy tai "
                                + "hakemistopolkuun " + pathCompressOutput + " ei voi tallentaa.");
                    }
                    compressFinish = System.nanoTime();
                    compressTime = compressFinish - compressStart;
                    compressTimes[k] = compressTime;   
                }
                
                // Purkaminen ja ajan mittaus
                for (int k = 0; k < 9; k++) {
                    if (pathToCompressed.equals("empty")) {
                        break;
                    }
                    
                    decompressStart = System.nanoTime();
                    try {
                        pathToDecompressed = decompressor.decompress(algorithm, pathToCompressed,
                            pathDecompressOutput);
                    } catch (IOException ex) {
                        System.out.println("Purettavaa tiedostoa " + pathToCompressed + " ei löydy"
                                + " tai hakemistopolkuun " + pathDecompressOutput 
                                + " ei voi tallentaa.");
                    }
                    decompressFinish = System.nanoTime();
                    decompressTime = decompressFinish - decompressStart;
                    decompressTimes[k] = decompressTime;
                }
                
                // Otetaan talteen pakkaamiseen ja purkamiseen käytettyjen aikojen mediaanit
                Arrays.sort(compressTimes);
                compressTimeMedian = (double) compressTimes[compressTimes.length / 2] / 1000000;
                Arrays.sort(decompressTimes);
                decompressTimeMedian = (double) decompressTimes[decompressTimes.length / 2] 
                        / 1000000;
            
                // Pakatun tiedoston koko
                File compressedFile = new File(pathToCompressed);
                long outputFileSize = compressedFile.length();

                // Pakkaamisen tehokkuus-% (pakatun tiedoston koko suhteessa alkuperäiseen)
                double effectiveness = 100 * (double) outputFileSize / inputFileSize;

                // Talletetaan ja tulostetaan tiedot              
                String result = "Tiedosto: " + fileToCompress.getName() + "\n"
                        + "Algoritmi: " + algorithm + "\n"
                        + "Alkuperäinen koko: " + inputFileSize + " bytes \n"
                        + "Pakattu koko: " + outputFileSize + " bytes \n"
                        + "Pakkausteho: " + String.format("%.3f", effectiveness) + " % \n" 
                        + "Pakkausaika: " + String.format("%.3f", compressTimeMedian) + " ms \n"
                        + "Purkuaika: " + String.format("%.3f", decompressTimeMedian) + " ms \n";
                
                results.add(result);
                                
                if (!pathToResultDir.equals("")) {
                    String resultMd = algorithm + " | " 
                        + inputFileSize + " | " 
                        + outputFileSize + " | " 
                        + String.format("%.3f", effectiveness) + " | " 
                        + String.format("%.3f", compressTimeMedian) + " | " 
                        + String.format("%.3f", decompressTimeMedian);
                          
                    resultsMd.add(resultMd);
                }
            }
        }
        
        if (pathToResultDir.equals("")) {
            // Tulostetaan tulokset ruudulle, jos käyttäjä ei halunnut niitä tiedostoon
            for (String result : results) {
                System.out.println(result);
            } 
        } else {
            // Tulosten tallettaminen tiedostoon
            
            // md-muotoisen taulukon otsikkorivit 
            resultsMd.add(0, "algoritmi | alkuperäinen koko (tavua) | pakattu koko (tavua) | "
                    + "pakattu / alkuperäinen (%) | pakkausaika (ms) | purkuaika (ms)\n"
                    + "---|---|---|---|---|---");

            String pathResults = null; 
            String pathResultsMd = null;

            try {
                pathResults = resultsToFile(results, pathToResultDir + "/performance_results.txt");
                pathResultsMd = resultsToFile(resultsMd, pathToResultDir 
                        + "/performance_resultsMD.txt");
                // Lopuksi palautetaan tulostiedostojen osoitteet
                return pathResults + " ja " + pathResultsMd;
            } catch (IOException ex) {
                System.out.println("Ei voitu tallettaa tiedostoon.");
            }
            
        }
        // Palautetaan tyhjä merkkijono, jos käyttäjä ei halunnut tuloksia tiedostoon
        // tai jos tulosten tallentamissa tapahtui IOException-virhe
        return "";
    }
    
    /**
     * Tallettaa testauksen tulokset tiedostoon.
     * @param resultList Suorituskykytestien tulokset merkkijonolistana
     * @param filePath Tulostiedoston osoite
     */
    private String resultsToFile(List<String> resultList, String filePath) throws IOException {

        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
        for (int i = 0; i < resultList.size(); i++) {
            bw.write(resultList.get(i) + "\n");
        }
        bw.close();
        return filePath;

    }

    /**
     * Poistaa suorituskykytestien aikana luodut tiedostot.
     */
    public void removeTestOutputFiles() {

        for (int i = 0; i < compressables.size(); i++) {
            try {
                Files.deleteIfExists(Paths.get(compressables.get(i) + "_compressed.bin"));
                Files.deleteIfExists(Paths.get(compressables.get(i) + "_decompressed.txt"));
            } catch (IOException ex) {
                System.out.println("Ongelma tiedostojen poistamisessa.");
            }
        }
    }
    
}




package tiedonpakkaus.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import tiedonpakkaus.domain.Compressor;
import tiedonpakkaus.domain.Decompressor;

/**
 * Tekstitiedostojen pakkaamisen ja purkamisen suorituskykytestit.
 * 
 */
public class PerformanceTester {
    
    // Testauksessa käytettävät tiedostot
    // Ohjelma odottaa näiden tiedostojen olevan projektin juuressa olevassa files-kansiossa
    String[] compressables = {
        "files/file64B.txt",
        "files/file128B.txt",
        "files/file256B.txt",
        "files/file512B.txt",
        "files/file1024B.txt",
        "files/file2048B.txt",
        "files/file4096B.txt",
        "files/file8192B.txt",
        "files/file16384B.txt",
        "files/file32768B.txt",
        "files/file65536B.txt",
        "files/file131072B.txt",
        "files/file262144B.txt",
        "files/file524288B.txt",
        "files/file1048576B.txt",
        "files/file2097152B.txt"
    };
    
    String[] algorithms = {
        "LZW",
        "Huffman"
    };
    
    String[] results = new String[32]; 
    String[] resultsMd = new String[33];
    
    Compressor compressor = new Compressor();
    Decompressor decompressor = new Decompressor();
    
    /**
     * Testaa algoritmien suorituskykyä 16 eri kokoisella tiedostolla.
     * Testauksessa käytettävien tiedostojen on sijaittava projektin juuren files-kansiossa.
     */
    public String testPerformance() throws IOException {
        
        System.out.println("Ajetaan suorituskykytestejä...");
        
        for (int i = 0; i < compressables.length; i++) {
            
            String algorithm = "";
            
            String pathInput = compressables[i];
            String pathCompressOutput = compressables[i] + "_compressed.bin";
            String pathDecompressOutput = compressables[i] + "_decompressed.txt";
            
            long[] compressTimes = new long[10];
            long[] decompressTimes = new long[10];
            
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
                    pathToCompressed = compressor.compress(algorithm, pathInput, 
                           pathCompressOutput);
                    compressFinish = System.nanoTime();
                    compressTime = compressFinish - compressStart;
                    compressTimes[k] = compressTime;   
                }
                
                // Purkaminen ja ajan mittaus
                for (int k = 0; k < 9; k++) {
                    decompressStart = System.nanoTime();
                    pathToDecompressed = decompressor.decompress(algorithm, pathToCompressed, 
                            pathDecompressOutput);
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

                // Talletetaan tiedot                
                String result = "Tiedosto: " + fileToCompress.getName() + "\n"
                        + "Algoritmi: " + algorithm + "\n"
                        + "Alkuperäisen tiedoston koko: " + inputFileSize + " bytes \n"
                        + "Pakatun tiedoston koko: " + outputFileSize + " bytes \n"
                        + "Pakkausteho: " + String.format("%.3f", effectiveness) + " % \n" 
                        + "Pakkausaika: " + String.format("%.3f", compressTimeMedian) + " ms \n"
                        + "Purkuaika: " + String.format("%.3f", decompressTimeMedian) + " ms \n";
                
                String resultMd = algorithm + " | " 
                        + inputFileSize + " | " 
                        + outputFileSize + " | " 
                        + String.format("%.3f", effectiveness) + " | " 
                        + String.format("%.3f", compressTimeMedian) + " | " 
                        + String.format("%.3f", decompressTimeMedian);
                
                results[i + j * 16] = result;
                resultsMd[i + 1 + j * 16] = resultMd;
            }
        }
        
        // taulukon otsikkorivit
        resultsMd[0] = "algoritmi | alkuperäinen koko (tavua) | pakattu koko (tavua) | "
                + "pakattu / alkuperäinen (%) | pakkausaika (ms) | purkuaika (ms)\n"
                + "---|---|---|---|---|---";
        
        String pathResults = null; 
        String pathResultsMd = null;
        
        try {
            pathResults = resultsToFile(results, "files/performance_results.txt");
            pathResultsMd = resultsToFile(resultsMd, "files/performance_resultsMD.txt");
        } catch (IOException ex) {
            System.out.println("Ei voitu tallettaa tiedostoon.");;
        }
        
        return pathResults + " ja " + pathResultsMd;
    }
    
    /**
     * Tallettaa testauksen tulokset tiedostoon.
     * @param results Suorituskykytestien tulokset merkkijonolistana
     * @param filePath Tulostiedoston osoite
     * @throws IOException 
     */
    private String resultsToFile(String[] results, String filePath) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(filePath));
        for (int i = 0; i < results.length; i++) {
            bw.write(results[i] + "\n");
        }
        bw.close();
        return filePath;
    }

    /**
     * Poistaa suorituskykytestien aikana luodut tiedostot.
     */
    public void removeTestOutputFiles() {
        for (int i = 0; i < compressables.length; i++) {
            try {
                Files.deleteIfExists(Paths.get(compressables[i] + "_compressed.bin"));
                Files.deleteIfExists(Paths.get(compressables[i] + "_decompressed.txt"));
            } catch (IOException ex) {
                System.out.println("Ongelma tiedostojen poistamisessa.");
            }
        }
    }
    
}




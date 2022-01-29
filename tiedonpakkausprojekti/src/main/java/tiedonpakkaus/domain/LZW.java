package tiedonpakkaus.domain;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


/**
 * Luokka LZW-algortimin mukaiselle tiedoston pakkaamiselle ja purkamiselle.
 */
public class LZW {

    /**
     * Pakkaa tiedostosta luetun tekstin kokonaislukuja sisältäväksi listaksi.
     * @param inputFile käyttäjän osoittama tiedosto
     * @return pakattu teksti kokonaislukuja sisältävänä listana
     */
    public List<Integer> encode(File inputFile) throws FileNotFoundException {
        
        Scanner fileScanner = null;
        fileScanner = new Scanner(inputFile);
        
        String textFromFile = "";
        while (fileScanner.hasNextLine()) {
            textFromFile += "\n" + fileScanner.nextLine();
        }      
        
        // Luodaan sanakirjan pohja: kirjainten Unicode-arvot
        int dictionarySize = 256;
        Map<String,Integer> dictionary = new HashMap<>();
        for (int i = 0; i < 256; i++) {
            dictionary.put("" + (char)i, i);
        }
 
        // tiedostossa olleen tekstin läpikäynti
        String s = "";
        List<Integer> codes = new ArrayList<>();
        for (char c : textFromFile.toCharArray()) {
            // Muodostetaan uusi merkkijono 
            // yhdistämällä tarkasteltavaan merkkijonoon seuraava merkki
            String sc = s + c;
            // Jos uusi yhdistetty merkkijono on jo sanakirjassa, 
            // otetaan se uudeksi tarkasteltavaksi merkkijonoksi
            if (dictionary.containsKey(sc)) {
                s = sc;             
            } else {
                // Lisätään tarkasteltava merkkijono koodilistalle 
                // ja uusi yhdistetty merkkijono sanakirjaan
                codes.add(dictionary.get(s));
                System.out.println("added: " + s + " =  "+ dictionary.get(s));
                dictionary.put(sc, dictionarySize++);
                s = "" + c;
            }
        }
 
        // Lisätään viimeinen merkkijono koodilistalle
        if (!s.equals("")) {
            codes.add(dictionary.get(s));
        }
        
        // Listan loppuun tieto listan suurimmasta arvosta
        codes.add(Collections.max(codes));

        return codes;
    }
    
    /**
     * Muuttaa listan bittijonoja sisältäväksi merkkijonoksi. 
     * @param codeList kokonaislukuja sisältävä koodilista 
     * @return pakattu teksti bittijonoesityksenä
     */
    public String codesToBits(List<Integer> codeList) {
        
        // Haetaan koodilistan lopusta tieto suurimmasta arvosta
        // ja asetetaan sen perusteella yksittäistä lukua kuvaavan bittijonon bittien määrä
        // ja poistetaan tämä luku listalta, jotta sinne jää vain pakattua tekstiä kuvaavat luvut
        
        int maxCode = codeList.get(codeList.size() - 1);
        codeList.remove(codeList.size() - 1);
        String maxCodeBinary = Integer.toBinaryString(maxCode);
        int binaryLength = maxCodeBinary.length();
        
        // bitit merkkijonona
        String bits = "";
        
        // lisätään bittien alkuun tieto yksittäistä lukua kuvaavan bittijonon pituudesta 
        // ja välilyönti erottamaan pituustiedon muista biteistä
        bits = bits + maxCodeBinary + " ";
        
        // muutetaan listalla olevat luvut samanpituisiksi bittijonoiksi 
        // ja yhdistetään merkkijonoksi
        String intToBits = "";
        String strFormat = "%" + binaryLength + "s";
        
        for (int code : codeList) {
            intToBits = String.format(strFormat, Integer.toBinaryString(code)).replaceAll(" ", "0");
            bits = bits + intToBits;            
        }
        
        return bits;
    }
        
}

package tiedonpakkaus.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Luokka LZW-algoritmin mukaiselle tiedon pakkaamiselle ja purkamiselle.
 */
public class LZW {
       
    /**
     * Muuttaa tekstin tavutaulukoksi.
     * @param text pakattava teksti
     * @return pakattu teksti tavutaulukkona
     */
    public byte[] encode(String text) {
        List<Integer> codes = createCodes(text);
        String bits = codesToBits(codes);
        byte[] bytes = bitsToBytes(bits);
        
        return bytes;
    }
    
    /**
     * Muuttaa tavutaulukon tekstiksi
     * @param bytes purettava tavutaulukko
     * @return purettu tavutaulukko tekstinä
     */
    public String decode(byte[] bytes) {
        
        String bits = bytesToBits(bytes);
        List<Integer> codes = bitsToCodes(bits);
        String text = codeListToText(codes);
        
        return text;
    }
    
    /**
     * Muuntaa tekstin kokonaislukuja sisältäväksi koodilistaksi.
     * @param text muunnettava merkkijono
     * @return pakattu teksti kokonaislukuja sisältävänä listana
     */
    private List<Integer> createCodes(String text) {
       
        // Luodaan sanakirjan pohja: kirjainten Unicode-arvot
        int dictionarySize = 383;
        Map<String,Integer> dictionary = new HashMap<>();
        for (int i = 0; i < dictionarySize; i++) {
            dictionary.put("" + (char)i, i);
        }
 
        // tekstin läpikäynti
        String s = "";
        List<Integer> codes = new ArrayList<>();
        for (char c : text.toCharArray()) {
            // Jos alkuperäistekstissä on merkkejä, jotka eivät kuulu pohjasanakirjaan,
            // korvataan ne tilde-merkillä (~)
            if (!dictionary.containsKey("" + c)) {
                System.out.println("Tekstissä on merkki ohjelmalle tuntemattomasta merkistöstä. "
                        + "Merkki '" + c + "' korvattu pakatessa merkillä '~'.");
                c = "~".charAt(0);
            }
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
                dictionary.put(sc, dictionarySize++);
                s = "" + c;
            }
        }
 
        // Lisätään viimeinen merkkijono koodilistalle
        if (!s.equals("")) {
            codes.add(dictionary.get(s));
        }
        
        // Talletetaan tieto listan suurimmasta arvosta listan loppuun
        int maxCode = Collections.max(codes);
        codes.add(maxCode);

        return codes;
    }
    
    /**
     * Muuttaa kokonaislukuja sisältävän koodilistan bittijonoja sisältäväksi merkkijonoksi. 
     * @param codes kokonaislukuja sisältävä koodilista 
     * @return pakattu teksti bittijonoesityksenä
     */
    private String codesToBits(List<Integer> codes) {
        
        // Haetaan listan lopusta tieto listan suurimmasta arvosta, poistetaan tämä tieto listalta, 
        // ja asetetaan sen perusteella yksittäistä lukua kuvaavan bittijonon bittien määrä
        int maxCode = codes.remove(codes.size() - 1);
        String maxCodeBinary = Integer.toBinaryString(maxCode);
        int binaryLength = maxCodeBinary.length();
        
        // Bitit merkkijonona
        String bits = "";
        
        // Merkkijonon ensimmäisiin 8 merkkiin tieto bittijonojen pituuksista bittteinä
        bits += String.format("%8s", Integer.toBinaryString(binaryLength)).replaceAll(" ", "0");
        
        // Muutetaan listalla olevat luvut samanpituisiksi bittijonoiksi 
        // ja yhdistetään merkkijonoksi
        String intToBits = "";
        String strFormat = "%" + binaryLength + "s";
        
        for (int code : codes) {
            intToBits = String.format(strFormat, Integer.toBinaryString(code)).replaceAll(" ", "0");
            bits = bits + intToBits;            
        }
        
        return bits;
    }
    
    /**
     * Muuttaa bittimerkkijonon tavutaulukoksi. 
     * @param bits bittijonot peräkkäin yhtenä merkkijonona
     * @return tavutaulukko, jossa bitit on jaettu tavuiksi tallentamista varten
     */
    private byte[] bitsToBytes(String bits) {
        
        // Haetaan merkkijonon alusta tieto alkuperäisten bittijonojen pituudesta
        // ja poistetaan nämä bitit merkkijonosta
        final int binaryLength = Integer.parseInt(bits.substring(0, 8), 2);
        bits = bits.substring(8);

        // Märitetään taulukon koko siten, että siinä on varmasti tila 
        // kaikille biteistä muodostettaville tavuille 
        // ja kaksi ylimääräistä paikkaa biteistä tallennettaville lisätiedoille
        byte[] bytes = new byte[bits.length() / 8 + 3];
        
        int len = 8;        
        String byteString = "";
        int b; 
        
        // taulukon kaksi ensimmäistä paikkaa lisätiedoille, 
        // alkuperäisen tiedoston sisältö alkaa indeksistä 2
        int i = 2; 
        int addedZerosInLastByte = 0;
               
        for (int start = 0; start < bits.length(); start += len) {
            if ((start + len) >= bits.length()) {
                byteString += bits.substring(start, bits.length());
                // jos viimeiseen indeksiin jää alle 8 bittiä, lisätään perään nollia 
                // ja tallennetaan tieto ylimääräisten nollien lukumäärästä
                if (byteString.length() < 8) {
                    addedZerosInLastByte = 8 - byteString.length();
                    for (int j = byteString.length(); j < 8; j++) {
                        byteString += "0";
                    }
                }
            } else {
                byteString += bits.substring(start, start + len);
            }
            
            b = Integer.parseInt(byteString, 2);

            bytes[i] = (byte) b;
                        
            byteString = "";
            i++;
        }

        // taulukon ensimmäiseen indeksiin viimeisen tavun ylimääräisten nollien lukumäärä
        bytes[0] = (byte) addedZerosInLastByte;

        // taulukon toiseen indeksiin alkuperäisten bittijonojen pituus
        bytes[1] = (byte) binaryLength;

        return bytes;
    }
    
    /**
     * Muuttaa tavutaulukon bittimerkkijonoksi
     * @param bytes tavutaulukko
     * @return tavutaulukko bittimerkkijonona
     */    
    private String bytesToBits(byte[] bytes) {

        // taulukon ensimmäisessä indeksissä tieto lopusta poistettavien nollien määrästä
        int zerosToRemove = bytes[0];
        
        int byteAsInt;
        String byteAsString;
        String bits = "";
        
        // käydään läpi tavutaulukko toisesta indeksistä alkaen 
        // pidetään mukana tieto myöhemmin numerokoodeiksi muunnettavien bittijonojen pituudesta
        for (int i = 1; i < bytes.length; i++) {
            byteAsInt = bytes[i];
            byteAsString = String.format("%8s", Integer.toBinaryString(byteAsInt & 0xFF))
                    .replace(" ", "0");
            bits = bits + byteAsString;
        }
        
        // poistetaan lopusta ylimääräiset nollat
        bits = bits.substring(0, bits.length() - zerosToRemove);

        return bits;
    }
    
    /**
     * Muuttaa bittimerkkijonon kokonaislukuja sisältäväksi koodilistaksi
     * @param bits bittimerkkijono
     * @return kokonaislukuja sisältävä koodilista
     */
    private List<Integer> bitsToCodes(String bits) {
        List<Integer> codes = new ArrayList<>();
        
        int len = Integer.parseInt(bits.substring(0, 8), 2);
        
        bits = bits.substring(8);
        String bitString = "";
        int code;
        
        for (int start = 0; start < bits.length(); start += len) {
            if ((start + len) >= bits.length()) {
                bitString += bits.substring(start, bits.length());
            } else {
                bitString += bits.substring(start, start + len);
            }
            code = Integer.parseInt(bitString, 2);
            codes.add(code);
            bitString = "";
        }
        
        return codes;
    }
    
    /**
     * Muuttaa koodilistan luvut tekstiksi
     * @param codes kokonaislukuja sisältävä koodilista
     * @return teksti, jonka tulisi vastata alkuperäisen pakatun tiedoston sisältöä
     */
    private String codeListToText(List<Integer> codes) {
        // Luodaan sanakirjan pohja: kirjainten Unicode-arvot
        int dictionarySize = 383;
        Map<Integer, String> dictionary = new HashMap<>();
        for (int i = 0; i < dictionarySize; i++) {
            dictionary.put(i, "" + (char)i);
        }
        
        int c = codes.remove(0);
        String s = "" + (char) c;
        
        String text = "" + s;
        for (int code : codes) {
            String entry;
            if (dictionary.containsKey(code)) {
                entry = dictionary.get(code);
            } else if (code == dictionarySize) {
                entry = s + s.charAt(0);
            } else {
                throw new IllegalArgumentException("Bad compression in code number " + code);
            }
            
            text += entry;
            
            dictionary.put(dictionarySize++, s + entry.charAt(0));
            
            s = entry;
        }
        
        return text;
    }
    
}

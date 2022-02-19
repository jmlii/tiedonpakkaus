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
     * Muuttaa tekstin LZW-algoritmilla tavutaulukoksi (pakkaa tekstin).
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
     * Muuttaa tavutaulukon LZW-algoritmilla tekstiksi (purkaa pakatun tiedon tekstiksi).
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
     * @param text muunnettava merkkijono (pakattava teksti)
     * @return pakattu teksti kokonaislukuja sisältävänä listana
     */
    private List<Integer> createCodes(String text) {
       
        // Luodaan sanakirjan pohja: kirjainten Unicode-arvot
        int dictionarySize = 256;
        Map<String,Integer> dictionary = new HashMap<>();
        for (int i = 0; i < dictionarySize; i++) {
            dictionary.put("" + (char)i, i);
        }
 
        // Tekstin läpikäynti
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
            // Muulloin lisätään tarkasteltava merkkijono koodilistalle 
            // ja uusi yhdistetty merkkijono sanakirjaan            
            } else {
                codes.add(dictionary.get(s));
                dictionary.put(sc, dictionarySize++);
                s = "" + c;
            }
        }
 
        // Lisätään viimeinen merkkijono koodilistalle
        codes.add(dictionary.get(s));

        return codes;
    }
    
    /**
     * Muuttaa kokonaislukuja sisältävän koodilistan bittijonoja sisältäväksi merkkijonoksi. 
     * @param codes kokonaislukuja sisältävä koodilista 
     * @return pakattu teksti bittijonoesityksenä
     */
    private String codesToBits(List<Integer> codes) {
                
        // Haetaan tieto listan suurimmasta arvosta, ja asetetaan sen bittijonoesityksen pituuden
        // perusteella yksittäistä lukua kuvaavan bittijonon bittien määrä
        int maxCode = Collections.max(codes);
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
        
        // Lasketaan, kuinka monta ylimääräistä nollaa bittimerkkijonon alkuun lisätään, 
        // jotta bittien määrä on jaollinen 8:lla eli yhden tavun bittien määrällä,
        // ja muodostetaan nollista merkkijono
        int extraZeros = 8 - bits.length() % 8;
        String zeros = new String(new char[extraZeros]).replace("\0", "0");
        
        // Yhdistetään ylimääräiset nollat bittimerkkijonon alkuun
        bits = zeros + bits;
      
        // Märitetään taulukon koko siten, että siinä on tila 
        // kaikille biteistä muodostettaville tavuille 
        // ja kaksi ylimääräistä paikkaa biteistä tallennettaville lisätiedoille
        byte[] bytes = new byte[bits.length() / 8 + 2];
        
        // Taulukon ensimmäiseen indeksiin bittijonon alkuun lisättyjen nollien lukumäärä
        bytes[0] = (byte) extraZeros;
        
        // Taulukon toiseen indeksiin alkuperäisten bittijonojen pituus
        bytes[1] = (byte) binaryLength;
        
        // Luodaan yhdistetyn merkkijonon tavuiksi käsittelyssä tarvittavat apumuuttujat
        int len = 8;    // Tavuun tulevien bittien määrä
        String byteString = "";    // Tavuun tulevista biteistä tehtävä merkkijono
        int b;    // Tavua kuvaava kokonaisluku
        int i = 2;    // Yhdistetty bittijono tallennetaan alkaen indeksistä 2
        
        for (int start = 0; start < bits.length(); start += len) {
            
            // Erotellaan yhdistetystä bittijonosta 8 bittiä kerrallaan
            byteString += bits.substring(start, start + len);

            // Muutetaan bittijono sitä vastaavaksi luvuksi
            b = Integer.parseInt(byteString, 2);

            // Muutetaan bittijonosta muutettu luku tavuksi ja tallennetaan taulukkoon
            bytes[i] = (byte) b;
            
            // Tyhjennetään tavumerkkijono seuraavaa kierrosta varten
            // ja kasvatetaan taulukon tallennuspaikkaa merkitsevää indeksiä
            byteString = "";
            i++;
        }

        return bytes;
    }
    
    /**
     * Muuttaa tavutaulukon bittimerkkijonoksi
     * @param bytes tavutaulukko
     * @return tavutaulukko bittimerkkijonona
     */    
    private String bytesToBits(byte[] bytes) {

     
        int byteAsInt;
        String byteAsString;
        String bits = "";
        
        // käydään läpi tavutaulukko kolmannesta indeksistä alkaen 
        for (int i = 2; i < bytes.length; i++) {
            byteAsInt = bytes[i];
            byteAsString = String.format("%8s", Integer.toBinaryString(byteAsInt & 0xFF))
                    .replace(" ", "0");
            bits = bits + byteAsString;
        }

        // taulukon ensimmäisessä indeksissä tieto alusta poistettavien nollien määrästä
        int zerosToRemove = bytes[0];
        
        // Taulukon toisessa indeksissä tieto myöhemmin numerokoodeiksi muunnettavien 
        // bittijonojen pituudesta
        int binaryLength = bytes[1];

        // poistetaan alusta ylimääräiset nollat
        bits = bits.substring(zerosToRemove);

        // alkuun bittijonojen pituus bittimerkkijonona
        bits = String.format("%8s", Integer.toBinaryString(binaryLength))
                .replaceAll(" ", "0") + bits;
        
        return bits;
    }
    
    /**
     * Muuttaa bittimerkkijonon kokonaislukuja sisältäväksi koodilistaksi
     * @param bits bittimerkkijono
     * @return kokonaislukuja sisältävä koodilista
     */
    private List<Integer> bitsToCodes(String bits) {
        
        List<Integer> codes = new ArrayList<>();
        
        // Haetaan bittimerkkijonon alusta tieto 
        // numerokoodeiksi muunnettavien bittijonojen pituudesta
        int len = Integer.parseInt(bits.substring(0, 8), 2);        
        
        String bitString = "";
        int code;
        
        // Jaetaan bittijono oikean pituisiin osiin,
        // muutetaan bitit kokonaisluvuiksi ja tallennetaan koodilistalle;
        // hypätään yli ensimmäiset bitit, joissa on tieto bittijonojen pituudesta
        for (int start = 8; start < bits.length(); start += len) {
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
        int dictionarySize = 256;
        Map<Integer, String> dictionary = new HashMap<>();
        for (int i = 0; i < dictionarySize; i++) {
            dictionary.put(i, "" + (char)i);
        }
        
        // Koodilistan läpikäynti
        // Muunnetaan koodilistan kokonaisluvut merkeiksi tai merkkijonoiksi,
        // ja yhdistetään ne tekstiksi.
        int c = codes.remove(0);
        String s = "" + (char) c;
        String text = "" + s;
        for (int code : codes) {
            String str;
            // Varmistetaan, että koodi on jo sanakirjassa.
            // Jos sanakirjasta löytyy luvulle merkki tai merkkijono, otetaan se talteen
            if (dictionary.containsKey(code)) {
                str = dictionary.get(code);
            // Jos koodi ei ole sanakirjassa, korvataan sen tilde-merkillä '~'
            } else if (code >= dictionarySize) {
                str = "~";
            // Varaudutaan siihen, että tiedostossa on joku koodiluku, jota ei saada muunnettua 
            // tekstiksi, ja ilmoitetaan käyttäjälle epäonnistumisesta
            } else {
                throw new IllegalArgumentException("Bad compression in code number " + code);
            }
            
            // Lisätään tekstiin edellä talteen otetut merkit
            text += str;
            
            // Yhdistetään edellisen kierroksen merkkeihin tämän kierroksen ensimmäinen merkki
            // ja lisätään uusi merkkijono sanakirjaan
            dictionary.put(dictionarySize, s + str.charAt(0));
            dictionarySize++;
             
            // Otetaan seuraavalle kierrokselle talteen edellä tarkastellut merkit
            s = str;
        }
        
        return text;
    }
    
}

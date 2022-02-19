
package tiedonpakkaus.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Luokka Huffmanin koodauksen mukaiselle tiedon pakkaamiselle ja purkamiselle.
 */
public class Huffman {
    
    /**
     * Muuttaa tekstin Huffmanin koodin algoritmilla tavutaulukoksi (pakkaa tekstin).
     * @param text pakattava teksti
     * @return pakattu teksti tavutaulukkona 
     */
    public byte[] encode(String text) {
        Map<Character, Integer> freq = countFrequencies(text);
        HuffmanNode huffmanTree = createTree(freq);
        Map<Character, String> charCodes = createCharCodes(huffmanTree, "", new HashMap<>());
        String treeBits = treeToBits(huffmanTree, "");
        String textBits = textToBits(text, charCodes);
        byte[] bytes = bitsToBytes(treeBits, textBits);
        
        return bytes;
    }
    
    /**
     * Muuttaa tavutaulukon Huffmanin koodin algoritmilla tekstiksi 
     * (purkaa pakatun tiedon tekstiksi).
     * @param bytes purettava tavutaulukko
     * @return purettu tavutaulukko tekstinä
     */
    public String decode(byte[] bytes) {
        // Metodia ei ole vielä toteutettu
        return "ei vielä toteutettu";
    }
      
    /**
     * Tallentaa pakattavassa tekstissä esiintyvien merkkien esiintymiskertojen lukumäärät
     * @param text pakattava teksti
     * @return hakemisto, jossa kunkin tekstissä esiintyvän merkin esiintymiskertojen lukumäärä
     */
    private Map<Character, Integer> countFrequencies(String text) {
              
        // Käydään läpi teksti ja siinä esiintyvien merkkien esiintymiskerrat
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : text.toCharArray()) {
            // Jos merkki ei kuulu perusmerkistöön (Unicode-merkistön arvot 0-255),
            // korvataan se tilde-merkillä (~)
            if (c > 255) {
                c = "~".charAt(0);
            }
            if (!freq.containsKey(c)) {
                freq.put(c, 1);
            } else {
                freq.replace(c, freq.get(c) + 1);
            }
        }
        return freq;
    }
    
    /**
     * Muodostaa Huffmanin koodin mukaisen puun minimikeko-rakenteeseen
     * @param frequencies hakemisto, jossa merkit ja niiden esiintymiskerrat
     * @return puun juuri (aloitussolmu)
     */
    private HuffmanNode createTree(Map<Character, Integer> frequencies) {

        // Luodaan Huffmanin koodin puu (minimikeko)
        PriorityQueue<HuffmanNode> tree = new PriorityQueue<>();
        
        // Käydään läpi merkit ja niiden esiintymiskerrat, ja 
        // muodostetaan niistä HuffmanNode-solmut, ja 
        // muodostetaan solmuista puu 
        for (char c : frequencies.keySet()) {
            tree.add(new HuffmanNode(c, frequencies.get(c)));
        }
        
        // Muodostetaan uusi solmu kahdesta pienimmän esiintymislukumäärän merkistä
        while (tree.size() > 1) {
            HuffmanNode left = tree.poll();
            HuffmanNode right = tree.poll();
            tree.add(new HuffmanNode(left.frequency + right.frequency, left, right));
        }
        
        // puun juuri (aloitussolmu)
        return tree.poll();
    }
    
    
    
        
    private Map<Character, String> createCharCodes(HuffmanNode tree, String code, 
            HashMap<Character, String> charCodes) {       
        
        // Kun saavutaan solmuun, jolla ei ole lapsia, tallennetaan hakemistoon 
        // solmun merkki ja siihen johtava polku;
        // polku on merkin koodi pakatussa tekstissä
        if (tree.left == null && tree.right == null) {
            
            // Jos puussa on vain yksi merkki, eli code-merkkijono on tyhjä kun saavutaan 
            // lapsettomaan solmuun, tallennetaan hakemistoon tämän merkin koodiksi "0"
            if (code.equals("")) {
                code += "0";
            }
            charCodes.put(tree.ch, code);

        // Käydään koko puu läpi rekursiivisesti:
        // vasemmalle siirryttäessä lisätään polkuun 0, oikealle siirryttäessä 1
        } else {
            createCharCodes(tree.left, code + "0", charCodes);
            createCharCodes(tree.right, code + "1", charCodes);
        }
        
        return charCodes;
    }
    
        
    
    private String textToBits(String text, Map<Character, String> charCodes) {
         
        String textBits = "";
        
        for (char c : text.toCharArray()) {
            
            // Jos tekstissä oleva merkki ei kuulu perusmerkistöön (Unicode-merkistön arvot 0-255),
            // korvataan se tilde-merkillä (~) ja ilmoitetaan käyttäjälle
            if (c > 255) {
                System.out.println("Tekstissä on merkki ohjelmalle tuntemattomasta merkistöstä. "
                        + "Merkki '" + c + "' korvattu pakatessa merkillä '~'.");
                c = "~".charAt(0);
            }
            String code = charCodes.get(c);
            textBits += code;
        }

        return textBits;
    }
    
    
    
    private byte[] bitsToBytes(String treeBits, String textBits) {
     
        // Haetaan tieto Huffmanin puun bittijonon pituudesta 
        int treeBitsLength = treeBits.length();
       
        // Yhdistetään puun bittijono ja tekstin bittijono
        String bits = treeBits + textBits;
        
        // Lasketaan, kuinka monta ylimääräistä nollaa bittimerkkijonon alkuun lisätään, 
        // jotta bittien määrä on jaollinen 8:lla eli yhden tavun bittien määrällä,
        // ja muodostetaan nollista merkkijono
        int extraZeros = 8 - bits.length() % 8;
        String zeros = new String(new char[extraZeros]).replace("\0", "0");

        // Yhdistetään ylimääräiset nollat bittimerkkijonon alkuun
        bits = zeros + bits;
                
        // Määritetään taulukon koko siten, että siinä on tila 
        // kaikille biteistä muodostettaville tavuille 
        // ja kaksi ylimääräistä paikkaa alun extrabittien lukumäärän tallentamiseen ja
        // puuta kuvaavan bittijonon pituuden tallentamiseen
        byte[] bytes = new byte[bits.length() / 8 + 2];
        
        // Taulukon ensimmäiseen indeksiin bittijonon alkuun lisättyjen nollien lukumäärä
        bytes[0] = (byte) extraZeros;
        
        // Taulukon toiseen tavuun tieto puun kuvauksen sisältävän bittijonon pituudesta
        bytes[1] = (byte) treeBitsLength;
        
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
     * Muodostaa Huffmanin puusta bittiesityksen, joka tallennetaan pakatun tekstin yhteyteen
     * @param tree Huffmanin puun juurisolmu 
     * @return puu bittimerkkijonona
     */
    private String treeToBits(HuffmanNode tree, String treeBits) {
                
        if  (tree.left == null && tree.right == null) {
            // Merkitään lapsetonta solmua bitillä 1,
            // ja liitetään sen perään solmun merkki 8-bittisessä muodossa            
            String charBits = String.format("%8s", Integer.toBinaryString(tree.ch))
                    .replaceAll(" ", "0");
            treeBits = treeBits + "1" + charBits;
            
        
        } else {
            // Merkitään vanhempi-solmua bitillä 0;
            treeBits += "0";
            
            // Käydään puu läpi rekursiivisesti
            treeBits = treeToBits(tree.left, treeBits);
                       
            treeBits = treeToBits(tree.right, treeBits);
        }
        
        return treeBits;
    }

}

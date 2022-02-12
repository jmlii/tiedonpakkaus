
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
     * 
     */
    public byte[] encode(String text) {
        Map<Character, Integer> freq = getFrequencies(text);
        HuffmanNode huffmanTree = createTree(freq);
        
        // Metodiin tullaan lisäämään pakkaamisen tarvittavat apumetodit vaiheittain
        // Tässä vaiheessa palauttaa tyhjän taulukon
        byte[] bytes = new byte[1];
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
    private Map<Character, Integer> getFrequencies(String text) {
       
        // Käydään läpi teksti ja siinä esiintyvien merkkien esiintymiskerrat
        Map<Character, Integer> freq = new HashMap<>();
        for (char c : text.toCharArray()) {
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
     * @return puun aloitussolmu
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

        // puun aloitussolmu
        return tree.poll();
    }
    
}

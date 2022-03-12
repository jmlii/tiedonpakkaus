package tiedonpakkaus.domain;

/**
 * Huffmanin puumallin solmu.
 * 
 */
public class HuffmanNode implements Comparable<HuffmanNode> {
    
    char ch = 0;
    int frequency;
    HuffmanNode left; 
    HuffmanNode right;
        
    /**
     * Yksittäisen merkin solmu, jolla ei ole lapsia.
     * @param ch merkki
     * @param frequency merkin esiintymiskertojen lukumäärä
     */
    public HuffmanNode(char ch, int frequency) {
        this.ch = ch;
        this.frequency = frequency;
    }
    
    /**
     * Kahden lapsisolmun vanhempi, johon ei liity omaa merkkiä.
     * @param frequency lasten esiintymiskertojen lukumäärien summa
     * @param left vasen lapsi
     * @param right oikea lapsi
     */
    public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }

    /**
     * Vertailee kahden solmun esiintymistiheyksiä
     * @param otherNode vertailun toinen solmu
     * @return positiivinen, jos tämän esiintymislkm on suurempi kuin toisen solmun
     */
    @Override
    public int compareTo(HuffmanNode otherNode) {
        return frequency - otherNode.frequency;
    }
}

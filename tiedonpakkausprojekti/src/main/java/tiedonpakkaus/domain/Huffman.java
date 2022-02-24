package tiedonpakkaus.domain;

import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Stack;

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
        StringBuilder treeBits = treeToBits(huffmanTree, new StringBuilder());
        Map<Character, String> charCodes = createCharCodes(huffmanTree, "", new HashMap<>());
        String textBits = textToBits(text, charCodes);
        byte[] bytes = bitsToBytes(treeBits.toString(), textBits);
        
        return bytes;
    }
    
    /**
     * Muuttaa tavutaulukon Huffmanin koodin algoritmilla tekstiksi 
     * (purkaa pakatun tiedon tekstiksi).
     * @param bytes purettava tavutaulukko
     * @return purettu tavutaulukko tekstinä
     */
    public String decode(byte[] bytes) {
        String bits = bytesToBits(bytes);
        int treeBitsLength = Integer.parseInt(bits.substring(0, 16), 2);
        String treeBits = bits.substring(16, 16 + treeBitsLength);   
        String textBits = bits.substring(16 + treeBitsLength);
        HuffmanNode tree = bitsToTree(treeBits);
        String text = bitsToText(textBits, tree);
        return text;
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
    
    /**
     * Muodostaa Huffmanin puusta bittiesityksen, joka tallennetaan pakatun tekstin yhteyteen
     * @param tree Huffmanin puun juurisolmu 
     * @return puu bittimerkkijonona
     */
    private StringBuilder treeToBits(HuffmanNode tree, StringBuilder treeBits) {
             
        if  (tree.left == null) {
            // Merkitään lapsetonta solmua bitillä 1,
            // ja liitetään sen perään solmun merkki 8-bittisessä muodossa            
            String charBits = String.format("%8s", Integer.toBinaryString(tree.ch))
                    .replaceAll(" ", "0");
            treeBits.append("1").append(charBits);

        } else {
            // Merkitään vanhempi-solmua bitillä 0;
            treeBits.append("0");
            
            // Käydään puu läpi rekursiivisesti
            treeBits = treeToBits(tree.left, treeBits);     
            treeBits = treeToBits(tree.right, treeBits);
        }
        
        return treeBits;
    }
    
    /**
     * Muodostaa hakemiston merkeille ja niistä käytettäville koodeille.
     * @param tree Huffmanin puun juuri (aloitussolmu)
     * @param code yksittäiseen merkkiä kuvaava koodimerkkijono 
     * @param charCodes hakemisto merkeille ja niistä käytettäville koodeille
     * @return hakemisto merkeille ja niistä käytettäville koodeille
     */    
    private Map<Character, String> createCharCodes(HuffmanNode tree, String code, 
            HashMap<Character, String> charCodes) {       
        
        // Kun saavutaan solmuun, jolla ei ole lapsia, tallennetaan hakemistoon 
        // solmun merkki ja siihen johtava polku;
        // polku on merkin koodi pakatussa tekstissä
        if (tree.left == null) {
            
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
    
    /**
     * Muuttaa pakattavan tekstin bittimerkkijonoksi.
     * @param text pakattava teksti
     * @param charCodes hakemisto merkeille ja niistä käytettäville koodeille
     * @return teksti koodattuna bittimerkkijonona
     */    
    private String textToBits(String text, Map<Character, String> charCodes) {
         
        StringBuilder textBits = new StringBuilder();
        
        for (char c : text.toCharArray()) {
            
            // Jos tekstissä oleva merkki ei kuulu perusmerkistöön (Unicode-merkistön arvot 0-255),
            // korvataan se tilde-merkillä (~) ja ilmoitetaan käyttäjälle
            if (c > 255) {
                System.out.println("Tekstissä on merkki ohjelmalle tuntemattomasta merkistöstä. "
                        + "Merkki '" + c + "' korvattu pakatessa merkillä '~'.");
                c = "~".charAt(0);
            }
            String code = charCodes.get(c);
            textBits.append(code);
        }

        return textBits.toString();
    }
    
    /**
     * Muuttaa puun ja tekstin bittimerkkijonot tavutaulukoksi
     * @param treeBits Huffmanin puun bittimerkkijonoesitys
     * @param textBits bittimerkkijonoksi koodattu alkuperäinen teksti
     * @return puu ja teksti tavutaulukoksi pakattuna
     */
    private byte[] bitsToBytes(String treeBits, String textBits) {
     
        StringBuilder bits = new StringBuilder();

        // Haetaan tieto Huffmanin puun bittijonon pituudesta 
        // ja ilmaistaan se kahden tavun (16 bittiä) pituisena bittijonona
        int treeBitsLength = treeBits.length();
        String treeBitsLengthBits = String.format("%16s", Integer.toBinaryString(treeBitsLength))
                .replaceAll(" ", "0");

        // Lisätään merkkijonoon puun bittijonon pituus bitteinä;
        bits.append(treeBitsLengthBits);
        
        // Lasketaan, kuinka monta ylimääräistä nollaa bittimerkkijonon alkuun lisätään, 
        // jotta bittien määrä on jaollinen 8:lla eli yhden tavun bittien määrällä,
        // ja muodostetaan nollista merkkijono.
        int extraZeros = 8 - (treeBits.length() + textBits.length()) % 8;

        String zeros = new String(new char[extraZeros]).replace("\0", "0");
        
        // Lisätään ylimääräiset nollat bittimerkkijonoon puun pituuden perään
        bits.append(zeros);

        // Yhdistetään merkkijonoon puun bittijono ja tekstin bittijono
        bits.append(treeBits).append(textBits);
                                        
        // Määritetään taulukon koko siten, että siinä on tila 
        // kaikille biteistä muodostettaville tavuille 
        // ja yksi ylimääräinen paikka alun extrabittien lukumäärän tallentamiseen 
        byte[] bytes = new byte[bits.length() / 8 + 1];
        
        // Taulukon ensimmäiseen indeksiin bittijonon alkuun lisättyjen nollien lukumäärä
        bytes[0] = (byte) extraZeros;
               
        // Luodaan yhdistetyn merkkijonon tavuiksi käsittelyssä tarvittavat apumuuttujat
        int len = 8;    // Tavuun tulevien bittien määrä
        String byteString = "";    // Tavuun tulevista biteistä tehtävä merkkijono
        int b;    // Tavua kuvaava kokonaisluku
        int i = 1;    // Yhdistetty bittijono tallennetaan alkaen indeksistä 1
                       
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
     * Muuttaa tavutaukukon bittimerkkijonoksi.
     * @param bytes tavutaulukko
     * @return tavutaulukko bittimerkkijonona
     */
    private String bytesToBits(byte[] bytes) {
        
        StringBuilder bits = new StringBuilder();

        // Merkkijonon alkuun puun bittijonon pituus bittimerkkijonona tavuista 1-2
        bits.append(String.format("%8s", Integer.toBinaryString(bytes[1] & 0xFF))
                .replaceAll(" ", "0"))
                .append(String.format("%8s", Integer.toBinaryString(bytes[2] & 0xFF))
                .replaceAll(" ", "0"));

        // Taulukon neljännen indeksin alussa on pakatessa lisätyt ylimääräiset nollat (max 7 kpl)
        // Lisätään ensin kaikki tavun bitit merkkijonoon
        bits.append(String.format("%8s", Integer.toBinaryString(bytes[3] & 0xFF))
                .replace(" ", "0"));
        
        // Haetaan taulukon ensimmäisestä indeksistä tieto alusta poistettavien nollien määrästä
        int zerosToRemove = bytes[0] & 0xFF;

        // Jos nollia on tavun mukaan yli 8, on purettavan tiedoston biteissä virhe
        if (zerosToRemove > 8) {
            throw new IllegalArgumentException("Virhe purettavassa tiedostossa, ei voida purkaa.");
        }
        // poistetaan ylimääräiset nollat bittijonojen pituuden ilmoittavien 16 bitin jälkeen
        bits.delete(16, 16 + zerosToRemove);

        // käydään läpi loput tavutaulukosta viidennestä tavusta alkaen 
        for (int i = 4; i < bytes.length; i++) {
            bits.append(String.format("%8s", Integer.toBinaryString(bytes[i] & 0xFF))
                    .replace(" ", "0"));
        }
        
        return bits.toString();
    }
   
    /**
     * Muuttaa puun bittijonosta puun mallin.
     * @param treeBits puun bitit
     * @return Huffmanin puun aloitussolmu
     */
    private HuffmanNode bitsToTree(String treeBits) {
        
        HuffmanNode rootNode;
        
        // Jos tekstissä oli vain yksi merkki, otetaan se suoraan talteen 
        // ja palautetaan solmu, johon liitetään tämä merkki;
        // merkin esiintymiskertojen lukumäärällä ei tässä kohti ole merkitystä
        if (treeBits.substring(0, 1).equals("1")) {
            char c = (char) Integer.parseInt(treeBits.substring(1, 9), 2);
            rootNode = new HuffmanNode(c, 0);
            return rootNode;
            
        } else {
            // Aloitussolmuksi solmu, jolla on lapsia
            rootNode = new HuffmanNode(0, null, null);
            
            // Luodaan pino solmujen luomisen avuksi; asetetaan aloitussolmu pinoon
            Stack<HuffmanNode> nodeStack = new Stack<>();
            nodeStack.push(rootNode);
            
            // Käydään läpi puun bittijono, ja muodostetaan siitä puun solmut
            int i = 1;
            while (i < treeBits.length()) {
                
                // Haetaan käsiteltäväksi pinosta viimeisin solmu
                HuffmanNode latest = nodeStack.peek();
                
                if (treeBits.substring(i, i + 1).equals("0")) {
                    // 0-bittien kohdalla muodostetaan solmu, jolle tulee lapsia
                    HuffmanNode parentNode = new HuffmanNode(0, null, null);
                    
                    // Solmulle lisätään aina ensin vasen lapsi
                    // Jos solmulla on jo vasen lapsi, lisätään uusi solmu oikeaksi lapseksi
                    if (latest.left == null) {
                        latest.left = parentNode;
                    } else {
                        latest.right = parentNode;
                        // Poistetaan pinosta solmu, jolle on merkitty molemmat lapset
                        nodeStack.pop();
                    }
                    
                    // lisätään luotu solmu pinoon
                    nodeStack.push(parentNode);
                    
                    // seuraavan bitin indeksi kasvaa yhdellä
                    i += 1;
                    
                } else {
                    // 1-bittien kohdalla muodostetaan yksittäistä merkkiä kuvaavat solmut
                    // edellisen solmun vasemmaksi ja oikeaksi lapseksi
                    
                    // lapsen merkin bitit ovat 1-bitistä seuraavat 8 bittiä
                    char ch = (char) Integer.parseInt(treeBits.substring(i + 1, i + 9), 2);
                    HuffmanNode charNode = new HuffmanNode(ch, 0);
                    
                    // Solmulle lisätään aina ensin vasen lapsi
                    // Jos solmulla on jo vasen lapsi, lisätään uusi solmu oikeaksi lapseksi
                    if (latest.left == null) {
                        latest.left = charNode;
                    } else {
                        latest.right = charNode;
                        // Poistetaan pinosta solmu, jolle on merkitty molemmat lapset
                        nodeStack.pop();
                    }
                    // Koska tällä solmulla ei ole lapsia, sitä ei lisätä pinoon
                    
                    // Seuraavan bitin indeksi kasvaa poimittujen bittien määrällä
                    i += 9;
                } 
            }
        }

        return rootNode;
    }
    
    /**
     * Muuntaa tekstibitit tekstiksi.
     * @param textBits tekstin bitit 
     * @param rootNode Huffmanin puun aloitussolmu
     * @return teksti, jonka tulisi vastata alkuperäisen pakatun tiedoston sisältöä
     */
    private String bitsToText(String textBits, HuffmanNode rootNode) {

        StringBuilder text = new StringBuilder();
        
        HuffmanNode node = rootNode;

        for (char c : textBits.toCharArray()) {
            
            // Jos juurisolmu on lapseton, lisätään sen merkki tekstiin 
            // ja palataan loopin alkuun
            if (node.left == null) {
                text.append(node.ch);
                continue;
            }
       
            // Jos juurella on lapsia, tarkistetaan onko tekstin bitti 0 vai 1
            // 0-bitti hakee solmun vasemman lapsen, 1-bitti oikean lapsen
            if (String.valueOf(c).equals("0")) {
                node = node.left;
            } else {
                node = node.right;
            } 
            
            // Jos lapsisolmulla ei ole omia lapsia,
            // lisätään sen merkki tekstiin ja palataan juurisolmuun
            if (node.left == null) {
                text.append(node.ch);
                node = rootNode;
            }    
        }
        
        return text.toString();
    }
    
}
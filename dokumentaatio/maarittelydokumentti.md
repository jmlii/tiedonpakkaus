# Määrittelydokumentti
Projektissa tutkitaan tiedon tiivistämistä. Työssä keskitytään tekstimuotoiseen tietoon, ja siten häviöttömiin pakkausmenetelmiin, koska symbolinen tieto, kuten teksti, taulukot tai ohjelmakoodi, ei kestä sisällön muuttumista, mitä häviöllisessä pakkaamisessa tapahtuisi. 

Harjoitustyössä toteutetaan kaksi pakkausalgoritmia, Huffman ja Lempel Ziv, sekä vertaillaan niiden suoriutumista keskenään. Toteutettavat ohjelmat pystyvät sekä pakkaamaan että palauttamaan pakatun tekstin alkuperäiseen muotoonsa. Tavoitteena on, että pakattuna tekstin koko olisi noin 40-60 % alkuperäisestä koosta. 

## Toteutettavat algoritmit ja tietorakenteet
Toteutettavat algoritmit ovat Huffman ja Lempel Ziv, jotka ovat tunnettuja ja suosittuja häviöttömiä pakkausmenetelmiä. Lempel Zivin algoritmista käytetään Lempel–Ziv–Welch-toteutusta.

Huffmanin koodaus käyttää tietorakenteina binääripuuta ja kekoa. Huffmanin koodauksen aikavaativuus on O(n log n). 

Lempelin-Ziv-Welchin algoritmissa luodaan hakemisto, joka voi olla rakenteeltaan esimerkiksi hajautustaulu tai hakupuu. Se toimii ajassa O(n). 

## Syötteet
Ohjelmalle annetaan syötteeksi jokin tiedosto, jonka sisältö luetaan tekstiksi. Lempel-Ziv-Welchin algoritmissa tekstistä muodostetaan lista, jossa teksti on pakattu kokonaisluvuiksi. Huffmanin algoritmissa tekstistä muodostetaan puurakenne ja sen avulla tekstin merkeistä muodostetaan pakattu bittimerkkijono. Lopullisena tuotoksena kummassakin on tiedosto, jossa pakatut rakenteet on tallennettu bittimuotoista merkkijonoa apuna käyttäen tavuiksi. Huffmanin koodauksessa pakattu tiedosto sisältää myös puurakenteen. Pakattu tiedosto voidaan myös palauttaa alkuperäiseen muotoonsa tekstitiedostoksi. 

## Työn kielet
Ohjelma toteutetaan java-ohjelmointikielellä. Dokumentaation kieli on suomi.  

## Muuta
- opinto-ohjelma: tietojenkäsittelytieteen kandidaatti (TKT)
- kielet: hallitsen parhaiten javan, tarvittaessa pystyn vertaisarvioimaan myös pythonilla tehtyjä projekteja

## Lähteet
- https://fi.wikipedia.org/wiki/Tiedonpakkaus
- https://en.wikipedia.org/wiki/LZ77_and_LZ78
- https://en.wikipedia.org/wiki/Huffman_coding

Kaikki haettu 22.1.2022.

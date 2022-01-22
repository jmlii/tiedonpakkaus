# Määrittelydokumentti
Projektissa tutkitaan tiedon tiivistämistä. Työssä keskitytään tekstimuotoiseen tietoon, ja siten häviöttömiin pakkausmenetelmiin, koska symbolinen tieto, kuten teksti, taulukot tai ohjelmakoodi, ei kestä sisällön muuttumista, mitä häviöllisessä pakkaamisessa tapahtuisi. 

Harjoitustyössä toteutetaan kaksi pakkausalgoritmia, Huffman ja Lempel Ziv, sekä vertaillaan niiden suoriutumista keskenään. Toteutettavat ohjelmat pystyvät sekä pakkaamaan että palauttamaan pakatun tekstin alkuperäiseen muotoonsa. Tavoitteena on, että pakattuna tekstin koko olisi noin 40-60 % alkuperäisestä koosta. 

## Toteutettavat algoritmit ja tietorakenteet
Toteutettavat algoritmit ovat Huffman ja Lempel Ziv, jotka ovat tunnettuja ja suosittuja häviöttömiä pakkausmenetelmiä. 

Huffmanin koodaus käyttää tietorakenteina binääripuuta ja kekoa. Huffmanin koodauksen aikavaativuus on O(n log n). 

Lempelin ja Zivin algoritmissa luodaan hakemisto, joka voi olla rakenteeltaan esimerkiksi hajautustaulu tai hakupuu. Se toimii ajassa O(n). 

## Syötteet
Ohjelmalle annetaan syötteeksi jokin teksti. Tuotoksena on lista, jossa teksti on pakattu kokonaisluvuiksi. Pakattu teksti voidaan myös palauttaa alkuperäiseen muotoonsa. 

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

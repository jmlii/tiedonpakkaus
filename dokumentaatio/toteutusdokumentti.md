# Toteutusdokumentti

Ohjelmalla voi pakata tekstitiedostoja Lempel-Ziv-Welch-algoritmilla tai Huffmanin koodaus -algoritmilla sekä purkaa näillä menetelmillä pakattuja tiedostoja takaisin luettavaksi tekstiksi. Pakkaus tapahtuu häviöttömästi, mikä on tärkeää tekstin kohdalla. 

Rajoituksena häviöttömyyteen Lempel-Ziv-Welchin kohdalla kuitenkin on, että algoritmi ei tunnista kaikkia eri kirjaimistoissa käytettyjä merkkejä, vaan ainoastaan länsimaissa yleisimmät merkit (Unicode-merkistöistä kontrollikoodit, Basic Latin ja Latin-1 Supplement), ja se korvaa tuntemattomat merkit symbolilla "~". 

## Ohjelman yleisrakenne 
Ohjelma on jaettu kolmeen pakkaukseen: Tiedonpakkaus, Tiedonpakkaus.Ui ja Tiedonpakkaus.domain. Tiedonpakkaus-luokassa on ohjelman käynnistävä Main-luokka. Ui-pakkaus sisältää käyttöliittymän. Varsinainen tiedon pakkaamisen ja purkamisen toiminnallisuus on koottu domain-pakkaukseen. Pakkauksessa on omat luokkansa tiedostojen lukemiselle ja tallentamiselle, ja tiedon pakkaamisen ja purkamisen tekevät metodit ovat kummallekin pakkausalgoritmille omissa luokissaan.

Tiedon pakkaamisen algoritmeista Huffmanin koodaus käyttää tietorakenteina binääripuuta ja kekoa. Lempel-Ziv-Welchin algoritmissa luodaan hakemisto, joka on tässä ohjelmassa toteutettu hajautustauluna. Lisäksi ohjelma käyttää tekstiä pakattavaan muotoon muokatessaan Javan listaa (ArrayList), merkkijonoa (String) ja tavutaulukkoa (byte[]). 

## Saavutetut aika- ja tilavaativuudet 
Kattavaa suorituskykytestausta ei vielä ole suoritettu.

Algoritmien pseudokoodeista Huffmanin koodauksen aikavaativuus on O(n log n), ja Lempel-Ziv-Welch toimii ajassa O(n). 

## Suorituskyky- ja O-analyysivertailu 
Vertailu tehdään vasta, kun molemmat pakkausalgortimit ovat valmiit ja niiden suorituskykyä on kattavasti testattu.

## Työn mahdolliset puutteet ja parannusehdotukset 
Lempel-Ziv-Welchin algoritmissa luodaan ensin sanakirjan pohja määrätylle joukolle Unicode-merkkejä, ja algoritmi ei tunnista muita pakattavassa tekstissä olevia merkkejä oikein. Koska ohjelman halutaan toimivan tehokkaasti ja pakkaavan tiedon mahdollisimman pieneen tilaan, eli sisältämään mahdollisimman vähän bittejä, ei sanakirjaan kannata sisällyttää tarpeettoman suurta määrää merkkejä. Tämä ei johdu siitä, että sanakirja tallennettaisiin pakatun tiedon osana ja se kannattaisi siksi pitää pienenä, vaan siitä että sanakirjan alkukoko määrittää sen, mistä kokonaisluvusta alkaen aletaan viitata tekstissä toistuviin merkkijonoihin, ja mitä pienempänä luvut pysyvät, sitä pienempiä ovat myös niitä kuvaavat bittijonot. Tämä sanakirjan aiheuttama rajoite rajoittaa algoritmin toimintaa, jos alkuperäisteksti sisältää näitä tunnistamattomia merkkejä, sillä ohjelma ei kykene myöskään purkamaan niitä alkuperäisiä merkkejä vastaaviksi. Tältä osin ohjelmaa voisi parantaa esim. niin, että käyttäjä voisi pakkausta käynnistäessään valita, mitkä Unicode-merkistöt hän haluaa ottaa mukaan. Tämä kuitenkin vaatisi käyttäjää perehtymään etukäteen niin merkistöihin kuin myös pakattavien tiedostojen sisältöön, jotta hän varmasti sisällyttäisi ohjelmaan kaikki tarvittavat merkistöt. Toinen vaihtoehto olisi luoda sanakirjan pohja käymällä teksti ensin läpi kertaalleen lisäten kaikki siinä olevat yksittäiset merkit sanakirjaan, ja sitten vasta ryhtyä luomaan koodilistaa tämän sanakirjan pohjalta käymällä teksti uudelleen läpi. 

## Lähteet 
- Tiedonpakkaus (n.d.). Wikipedia. Haettu 22.1.2022: https://fi.wikipedia.org/wiki/Tiedonpakkaus
- Lempel–Ziv–Welch (n.d). Wikipedia. Haettu 12.2.2022: https://en.wikipedia.org/wiki/Lempel-Ziv-Welch
- Huffman coding (n.d.). Wikipedia. Haettu 22.1.2022: https://en.wikipedia.org/wiki/Huffman_coding

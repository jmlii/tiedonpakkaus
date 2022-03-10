# Toteutusdokumentti

Ohjelmalla voi pakata tekstitiedostoja Lempel-Ziv-Welch-algoritmilla tai Huffmanin koodaus -algoritmilla sekä purkaa näillä menetelmillä pakattuja tiedostoja takaisin luettavaksi tekstiksi. Pakkaus tapahtuu häviöttömästi, mikä on tärkeää tekstin kohdalla. 

Rajoituksena häviöttömyyteen kuitenkin on, että algoritmit eivät tunnista kaikkia eri kirjaimistoissa käytettyjä merkkejä, vaan ainoastaan länsimaissa yleisimmät merkit (Unicode-merkistöistä kontrollikoodit, Basic Latin ja Latin-1 Supplement, eli Unicode-arvoon 255 asti), ja se korvaa tuntemattomat merkit symbolilla "~". 

## Ohjelman yleisrakenne ja toiminta
Ohjelma on jaettu neljään pakkaukseen: Tiedonpakkaus, Tiedonpakkaus.domain, Tiedonpakkaus.ui ja Tiedonpakkaus.util. Tiedonpakkaus-luokassa on ohjelman käynnistävä Main-luokka. Ui-pakkaus sisältää käyttöliittymän. Util-pakkauksessa on ohjelman suorituskykyä testaava luokka. Varsinainen tiedon pakkaamisen ja purkamisen toiminnallisuus on koottu domain-pakkaukseen. Pakkauksessa on omat luokkansa tiedostojen lukemiselle ja tallentamiselle, ja tiedon pakkaamisen ja purkamisen tekevät metodit ovat kummallekin pakkausalgoritmille omissa luokissaan.

Pakkaamisen suorittaminen aloitetaan Compressor-luokassa, joka lukee pakattavan tiedoston sisällön merkkijonoksi ja välittää sen parametrina valitun algoritmin mukaisen luokan encode-metodille. Pakkausalgoritmi palauttaa pakatun tekstin Compressor-luokalle tavutaulukkona, jonka tavut luokka seuraavaksi tallettaa sille osoitettuun tiedostoon. 

Pakatun tiedoston purkaminen takaisin alkuperäiseksi tekstiksi tapahtuu vastaavalla tavalla, mutta nyt tiedostosta luetut tavut talletetaan tavutaulukkoon, joka välitetään valitun algoritmin decode-metodille. Pakkausalgoritmi palauttaa tavut merkkijonomuotoiseksi tekstiksi purettuna takaisin Decompressor-luokalle, joka tallettaa merkkijonon osoitettuun tiedostoon.

Kummallakin pakkausalgoritmilla on julkiset encode- ja decode-metodit, ja varsinainen pakkaaminen ja purkaminen tehdään luokan sisällä yksityisillä metodeilla. 

### Lempel-Ziv-Welch
Lempel-Ziv-Welchissä muodostetaan ensin sanakirjan pohja yksittäisistä merkeistä Unicode-arvoon 255 asti. Pakattava teksti käydään läpi merkki kerrallaan, yhdistämällä uusi merkki edelliseen tarkasteltuun merkkiin tai merkkijonoon, ja jos uusi yhdistelmä ei ole sanakirjassa, sille annetaan koodiksi seuraava vapaa kokonaisluku, joka lisätään sanakirjaan, ja tarkastellun merkkijonon (ilman viimeistä merkkiä) sanakirjassa jo oleva koodi lisätään koodilistalle, joka muodostaa pakatun tekstin. Lopuksi nämä kokonaislukukoodit muutetaan samanpituisiksi bittijonoiksi ja yhdistetään toisiinsa. Bittijonon alkuun lisätään vakiopituisena bittijonona tieto yksittäistä koodia kuvaavan bittijonon pituudesta. Lisäksi bitteihin yhdistetään ylimääräisiä nollia, joiden avulla kokonaisesitys voidaan jakaa tasan tavuiksi, ja alkuun lisätään yksi tavu, jossa kerrotaan näiden ylimääräisten nollien lukumäärä. Näitä ylimääräisiä tietoja tarvitaan pakatun tiedoston purkamisessa, jotta se saadaan jaettua tavuista takaisin oikean pituisiin bittijonoihin, jotka voidaan muuntaa jälleen kokonaislukulistaksi. Kokonaisluvut muutetaan merkkijonoiksi 255-merkkisen pohjasanakirjan avulla vastaavalla tavalla kuin pakatessa merkkijonot muutettiin luvuiksi. 

LZW:n pakkaamisen teho perustuu tekstissä toistuviin merkkijonoihin, kun pitkiäkin merkkijonoja voidaan kuvata kokonaislukukoodeilla, joiden bittiesitys on pienempi kuin yksittäisten merkkien bittiesitysten tallettaminen peräkkäin, kuten tavallisessa tekstin tallennuksessa tapahtuu.  

### Huffmanin koodaus
Huffmanin koodauksessa pakattava teksti käydään ensin kertaalleen läpi ja lasketaan kaikkien yksittäisten merkkien esiintymiskertojen lukumäärä. Näiden perusteella muodostetaan Huffmanin puumalli. Koodissa on oma luokka Huffmanin solmulle, joita on kahdenlaisia: lapsisolmuja, joissa on tieto yksittäisestä merkistä ja sen esiintymiskerroista, ja vanhempisolmuja, joilla kullakin on kaksi lasta ja esiintymislukumääränä näiden lasten esiintymiskertojen summa, mutta ei omaa merkkiä. Jokaiselle tekstissä esiintyvälle merkille muodostetaan ensin tällainen lapsisolmu, ja ne lisätään minimikekoon. Keon solmuja käydään läpi, poistaen keosta ja yhdistäen aina kaksi pienimmän esiintymislukumäärän solmua näiden solmujen vanhempisolmuksi, joka lisätään kekoon, kunnes keossa on jäljellä vain puun juurisolmu. Huffmanin puun juuressa on suurin esiintymiskertojen summa, ja yksittäisistä merkeistä lähimpänä juurta ovat tekstissä useimmin esiintyneet merkit, kun taas harvimmin esiintyneet ovat kauimpana juuresta. 

Huffmanin koodauksessa puumalli on tallennettava pakatun tekstin yhteyteen, jotta pakattu esitys voidaan purkaa. Koska tieto yritetään saada pieneen tilaan, niin puu pakataan muodostamalla siitä bittimerkkijono, jossa käydään puu läpi rekursiivisesti juuresta alkaen, ja lisätään bittijonoon 0 kuvaamaan venhempisolmuja, ja lapsisolmuun tuloa merkitään bitillä 1, jonka jälkeen seuraavat 8 bittiä ovat tämän solmun merkin Unicode-arvo bittimuodossa. 

Teksti puolestaan pakataan puuta apuna käyttäen. Puun avulla muodostetaan ensin hakemisto merkeille sekä niihin johtaville 0- ja 1-merkeistä koostuville poluille, jotka pakatussa tekstissä kuvaavat merkkiä. Puu käydään jälleen rekursiivisesti läpi, ja vasemmalle kulkiessa lisätään polkuun 0 ja oikealle kulkiessa 1. Lehteen eli lapsettomaan solmuun saavuttaessa hakemistoon tallennetaan tämän solmun merkki ja siihen johtanut polku. 

Seuraavaksi pakattava teksti käydään läpi hakemistoa apuna käyttäen, ja talletetaan bittimerkkijonoksi, jossa tekstin merkit on korvattu niitä kuvaavilla bittipoluilla. 

Lopuksi puun ja tekstin bittijonot yhdistetään, ja niihin yhdistetään ylimääräisiä 0-bittejä, joiden avulla merkkijono saadaan tasan tavuihin jaettavan pituiseksi. Merkkijonon alkuun talletetaan ylimääräisten nollien lukumäärä ja tieto puun bittijonon pituudesta. 

Pakattua tiedostoa purkaessa puun ja tekstin bittijonot erotetaan toisistaan. Puun bitit puretaan takaisin puuesitykseksi, ja tekstin bitit muunnetaan takaisin merkeiksi kulkemalla puussa joko vasemmalle tai oikealle bitin arvon mukaan, kunnes saavutaan lapsettomaan solmuun, josta saadaan tekstiin lisättävä merkki. 

Huffmanin koodauksen pakkausteho perustuu siihen, että tekstissä esiintyviä merkkejä voidaan kuvata tavua lyhyemmillä bittijonoilla, ja kun useimmin esiintyviä merkkejä kuvataan kaikkein lyhyimmillä bittijonoilla, saadaan säästettyä tilaa. Puumallin tallentaminen pakatun tekstin yhteyteen vie kuitenkin aina oman tilansa, kasvattaen pakatun tiedoston kokoa.

### Tietorakenteet
Tiedon pakkaamisen algoritmeista Huffmanin koodauksen keskeinen tietorakenne on Huffmanin solmuista koostuva binääripuu, joka muodostetaan käyttäen apuna Javan PriorityQueuena toteutettua minimikekoa. Lisäksi Huffmanin algoritmi on toteutettu käyttäen Javan hajautustaulua (HashMap) merkkien esiintymislukumäärien tallettamiseen ja pinoa (Stack) apuna puun palauttamiseen bittijonosta takaisin puuksi. 

Lempel-Ziv-Welchin algoritmin hakemisto on tässä ohjelmassa toteutettu Javan hajautustauluna ja koodilista Javan listana (ArrayList)

Lisäksi ohjelma käyttää tekstiä pakattavaan muotoon muokatessaan ja tavuista takaisin tekstiksi palauttaessaan merkkijonoa (String ja StringBuilder) ja tavutaulukkoa (byte[]). Pakattavat, tekstimuotoiset tiedostot luetaan Javan BufferedReaderilla, joka on kääritty FileReaderin ympärille, ja tiedostot tallennetaan tavuina Javan BufferedOutputStreamilla, joka on puolestaan kääritty FileOutputStreamin ympärille. Purettavat, tavumuotoiset tiedostot luetaan Javan Files-luokan readaAllBytes-metodilla, ja purettu merkkijonomuotoinen teksti tallennetaan tiedostoon Javan BufferedWriterilla, joka on kääritty FileWriterin ympärille. 

## Saavutetut aikavaativuudet 
Algoritmien pseudokoodeista Huffmanin koodauksen aikavaativuus on O(n log n), ja Lempel-Ziv-Welch toimii ajassa O(n). 

LZW:ssä teksti käydään niin pakatessa kuin purkaessa läpi vain kertaalleen, muuttaen merkkejä koodeiksi ja lisäten niitä sanakirjaan sitä mukaa kun uusia merkkiyhdistelmiä tulee tekstissä vastaan. Seuraavaksi koodilista käydään läpi ja muutetaan bittijonoksi, joka puolestaan käydään läpi ja talletetaan tavutaulukoksi, joka edelleen käydään läpi ja tallennetaan tiedostoon. Purkamisessa käydään läpi samat askeleet mutta vastakkaiseen suuntaan. Kaikkien toimien aikavaativuus on O(n). 

Huffmanin koodauksessa puu muodostetaan minimikeon avulla. Alkioiden lisääminen ja pienimpien alkioiden poistaminen keosta on aikavaativuudeltaan O(log n) ja keon järjestäminen O(n log n). 

## Suorituskyky- ja O-analyysivertailu 
Täydentyy

## Työn mahdolliset puutteet ja parannusehdotukset 
Lempel-Ziv-Welchin algoritmissa luodaan ensin sanakirjan pohja määrätylle joukolle Unicode-merkkejä, ja algoritmi ei tunnista muita pakattavassa tekstissä olevia merkkejä oikein. Koska ohjelman halutaan toimivan tehokkaasti ja pakkaavan tiedon mahdollisimman pieneen tilaan, eli sisältämään mahdollisimman vähän bittejä, ei sanakirjaan kannata sisällyttää tarpeettoman suurta määrää merkkejä. Tämä ei johdu siitä, että sanakirja tallennettaisiin pakatun tiedon osana ja se kannattaisi siksi pitää pienenä, vaan siitä että sanakirjan alkukoko määrittää sen, mistä kokonaisluvusta alkaen aletaan viitata tekstissä toistuviin merkkijonoihin, ja mitä pienempänä luvut pysyvät, sitä pienempiä ovat myös niitä kuvaavat bittijonot. Tämä sanakirjan aiheuttama rajoite rajoittaa algoritmin toimintaa, jos alkuperäisteksti sisältää näitä tunnistamattomia merkkejä, sillä ohjelma ei kykene myöskään purkamaan niitä alkuperäisiä merkkejä vastaaviksi. 

Vastaava rajoite on Huffmanin koodauksessa, sillä tekstin mukana tallennettava puumalli sisältää kertaalleen kaikkien tekstissä esiintyvien merkkien unicode-arvot määrämittaisena bittijonona, eikä puun bittijonoesitystä kannata tarpeettomasti pidentää. 

Tältä osin ohjelmaa voisi parantaa esim. niin, että käyttäjä voisi pakkausta käynnistäessään valita, mitkä Unicode-merkistöt hän haluaa ottaa mukaan. Tämä kuitenkin vaatisi käyttäjää perehtymään etukäteen niin merkistöihin kuin myös pakattavien tiedostojen sisältöön, jotta hän varmasti sisällyttäisi ohjelmaan kaikki tarvittavat merkistöt. Lempel-Ziv-Welchin kohdalla voisi vaihtoehtoisesti luoda sanakirjan pohjan käymällä teksti ensin läpi kertaalleen lisäten kaikki siinä olevat yksittäiset merkit sanakirjaan, ja sitten vasta ryhtyä luomaan koodilistaa tämän sanakirjan pohjalta käymällä teksti uudelleen läpi. Tällöin pitäisi kuitenkin tallettaa pakatun tekstin lisäksi myös yksittäiset merkit sisältävä sanakirja, joka jälleen vie tilaa, mutta saattaisi silti johtaa pienempään lopputulokseen esimerkiksi jos pakattava teksti ei sisällä isoa joukkoa eri merkkejä. 

Ohjelman nopeutta puolestaan voisi lisätä tehokkaammilla rakenteilla. Esimerkiksi kummassakin algoritmissa käytetään HashMapia jonkin tiedon tallettamiseen ja hakemiseen, eikä se ole siihen tehokkain ratkaisu. 


## Lähteet 
- Tiedonpakkaus (n.d.). Wikipedia. Haettu 22.1.2022: https://fi.wikipedia.org/wiki/Tiedonpakkaus
- Lempel–Ziv–Welch (n.d). Wikipedia. Haettu 12.2.2022: https://en.wikipedia.org/wiki/Lempel-Ziv-Welch
- Huffman coding (n.d.). Wikipedia. Haettu 22.1.2022: https://en.wikipedia.org/wiki/Huffman_coding

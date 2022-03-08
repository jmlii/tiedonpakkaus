# Testausdokumentti

Ohjelmaa on testattu sekä yksikkötestein että suorituskyvyn osalta.

## Yksikkötestit
Yksikkötestit on tehty JUnit-testeinä luokka- ja metodikohtaisesti. Testauskattavuutta seurattiin ohjelmaa tehdessä JaCoCon kattavuusraportin avulla. 

Yksikkötestit tehtiin vain domain-pakkauksessa oleville pakkaamisen ja purkamisen tekeville luokille. Main-luokka, käyttöliittymä ja suorituskykytestaus jätettiin yksikkötestien ulkopuolelle.

Yksikkötesteissä luodaan ensin testejä varten tarvittavat testitiedostot tai muut rakenteet, joita testeissä käsitellään, ja testien lopuksi luodut tiedostot poistetaan. 

Pakattavan tiedoston lukevan ja pakkauksen tuloksen tiedostoksi kirjoittavan Compressor-luokan sekä purettavan tiedoston lukevan ja purkamisen tuloksen tekstiksi kirjoittavan Decompressor-luokan testeissä keskeisimmät testattavat seikat ovat, että kumpaakin pakkausalgoritmia käytettäessä sekä pakkaamisen että purkamisen tuloksena on tiedosto, jos käsiteltäväksi annetussa tiedostossa on sisältöä joka pystytään käsittelemään, ja että jos käsiteltäväksi annettu tiedosto on tyhjä ei pakkaamista tai purkamista aleta suorittaa. Lisäksi testataan, että pakkausta tai purkamista ei aleta suorittaa, jos pakkausalgoritmia ei tunnisteta, vaikka käyttöliittymä estääkin tällaisen tilanteen. Lisäksi Decompressor-luokan testeissä tarkistetaan, että onnistuneen purkamisen tulos vastaa alkuperäistä tekstiä. 

Algoritmien testeissä keskeiset testattavat asiat ovat, että pakkauksen tulos on oikean pituinen, että tuntemattomat erikoismerkit muutetaan pakatessa tilde-merkiksi (~), ja että purkamisen tulos vastaa alkuperäistä tekstiä. 

Valmiin ohjelman yksikkötestien testikattavuus:
![testikattavuusraportti](/dokumentaatio/kuvat/kuva_testauskattavuus_valmis.png)


## Suorituskyky

Ohjelman tekstitiedostojen pakkaamisen ja purkamisen suorituskykyä on testattu eri kokoisilla tekstitiedostoilla. Suorituskykytesteille on tehty oma luokka PerformanceTester util-pakkaukseen, ja ne voi suorittaa ohjelman käyttöliittymästä. Suorituskykytestausta varten projektin juuressa olevassa files-kansiossa on 16 eri kokoista tiedostoa, kooltaan 64 tavua - 2,1 megatavua, joihin on tuotu sisällöksi Lorem Ipsum -tekstiä sivustolta https://lipsum.com/. 

Suorituskykytestaus pakkaa ja purkaa tiedostot ja tallettaa testien tulokset kahteen tiedostoon, joista toisessa tulokset listataan sanallisesti ja toisessa ne ovat muotoiltuna helposti md-tiedoston osaksi kopioitavaksi. Tuloksissa ilmoitetaan alkuperäisen ja pakatun tiedoston koot, pakkausteho eli pakatun tiedoston koko suhteessa alkuperäiseen prosentteina sekä pakkaamiseen ja purkamiseen kuluneet ajat millisekunteina. Koska kuluneissa ajoissa esiintyy vaihtelua suorituskertojen välillä, suorittaa ohjelma testit kummallakin algoritmilla 9 kertaa, ja tuloksista talletetaan vain niiden mediaani. 

Alla on listattu suorituskykytestauksen tulokset yhdeltä testauskerralta ja algoritmeja vertailtu niiden perusteella. 

### Lempel-Ziv-Welch

alkuperäinen koko (tavua) | pakattu koko (tavua) | pakattu / alkuperäinen (%) | pakkausaika (ms) | purkuaika (ms)
---|---|---|---|---
64 | 66 | 103.125 | 0.387 | 0.370
128 | 116 | 90.625 | 0.389 | 0.528
256 | 204 | 79.688 | 0.564 | 0.511
512 | 398 | 77.734 | 0.929 | 0.834
1 024 | 689 | 67.285 | 0.935 | 0.974
2 048 | 1 317 | 64.307 | 2.601 | 3.014
4 096 | 2 264 | 55.273 | 5.790 | 2.870
8 192 | 4 174 | 50.952 | 6.948 | 4.227
16 384 | 7 567 | 46.185 | 11.360 | 7.260
32 768 | 12 268 | 37.439 | 13.661 | 11.419
65 536 | 22 179 | 33.842 | 32.322 | 19.294
131 072 | 41 794 | 31.886 | 53.382 | 31.208
262 144 | 80 551 | 30.728 | 101.434 | 61.626
524 288 | 156 602 | 29.869 | 195.810 | 125.634
1 048 576 | 304 295 | 29.020 | 371.246 | 237.150
2 097 152 | 556 353 | 26.529 | 796.787 | 513.327

### Huffman

alkuperäinen koko (tavua) | pakattu koko (tavua) | pakattu / alkuperäinen (%) | pakkausaika (ms) | purkuaika (ms)
---|---|---|---|---
64 | 59 | 92.188 | 0.195 | 0.353
128 | 95 | 74.219 | 0.223 | 0.429
256 | 172 | 67.188 | 0.222 | 0.431
512 | 314 | 61.328 | 0.192 | 0.441
1 024 | 595 | 58.105 | 0.342 | 1.485
2 048 | 1 144 | 55.859 | 1.183 | 2.437
4 096 | 2 243 | 54.761 | 0.599 | 3.051
8 192 | 4 415 | 53.894 | 1.228 | 6.164
16 384 | 8 798 | 53.699 | 1.065 | 6.924
32 768 | 17 554 | 53.571 | 2.209 | 14.244
65 536 | 35 087 | 53.539 | 3.926 | 34.585
131 072 | 70 106 | 53.487 | 9.235 | 51.271
262 144 | 140 104 | 53.445 | 18.829 | 126.531
524 288 | 280 288 | 53.461 | 29.786 | 213.252
1 048 576 | 560 594 | 53.462 | 62.908 | 417.519
2 097 152 | 1 121 061 | 53.456 | 131.862 | 741.858

### Vertailu

LZW:n pakkaustehokkuus on huono pienillä tekstitiedostoilla, koska tavallisessa tekstissä ei tyypillisesti ehdi olla paljon toistoa lyhyissä teksteissä. Pienimmässä testitiedostossa pakattu tiedosto on jopa suurempi kuin alkuperäinen. 
Noin 50 % pakkaustehokkuus saavutettiin kuitenkin jo noin 4 kilotavun tiedostolla, ja suurimmissa testitiedostoissa saavutettiin huomattava tehokkuus, kun pakattu tiedosto oli vain noin 26-30 % alkuperäisestä tiedostokoosta.

Huffmanin koodauksessa pakatun tiedoston kokoa kasvattaa pakatun tekstin mukana talletettu puun bittiesitys. Huffman saavuttaa noin 50 % pakkaustehokkuuden kuitenkin hieman pienemmällä tiedostolla kuin LZW, mutta sen tehokkuus jää isoillakin tiedostoilla noin 50 %:iin, sillä mikään pakatuista tiedostoista ei ollut alle 53 % alkuperäisestä tiedostokoosta. 

![pakkaustehokkuus](/dokumentaatio/kuvat/kuva_pakkaustehokkuus.png)

Kumpikin algoritmi suoriutui kaikilla tiedostoilla niin pakkaamisesta kuin purkamisesta alle sekunnissa. LZW pakkasi isot tiedostot huomattavasti hitaammin kuin Huffman, joka suoriutui melko nopeasti kaikissa testitiedostoissa. Isojen tiedostojen purkamisesta LZW puolestaan suoriutui hieman nopeammin kuin Huffman. 

![pakkausaika](/dokumentaatio/kuvat/kuva_pakkausaika.png) 
![purkuaika](/dokumentaatio/kuvat/kuva_purkuaika.png)

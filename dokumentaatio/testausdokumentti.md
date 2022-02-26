# Testausdokumentti

Ohjelmaa testataan sekä yksikkötestein että suorituskyvyn osalta.

## Yksikkötestit
Yksikkötestit on tehty JUnit-testeinä luokka- ja metodikohtaisesti. Yksikkötesteissä luodaan ensin testejä varten tarvittavat testitiedostot tai muut rakenteet, joita testeissä käsitellään. Testauskattavuutta seurataan JaCoCon kattavuusraportin avulla. 

Testikattavuus viikon 6 päätteeksi:
![testikattavuusraportti viikko 6](/dokumentaatio/kuvat/kuva_testauskattavuus_viikko6.png)


## Suorituskyky

Ohjelman suorituskykyä testataan eri kokoisilla tekstitiedostoilla, joihin on tuotu sisällöksi Lorem Ipsum -tekstiä sivustolta https://lipsum.com/. 

### Lempel-Ziv-Welch

alkuperäinen koko (tavua) | pakattu koko (tavua) | pakattu / alkuperäinen (%) | pakkausaika (ms) | purkuaika (ms)
---|---|---|---|---
14 230 | 6 740 | 47,4 | 521 | 514

### Huffman

Ei vielä testattu.

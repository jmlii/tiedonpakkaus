# Käyttöohje

Ohjelmaa käytetään komentoriviltä, ja siinä on vain tekstikäyttöliittymä. 

Ohjelmaa voi käyttää Gradlen tai Gradle wrapperin (jolloin komentojen alku on muutettava muotoon ./gradlew) avulla. Ohjelma on tehty Gradlen versiolla 6.7.1.

Ohjelma käynnistetään projektin tiedonpakkausprojekti-kansion juuressa (huom. ei koko repositorion juuressa) komennolla 
```
gradle run
```
Vaihtoehtoisesti projektille voi ensin tehdä buildin 
```
gradle build
```
ja sen jälkeen käynnistää sen build/libs-kansioon muodostetusta jar-tiedostosta:
```
java -jar build/libs/tiedonpakkausprojekti.jar
```

Ohjelman käynnistys avaa käyttöliittymän:
```
***** Tekstitiedoston pakkaus ja purku *****
Valitse toiminto:
1  -  Pakkaa tiedosto
2  -  Pura pakattu tiedosto
3  -  Aja suorituskykytestit
4  -  Sulje ohjelma
```

## Tiedoston pakkaaminen
Valinnalla 1 pystyy pakkaamaan tekstitiedoston.

Ohjelma kysyy ensin pakattavan tiedoston osoitetta. Päävalikkoon pääsee palaamaan kaikissa vaiheissa syötteellä R (tai r). 
```
** Palaa päävalikkoon komennolla R **
Anna käsiteltävän tiedoston osoite:
```

Seuraavaksi ohjelma kysyy, mihin pakattu tiedosto tallennetaan.
```
Anna osoite käsitellylle tiedostolle:
```

Jos pakatulle tiedostolle ei anna omaa osoitetta, generoi ohjelma sen automaattisesti pakattavan tiedoston hakemiston ja nimen perusteella. Jos pakattavaksi tiedostoksi antaa esim. kansiossa files olevan tiedoston f.txt, ja jättää pakatun tiedoston osoitteen tyhjäksi, generoi ohjelma automaattisesti ao. osoitteen ja ilmoittaa sen käyttäjälle:
```
Osoitteeksi asetettu files/f.txt_compressed.bin
```

Ohjelma ei myöskään tallenna pakattua tiedostoa alkuperäisen pakattavan tiedoston päälle, vaan jos pakatulle tiedostolle antaa saman osoitteen kuin pakattavan tiedoston osoite on, ohjelma generoi uuden osoitteen kuten tyhjälle osoitteelle. 
```
Ei voida tallentaa alkuperäisen tiedoston päälle.
Osoitteeksi muutettu files/f.txt_compressed.bin
```

Seuraavaksi ohjelma kysyy, kumpaa pakkausmenetelmää käytetään.
```
Pakkausmenetelmä: L = Lempel-Ziv-Welch, H = Huffman
```

Pakkauksen suoritettuaan ohjelma ilmoittaa onnistuneen pakkaamisen tuloksena syntyneen tiedoston osoitteen:
```
Pakattu tiedosto tallennettu osoitteeseen files/f.txt_compressed.bin
```

Jos pakattavaa tiedostoa tai haluttua tallennuspaikkaa ei löydy, pakkaamista ei suoriteta ja ohjelma antaa käyttäjälle ilmoituksen:
```
Käsiteltävää tiedostoa tai haluttua tallennuspaikkaa ei löydy, tarkista osoitteet.
```

Jos pakattavaksi annettu tiedosto oli tyhjä, ohjelma ei ala pakata sitä, vaan ilmoittaa käyttäjälle tiedoston olleen tyhjä:
```
Pakattavaksi annettu tiedosto on tyhjä, pakkausta ei suoritettu.
```

Jos pakkaaminen epäonnistuu pakkaamisen aikana, pakkaaminen keskeytyy ja ohjelma ilmoittaa siitä käyttäjälle:
```
Pakkaaminen epäonnistui.
```


## Pakatun tiedoston purkaminen
Valinnalla 2 pystyy purkamaan pakatun tiedoston. Käyttäjän on tiedettävä, kummalla pakkausmenetelmällä purettava tiedosto on alunperin pakattu, ohjelma ei itse tunnista sitä tiedostosta.

Ohjelma kysyy purettavan tiedoston osoitetta. Päävalikkoon pääsee palaamaan kaikissa vaiheissa syötteellä R (tai r). 
```
** Palaa päävalikkoon komennolla R **
Anna käsiteltävän tiedoston osoite:
```

Seuraavaksi ohjelma kysyy, mihin purettu tiedosto tallennetaan.
```
Anna osoite käsitellylle tiedostolle:
```

Jos puretulle tiedostolle ei anna omaa osoitetta, generoi ohjelma sen automaattisesti purettavan tiedoston osoitteen ja nimen perusteella. Jos purettavaksi tiedostoksi antaa esim. kansiossa files olevan tiedoston f.txt_compressed.bin, ja jättää puretun tiedoston osoitteen tyhjäksi, generoi ohjelma automaattisesti ao. osoitteen ja ilmoittaa sen käyttäjälle.
```
Osoitteeksi asetettu files/f.txt_compressed.bin_decompressed.txt
```

Ohjelma ei myöskään tallenna purettua tiedostoa alkuperäisen purettavan tiedoston päälle, vaan jos puretulle tiedostolle antaa saman osoitteen kuin purettavan tiedoston osoite on, ohjelma generoi uuden osoitteen kuten tyhjälle osoitteelle. 
```
Ei voida tallentaa alkuperäisen tiedoston päälle.
Osoitteeksi muutettu files/f.txt_compressed.bin_decompressed.txt
```

Seuraavaksi ohjelma kysyy, kumpaa pakkausmenetelmää tiedoston pakkaamisessa on käytetty.
```
Pakkausmenetelmä: L = Lempel-Ziv-Welch, H = Huffman
```

Purkamisen suoritettuaan ohjelma ilmoittaa purkamisen tuloksena syntyneen tiedoston osoitteen:
```
Purettu tiedosto tallennettu osoitteeseen files/f.txt_compressed.bin_decompressed.txt
```

Jos purettavaa tiedostoa tai haluttua tallennuspaikkaa ei löydy, purkamista ei suoriteta ja ohjelma antaa käyttäjälle ilmoituksen:
```
Käsiteltävää tiedostoa tai haluttua tallennuspaikkaa ei löydy, tarkista osoitteet.
```

Jos purettavaksi annettu tiedosto oli tyhjä, ohjelma ei ala purkaa sitä, vaan ilmoittaa käyttäjälle tiedoston olleen tyhjä:
```
Purettavaksi annettu tiedosto on tyhjä, purkua ei suoritettu.
```

Jos purkaminen epäonnistuu purkamisen aikana, purkaminen keskeytyy ja ohjelma ilmoittaa siitä käyttäjälle:
```
Purkaminen epäonnistui.
```

## Suorituskykytestien ajaminen
Valinnalla 3 voi vertailla pakkausmenetelmien suorituskykyä joko yksittäisellä tiedostolla tai kaikilla jonkin kansion sisältämillä tiedostoilla. 

Ohjelma kysyy testattavan tiedoston tai kansion osoitetta. Päävalikkoon pääsee palaamaan kaikissa vaiheissa syötteellä R (tai r). 
```
** Palaa päävalikkoon komennolla R **
Anna testattavan tiedoston osoite tai tiedostot sisältävän hakemiston osoite:
```

Seuraavaksi ohjelma kysyy, mihin kansioon testauksesta muodostuvat tulostiedostot tallennetaan. Jos tiedot halutaan vain ruudulle, jätetään syöte tyhjäksi.
```
Anna hakemisto tulostiedostojen tallettamiselle tai jätä tyhjäksi jos haluat tulokset ruudulle:
```

Jos annettua tiedostoa tai hakemistoa ei löydy tai ei voida käsitellä, ohjelma ei suorita testausta ja ilmoittaa siitä käyttäjälle:
```
Tiedostoa tai hakemistoa ei löydy tai ei voida käsitellä.
Testaus ei onnistunut. Tarkista testattavien tiedostojen osoite.
```

Muutoin ohjelma ajaa suorituskykytestit annetulle tiedostolle tai kansion tiedostoille.
```
Ajetaan suorituskykytestejä...
```

Jos tulosten tallentaminen annettuun hakemistoon ei onnistu, ohjelma antaa käyttäjälle ilmoituksen:
```
Ei voitu tallettaa tiedostoon.
```

Lopuksi ohjelma kysyy käyttäjältä, halutaanko testauksen aikana testattavien tiedostojen yhteyteen muodostuneet pakkaus- ja purkutiedostot poistaa.
```
Poistetaanko suorituskykytestauksen aikana muodostuneet pakkaus- ja purkutiedostot?
Y = kyllä N = ei
```

Ruudulle tulostettavat testien tulokset ilmoitetaan tiedosto- ja algoritmikohtaisesti seuraavaan tapaan:
```
Tiedosto: f.txt
Algoritmi: LZW
Alkuperäinen koko: 3787 bytes 
Pakattu koko: 2174 bytes 
Pakkausteho: 57.407 % 
Pakkausaika: 8.343 ms 
Purkuaika: 3.215 ms 

Tiedosto: f.txt
Algoritmi: Huffman
Alkuperäinen koko: 3787 bytes 
Pakattu koko: 2088 bytes 
Pakkausteho: 55.136 % 
Pakkausaika: 2.486 ms 
Purkuaika: 2.750 ms 
```

Jos tulokset haluttiin tiedostoon, muodostuu annettuun hakemistoon kaksi testitiedostoa: performance_results.txt ja performance_resultsMD.txt. Tiedostossa performance_results.txt tulokset on esitetty vastaavassa muodossa kuin ruudulle tulostettaessa. Tiedostoon performance_resultsMD.txt tulokset talletetaan Markdown-taulukkomuodossa.

## Ohjelman sulkeminen
Ohjelma suljetaan päävalikossa komennolla 4. 

## Testitiedostot
Tiedonpakkausprojekti-kansion (eli gradle-projektin) files-kansiossa on useita tiedostoja, joita voi käyttää ohjelman käytön testaamisessa ja suorituskykytestien ajamisessa. Kansiossa on esim. tyhjä tiedosto emptyfile.txt ja satunnaista tekstisisältöä sisältävä tekstitiedosto f.txt. Lisäksi etenkin suorituskykytestien ajamista varten kansiossa on 16 määräkokoista tiedostoa, 64-tavuisesta 2,1-megatavuiseen. 

## Javadoc
Javadocin voi generoida tiedonpakkausprojekti-kansion juuressa komennolla
```
gradle javadoc
```
Javadocia voi tarkastella avaamalla selaimella tiedosto build/docs/javadoc/index.html.

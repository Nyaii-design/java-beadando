# DevizaPont – Java alkalmazások előadás beadandó

Böngészőben használható Spring Boot alkalmazás (Java 25):
- **Főoldal** – Bootswatch „Flatly” ingyenes reszponzív téma (Bootstrap 5)
- **SOAP** – MNB devizaárfolyamok + Chart.js grafikon
- **Forex** – Oanda v20 API: account, AktÁr, HistÁr, Nyit, Poz, Zár

## Futtatás

```
java -jar beadando.jar
```
Utána böngészőben: http://localhost:8080

## Oanda-token beállítása

1. Másold le az `oanda.properties.minta` fájlt **`oanda.properties`** néven
   ugyanabba a mappába, ahonnan a programot indítod (projekt főmappája / a jar mellé).
2. Írd bele a tokent: `oanda.token=...`
3. A `.gitignore` miatt ez a fájl nem töltődik fel.

A számlaazonosítót nem kötelező megadni, a program a tokenhez tartozó első számlát használja.

## Készítők nevének beállítása

`src/main/resources/application.properties` → `app.keszito1.nev`, `app.keszito1.neptun`, `app.keszito2.nev`, `app.keszito2.neptun`

## beadando.jar készítése (IntelliJ, Maven)

Jobb oldalt Maven → Lifecycle → `clean`, majd `package`.
Elkészül: `target/beadando.jar` → ezt másold a projekt főmappájába, és töltsd fel.

## Projektszerkezet

| Fájl | Menü |
|---|---|
| `FooldalController`, `templates/index.html`, `fragments.html`, `static/css/sajat.css` | Főoldal, téma, lábléc |
| `soap/SoapController`, `soap/SoapUrlap`, `templates/soap.html`, `soapclient/*` | SOAP |
| `forex/OandaService`, `com/oanda/*` | közös Oanda-kapcsolat |
| `forex/ForexAccountController`, `templates/forex/account.html` | Forex-account |
| `forex/ForexAktArController`, `templates/forex/aktar.html` | Forex-AktÁr |
| `forex/ForexHistArController`, `templates/forex/histar.html` | Forex-HistÁr |
| `forex/ForexNyitController`, `templates/forex/nyit.html` | Forex-Nyit |
| `forex/ForexPozController`, `templates/forex/poz.html` | Forex-Poz |
| `forex/ForexZarController`, `templates/forex/zar.html` | Forex-Zár |

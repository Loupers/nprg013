# nprg013-zapoctovy-projekt

zápočtový projekt na javu

# zadani
---

Desktopová verze zjednodušeného kanboardu.

+ GUI aplikace s použitím swingu. https://docs.oracle.com/javase/tutorial/uiswing/
+ pouze pro jednoho uživatele (žádné přihlašování).
+ Možnost tvořit a spravovat projekty a uvnitř projektů spravovat úkoly (task) a jejich stavy (open, todo, doing...)
+ U všeho možnost tvorba, čtení, úprava, mazání (asi můžu říct CRUD?).

#poznámky
---
+ program obsahuje úplě základní soubor pro generaci dokumentace s pomocí doxygen
+ logování errorů probíhá do konzole, log4j2 má správnou verzi bez známých děr
+ build probíhá příkazem `mvn package`
    + ve složce target se vygeneruje soubor `kaboard_jan_piroutek.jar`, který neobsahuje zabalené potřebné knihovny
    + ve složce target se vygeneruje soubor `kaboard_jan_piroutek-jar-with-dependencies.jar`, který obsahuje zabalené potřebné knihovny

#uživatelská příručka
---

po spuštění aplikace se uživatel dostane na úvodní stránku, kde
+ vpravo nahoře se nachází tlačítko `create`, kterým se tvoří nový projekt
+ uprostřed stránky je scrollovatelný list všech založených projektů
  + tlačítko `tasks` přesměruje uživatele na seznam tasků daného projektu
  + tlačítko `tags` přesměruje uživatele na seznam tagů daného projektu
  + tlačítko `edit` umožní uživateli změnit název daného projektu
  + tlačítko `delete` smaže daný projekt z databáze
  
stránka s tagy
+ vpravo nahoře se nacházejí tlačítka
  + `create`, které tvoří nový tag a po vytvoření přesměruje uživatele na jeho editaci
  + `back`, které vrátí uživatele na seznam projektů
+ uprostřed se nachází list s tagy, které mají tlačítka
  + `edit`, které přesměruje uživatele na stránku s editováním tagu
  + `delete`, které smaže tag
  
stránka pro editování tagů
+ vpravo nahoře jsou tlačítka
  + `cancel`, které zruší provedené změny a vrátí uživatele na list tagů
  + `save`, které uloží změny a vrátí uživatele na list tagů
+ pod horními tlačitky se nachází
  + input pro `name` tagu
  + tlačítko `choose`, které otevře dialog, pro vybrání barvy tagu
    
stránka s tasky
+ vpravo nahoře se nacházejí tlačítka
  + `create`, které tvoří nový task a po vytvoření přesměruje uživatele na jeho editaci
  + `back`, které vrátí uživatele na seznam projektů
+ uprostřed se nachází list s tasky, které mají tlačítka
  + `edit`, které přesměruje uživatele na stránku s editováním tasku
  + `delete`, které smaže task

stránka pro editování tasků
+ vpravo nahoře jsou tlačítka
  + `cancel`, které zruší provedené změny a vrátí uživatele na list tasků
  + `save`, které uloží změny a vrátí uživatele na list tasků
+ pod horními tlačitky se nachází
  + input pro `name` tasku
  + select, kterým uživatel může vybrat tag pro daný task
  + input pro description tasku
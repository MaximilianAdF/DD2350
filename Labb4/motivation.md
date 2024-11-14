Kattis testar om din reduktion är korrekt, men du måste naturligtvis kunna bevisa att den är det vid redovisningen. 
Kattis svar är egentligen avsedda att vägleda dig i arbetet med beviset och påpeka om du glömt något viktigt specialfall. 
Vid redovisningen kommer handledaren också att fråga varför problemet ligger i NP och vad komplexiteten är för din reduktion.

# Korrekthetsbevis
Kattis :P

# Varför ligger problemet i NP
För att visa att rollbesättningsproblemet är NP-svårt, kan vi använda en Karpreduktion från det NP-fullständiga problemet Graffärgning. Graffärgning innebär att vi måste bestämma om vi kan färga hörnen i en graf med högst `m` färger så att inga två angränsande hörn får samma färg. Detta kan omvandlas till ett rollbesättningsproblem där vi söker efter en lösning som uppfyller samma krav.

## Beskrivning av Graffärgning:
- Indata: 
    - En oriktad graf med `V` hörn och `E` kanter. Isolerade hörn och dubbelkanter kan förekomma, inte öglor.
    - En parameter `m`, som anger hur många färger vi högst kan använda för att färga hörnen.
    - Varje kant representerar ett krav att de två hörnen som den förbinder måste ha olika färger

- Fråga:
    - Kan vi färga hörnen i grafen med högst mm färger så att inga två angränsande hörn får samma färg?

## Omvandling till Rollbesättningsproblem:
För att reducera Graffärgning till Rollbesättningsproblemet gör vi följande omvandlingar.

- Roller
    - För varje hörn i grafen skapar vi en roll i rollbesättningsproblemet. Om grafen har `V` hörn, skapar vi `V+2` roller (+2 för divorna p1 och p2).
- Skådespelare
    - För att färga hörnen använder vi max `m` färger. Vi skapar `Math.min(V+2, m+2)` skådespelare där varje skådespelare representerar en unik färg.
- Rolltilldelning (vilkor typ 1)
    - För varje hörn i grafen, tilldela alla skådespelarna förutom P1 och P2.
    - För varje hörn `i` i grafen, kan denna roll tilldelas någon av de tilldelade skådespelarna, så länge den tilldelningen ej bryter mot kraven i rollbesättningsproblemet. (GÖRS AV KATTIS)
- Scener
    - För varje kant mellan två hörn `x` och `y` i grafen, skapar vi en scen med rollena `x` och `y`.
    - En scen kan bara ha två skådespelare men enl. teoriuppgifterna kan flera scener med två roller kombineras till större scener om grafen komplett.
- P1 och P2
    - De speciella skådespelarna p1 och p2 som representerar de två divorna behöver inte användas direkt i Graffärgning. Men vi kan se till att de används för att representera de två första rollerna i scenerna och att de inte får vara med i samma scen

Reduktionen från Graffärgning till rollbesättningsproblemet kan genomföras i polynomtid i storleken på grafen, eftersom vi bara behöver skapa roller, skådespelare och scener, vilket kan göras på ett polynomväxande antal steg i relation till antalet hörn och kanter.

# Komplexiteten för våran reduktion
Komplexiteten beror på indata parametrarna i Graffärgnings problemet uttrycks i dessa variabler. Det vill säga `V` *antal hörn*, `E` *antal kanter* och `m` *antal färger*. Det som tar längst tid är inläsningen och utskriften av kanterna som konverteras till scener, det finns `E` indviduella kanter som måste gås igenom. Dessutom skrivs `V+2` extra scener ut som ser till att alla roler deltar i minst en scen. Det ger komplexiteten `O(E + V + 2)`
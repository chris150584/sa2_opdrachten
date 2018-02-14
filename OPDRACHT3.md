Opdracht 3
==
Voorbereiding
--
1. Doorloop volgende officiele Spring guides:
    * [Handling form submission](https://spring.io/guides/gs/handling-form-submission/)
    * [Validating form input](https://spring.io/guides/gs/validating-form-input/)

2. Indien je dit nog niet gedaan hebt, breng dan je _fork_
synchroon met de originele repository. Het synchroniseren
van een git fork wordt toegelicht in [dit filmpje](https://www.youtube.com).
Vergeet niet om je lokaal gemaakte aanpassingen eerst te committen
en te pushen zodat je geen werk verliest in geval van
problemen.
    * Je voegt een _remote_ toe die je _upstream_ noemt:  
    `git remote add upstream https://github.com/kdg-ti/sa2_opdrachten.git`
    * Je download alle aanpassingen van upstream:  
    `git fetch upstream`
    * Je doet een _merge_ van alle aanpassingen die gebeurd
    zijn in upstream:  
    `git merge upstream/master`

Deel 1
--
Maak een nieuwe controller aan: `be.kdg.blog.controllers.mvc.NewEntryController`.
Deze controller moet het `Blog` object kunnen aanspreken,
dus je voegt een `final` attribuut toe van het type `Blog` dat je
via _constructor injection_ initialiseert.  
Een GET request op `/new_entry` moet een HTML formulier tonen
dat de gebruiker moet toelaten om een nieuwe entry toe te voegen
aan de blog:
* Je voegt een methode toe aan je nieuwe controller die deze
GET request kan afhandelen.  
Je gaat tewerkt volgens Spring MVC:
    * Als view naam gebruik je "new_entry"
    * In het model stop je een `List` van alle tags die beschikbaar
    zijn
    * In het model stop je een **nieuw** `BlogEntry`
    object.  
    Dit laatste hebben we in voorgaande code nog niet gedaan.
    De bedoeling is dat de gebruiker het formulier invult en
    zo ook het nieuw aangemaakte object invult. Dit ingevulde
    object wordt dan terug naar de server gestuurd zodat we
    het aan de lijst van entries kunnen toevoegen (als alles
    correct ingevuld is!).
* Maak een nieuw Thymeleaf template genaamd `new_entry.html`.  
Naast de gebruikelijke hoofdingen en een titel moet deze template
een formulier bevatten (HTML `form`) dat het lege `BlogEntry`
object kan invullen. Je kan hiervoor vertrekken vanaf volgend
stuk HTML code en aanvullen met de nodige Thymeleaf tags.  
Kijk ook zeker eens naar sectie 6.4.2 van het boek op p.189 en
volgende.
    * Aan de `form` tag voeg je een `th:object` en een `th:action` toe:  
        * `th:object` geef je als waarde de naam van het nieuw aangemaakte
        `BlogEntry` object dat je eerder vanuit de controller in het model
        hebt gestoken. (`th:object="${objectnaam}"`)  
        * `th:action` geef je als waarde de URL waar deze POST op moet
        toekomen: `th:action="@{/new_entry}"`. Met de "@"-notatie
        verkrijg je een [context-relative URL (zie puntje 2)](http://www.thymeleaf.org/doc/articles/standardurlsyntax.html).
        
    We kunnen nu de individuele input-velden (HTML tags) koppelen
    aan de attributen van het Java object.
    * Aan de `input` tag voor het _subject_ voeg je een
    `th:field` toe om aan te duiden met welk veld/attribuut
    dat deze tag overeen komt.  
    Met [de *{...} syntax](http://www.thymeleaf.org/doc/tutorials/2.1/thymeleafspring.html#inputs)
    kan je de naam van het veld specifieren. Zie ook p.192 van
    het boek (even naast de error handling kijken).
    * Aan de `textarea` tag voor de _message_ voeg je ook
    een `th:field` toe.

```
<form method="POST">
    <div>
        <label for="subject">Subject</label>
        <input type="text" id="subject"/>
    </div>
    <div>
        <label for="message">Message</label>
        <textarea id="message" th:field="*{message}" rows="12"></textarea>
    </div>
    <button>Submit</button>
</form>
``` 
Surf eens naar `localhost:8080/new_entry` om te kijken of je
formulier goed getoond wordt.

Deel 2
--
Voeg een nieuwe methode toe aan `NewEntryController` die
de POST actie van deel 1 kan opvangen:
* Gebruik `@PostMapping` met als pad "/new_entry".
* Voeg een parameter toe van het type `BlogEntry` en annoteer
deze parameter met `@ModelAttribute("objectnaam")`. Als je
in de plaats van "objectnaam" de juiste naam van het
model-attribuut gebruikt dan zal deze parameter automatisch
gekoppeld worden aan het binnenkomende (ingevulde) object
met dezelfde naam.
* Voeg een parameter toe van het type `BindingResult`.
Dit is een extra Spring object dat ons zal kunnen vertellen
ofdat de mapping van HTML naar Java gelukt is.
* In de methode zelf: Als `bindingResult.hasErrors()` `true` teruggeeft dan willen
we de gebruiker terug hetzelfde `form` voorschotelen:
    * Stel de view naam in op "new_entry"
    * In het model stop je een List van alle tags die beschikbaar zijn
    * Je hoeft **geen** nieuw `BlogEntry` object in het model te
    plaatsen in tegenstelling tot bij de `@GetMapping` implementatie.
    Dit komt doordat de `@ModelAttribute` parameter automatisch
    opnieuw terug in het model wordt gestoken (inclusief de foute
    input die erin was terecht gekomen).
* Als `bindingResult.hasErrors()` `false` teruggeeft dan
voeg je het `BlogEntry` object toe aan de blog. (Gebruik
voorlopig een **lege List** als parameter voor de tags.) Vervolgens
redirect je de gebruiker naar de home-pagina: `return "redirect:/";`

Surf naar `localhost:8080/new_entry` en geef een subject/message
in. Je zou je nieuwe entry in de lijst moeten zien verschijnen.
De tags doen we in het volgende deel.

Deel 3
--
De velden die we aan de client laten zien (HTML/Thymeleaf) zijn
niet altijd dezelfde velden als die van het model. Daarbovenop
zal de structuur van de gegevens ook meestal verschillen (platte
structuur t.o.v. geneste structuur).

Om de view en het model los te koppelen van elkaar gebruiken
we DTOs (Data Transfer Objects). In de Thymeleaf templates
zullen we vanaf nu **enkel nog gebruik maken van DTOs**. De controller wordt
verantwoordlijk voor het omzetten van en naar DTOs.

Bestudeer volgende klassen:
* `be.kdg.blog.dto.thymeleaf.BlogEntryDto`
* `be.kdg.blog.dto.thymeleaf.TagDto`
* `be.kdg.blog.dto.thymeleaf.form.NewBlogEntryFormDto`
* `be.kdg.blog.dto.mapper.DtoMapper`

Om eenvoudig te starten passen we `HomeController` als
eerste aan:
* GET op `/`
    * De `List` van `BlogEntry` objecten die we in het model
    stoppen moet een `List` worden van `BlogEntryDto` objecten.  
    Gebruik de methode `DtoMapper::convertBlogEntriesToDto` om
    de lijst van het ene type om te zetten naar een lijst
    van het andere type.
    * Pas je test aan in de klasse `HomeControllerTests`
    en probeer je code uit met de browser.

Pas `NewEntryController` aan:
* GET op `/new_entry`
    * In het model stoppen we een object van het type `NewBlogEntryFormDto`
    en een `List` van `TagDto` objecten (`DtoMapper::convertTagsToDto`).
* POST op `/new_entry`
    * Het type van de `@ModelAttribute` parameter pas je aan naar
    `NewBlogEntryFormDto`.
    * Met behulp van de methode `DtoMapper::convertFromDto`
    zet je het binnenkomende object om van een DTO naar
    een model-klasse.
    * Als `bindingResult` fouten bevat dan stoppen we een `List`
    van `TagDto` objecten in het model i.p.v. `Tag` objecten.
    (analoog aan: GET op `/new_entry`) 

Om voor een nieuwe blog entry de tags te kunnen invullen
moeten we nog enkele checkboxes laten genereren. Plaats
volgende `div` tussen de _Subject_ `div` en de _Message_ `div`.  
De naam van het model-attribuut `allTags` zal je nog moeten aanpassen naar de naam
die jij hebt gekozen.
```
<div>
    <label>Tags</label>
    <ul>
        <li th:each="tag : ${allTags}">
            <input type="checkbox" autocomplete="off"
                   th:name="${tag.id}"
                   th:value="${tag.id}"
                   th:field="*{tagIds}"/>
            <label th:text="${tag.name}"></label>
        </li>
    </ul>
</div>
```
In `NewEntryController` kan je dan bij de aanroep naar
`Blog::addEntry` de tweede parameter een zinvolle waarde
geven: gebruik `NewBlogEntryFormDto::getTagsIds`.

Probeer ook dit even uit met je browser.

Deel 4
--
Probeer eens een _Subject_ of een _Message_ in te geven van minder
dan 3 karakters. Je zal zien dat dit niet lukt. Dit komt door
de `@NotNull` en `@Size` annotaties die je kan terugvinden in de klasse 
`NewBlogEntryFormDto`.  
Bij een foute ingave kom je als gebruiker terug terecht op
URL `/new_entry`, dit komt doordat we dit zo gespecifieerd
hebben in de `@PostMapping` methode van `NewEntryController`.
Concreet gebeurt er nu al dit:
* `bindingResult` bevat errors
* De lijst van tags wordt in het model geplaatst
* De view naam wordt op `new_entry` gezet
* De `@ModelAttribute` parameter met de te korte waardes
wordt terug in het model geplaatst
* De Thymeleaf template wordt opgemaakt en de gebruiker
krijgt zijn/haar te korte waardes opnieuw te zien

Voeg volgend stuk code toe onder de `input` tag van het
_Subject_:

```
<span th:if="${#fields.hasErrors('subject')}" th:errors="*{subject}">Subject Error</span>
```
Dit zal ertoe leiden dan er een extra `span` tag verschijnt
indien de gebruiker foute invoer meegaf. Zo krijgt de
gebruiker feedback over de invoer en kan hij/zij deze
corrigeren.

* Voeg ook voor **tagIds** en **message** gelijkaardige
`span` tags toe.

Deel 5
--
Voeg volgende HTML `nav` tag toe aan beide Thymeleaf templates:
```
<nav>
    <ul>
        <li>
            <a th:href="@{/}">My Blog</a>
        </li>
        <li>
            <a th:href="@{/new_entry}">New post</a>
        </li>
    </ul>
</nav>
```

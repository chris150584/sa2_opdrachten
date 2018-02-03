Opdracht 2
==
Voorbereiding
--
Kopieer je klassen `be.kdg.blog.controllers.ResponseBodyController`
en `be.kdg.blog.tests.mvc.ResponseBodyControllerTests` van je repository van opdracht 1
naar dit project. Zo kan je alle volgende opdrachten uitwerken met dit ene project.

Deel 1
--
Maak een nieuwe controller aan genaamd `HomeController`. Deze controller zal de home-pagina
(URL "/") van onze blog-applicatie implementeren. `HomeController` zal werken volgens het
MVC patroon van Spring, plaats deze controller dus in de package
`be.kdg.blog.controllers.mvc`.

* Analoog aan opdracht 1: Zorg voor een `final` attribuut van het type `Blog` dat via de
constructor meegegeven wordt. De verantwoordelijkheid voor het aanmaken van de blog ligt
**niet** bij deze controller. Gebruik dus dependency injection bij de constructor.

Voeg een methode toe aan je `HomeController` om volgende functionaliteit te voorzien.

* GET op `/`  
Je controller zorgt voor een correcte instelling van de view naam en een correcte
opvulling van het model:
    * __View:__ de naam van de view stel je in op de waarde "home". Dit kan je o.a.
    doen door deze `String` als return-waarde terug te geven of door hem te geven
    aan de constructor of setter van de klasse `ModelAndView`.  
    __Let op:__ geen `@ResponseBody` bij MVC.
    * __Model:__ in het model plaats je een attribuut genaamd "entries" met als waarde
    alle entries van de blog.

Maak een view genaamd "home", dit doe je door een bestand met de naam `home.html` te
plaatsen in de directory `src/main/resources/templates/`. Om de view uit te werken
gebruiken we Thymeleaf. Voeg volgend stuk code toe aan
`src/main/resources/application.properties` om Thymeleaf to configureren.
```
spring.thymeleaf.enabled=true
spring.thymeleaf.cache=false
spring.thymeleaf.encoding=UTF-8
spring.thymeleaf.mode=HTML5
spring.thymeleaf.prefix=classpath:templates/
spring.thymeleaf.suffix=.html
```

In `home.html` doe je dit:
* Je overloopt de elementen van attribuut "entries" met `th:each`.
* Van elke entry toon je de _subject_, _message_ en _tags_. Een stukje tekst
tonen kan je doen met `th:text`. De tags zal je individueel moeten verwerken
door opnieuw `th:each` te gebruiken.
* Het dateTime veld kan je toevoegen met
`th:text="${#temporals.format(entry.dateTime, 'dd/MMM/yyyy HH:mm')}"`
(ervan uitgaande dat je de naam "entry" gekozen hebt).
* Je kan o.a. inspiratie opdoen bij het [upvote](https://github.com/kdg-ti/upvote) project. Zie `index.html`.

Je kan starten met deze HTML:
```
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <title>My Blog</title>
</head>
<body>

<section>
    <h3>SUBJECT</h3>
    <span>TAG</span>
    <article>
        <p>MESSAGE</p>
        <span>DATE</span>
    </article>
</section>

</body>
</html>
```

__**Extra:**__ Indien je statische bestanden zoals CSS of Javascript wil
toevoegen aan je webapplicatie dan kan je in `application.properties`
het volgende toevoegen.
```
spring.mvc.static-path-pattern=/client/**
spring.resources.static-locations=classpath:/client/
```
Dit zal je toelaten om onder `resources/client` zulke bestanden te
plaatsen.

Deel 2
--
Maak een nieuwe test-klasse `be.kdg.blog.tests.mvc.HomeControllerTests`
volgens deze specificaties:
* Voeg de nodige boilerplate code toe (`MockMvc`, `WebApplicationContext`,
`@RunWith`, `@SpringBootTest`, `@WebAppConfiguration`, `setup` methode,
`@MockBean` `Blog`, ...). Volledig analoog aan opdracht 1.
* Voeg een `@Test` methode toe die het volgende doet:
    * We mocken het `blog` object en dus moeten we het gedrag
    vastleggen van de `getEntries` methode (_stubbing_). Met behulp van de methode
    [BDDMockito::given](https://static.javadoc.io/org.mockito/mockito-core/2.13.0/org/mockito/BDDMockito.html)
    kunnen we dit voor elkaar krijgen.  
    Maak een `List` aan met drie dummy `BlogEntry` objecten die je elks
    hard-coded een welpbepaalde _subject_ en _message_ naar keuze geeft.  
    Met behulp van
    `given` probeer je nu vast te leggen dat de `getEntries` methode, aangeroepen
    op jouw blog object, het eerder gemaakte lijstje moet teruggeven.
    * Via MVC simuleer je nu een GET op `/` waarbij je enkel `text/html`
    accepteert.
        * Verwacht status code 200
        * Verwacht dat de naam van de view "home" is
        * Verwacht dat het attribuut "entries" een _item_ heeft met een
        _property_ genaamd "subject" die de waarde heeft van de eerste _subject_ uit
        je lijstje. Doe dit ook voor de eerste "message". Doe de controles
        ook voor de tweede en derde _subject_ en _message_.

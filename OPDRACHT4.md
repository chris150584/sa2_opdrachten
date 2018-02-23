#Opdracht 4
##Voorbereiding
* Doorloop volgende officiele Spring guide:
    * [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)  
      Om het geheel te testen kan je ook een kleine controller maken
      in plaats van te werken met de `CommandLineRunner`.  
      Het gedeelte "Build an executable JAR" mag je overslaan.
* Breng je _fork_ synchroon met de originele repository.
* Lees hoofdstuk 11 van het boek (de _setup_/configuratie van
_entity-managers_, _datasources_, enz. mag je overslaan)

##Deel 1
We gaan onze gegevens persisteren door ze op te slaan
in een relationele databank. Om vlot te kunnen
ontwikkelen kiezen we voor een H2 databank die **bij
elke uitvoering volledig wordt ge-_reset_**.

Als eerste stap zullen we de entiteiten definiëren en
en als tweede stap de repositories. Dat is het minimum
dat we nodig hebben om opslag te voorzien via een
[ORM](https://en.wikipedia.org/wiki/Object-relational_mapping).  
We maken ook meteen van de gelegenheid gebruik om
_services_ te introduceren: de componenten die de
business-logica zullen bevatten.  
Tenslotte doen we enkele minimale aanpassingen aan
de bestaande controllers.

a) Entiteiten  
b) Repositories  
c) Services  
d) Controllers

###a) Entiteiten
* Verwijder de klasse `Blog`. (De gegevens zullen we
  vanaf nu ophalen uit een tabel.)  
  Verwijder ook `ResponseBodyController` en `ResponseBodyControllerTests`.
  Deze klassen zullen we niet verder gebruiken en updaten.
* Pas de klasse `BlogEntry` aan door er _Hibernate_
  annotaties aan toe te voegen. Met deze annotaties
  kunnen we aanduiden met welke tabel een klasse
  overeenkomt en met welke kolommen de attributen overeenkomen.  
  **Neem de tijd om de documentatie van deze annotaties
  even te raadplegen.**
  * Voeg de klasse-annotatie `@Entity` toe.
  * Voeg bij het `id` attribuut de `@Id` en
  `@GeneratedValue(strategy = GenerationType.IDENTITY)`
  annotaties toe.
  * Voeg bij zowel `subject`, `message` als `dateTime`
  `@Column(nullable = false)` toe. `@Column` is optioneel
  in een `@Entity` klasse tenzij je non-default
  attribuut-waardes gebruikt zoals in ons geval (`nullable = false`).
  * Voeg bij message ook nog `@Lob` toe.
  * Verander het type van `dateTime` naar `java.sql.Timestamp`.
  (De Java 8 date/time API is nog niet ondersteund in
  onze versie van Hibernate)
  * Voeg bij het `tags` attribuut volgende annoties toe.  
  De veel-op-veel relatie tussen entry en tag wordt
  achter de schermen geimplementeerd d.m.v. de
  tussentabel `blog_entry_tag`. We zullen verder **niet**
  in aanraking komen met deze tabel, alles gebeurt achter de
  schermen.
```
@ManyToMany(cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
@JoinTable(
        name = "blog_entry_tag",
        joinColumns = {@JoinColumn(name = "blog_entry_id")},
        inverseJoinColumns = {@JoinColumn(name = "tag_id")}
)
```
* We gaan analoog tewerk in de `Tag` klasse:
  * Voeg bij het `id` attribuut de `@Id` en
  `@GeneratedValue(strategy = GenerationType.IDENTITY)`
  annotaties toe.
  * Voeg bij `name` `@Column(nullable = false)` toe.

###b) Repositories
* Maak een nieuwe package aan genaamd `be.kdg.blog.persistence`.
In deze package maak je twee interfaces aan:
  * `BlogEntryRepository`: een interface die een uitbreiding is
  van `JpaRepository<T, ID>`. Als `T` neem je de juiste entiteit
  en als `ID` neem je het (wrapper-)type van de ID van deze entiteit.
  * Idem voor `TagRepository`

###c) Services
* Maak een nieuwe package aan genaamd `be.kdg.blog.services`.
In deze package maak je twee klassen aan:
  * `BlogEntryService`: een "gewone" klasse die je annoteert met
  `@Service` en `@Transactional`. Zoek beide annotaties op in de
  documentatie. Je zal zien dat `@Service` een specifieker
  geval is van een annotatie die we al eerder tegenkwamen.
    * Voeg twee `final` attributen toe: één voor elke repository.
    * Maak een constructor met twee parameters waarmee je beide
    attributen initialiseert **via dependency injection**.  
    Handig feit: repositories zitten automatisch in de Spring container,
    zelfs zonder `@Component`.
    * Maak een methode met dit signatuur: `public List<BlogEntry> findAll()`  
    Je implementeert de methode zodat ze een lijst teruggeeft met
    alle blog-entries die in de repository zitten.
    (eenvoudige [delegation](https://en.wikipedia.org/wiki/Delegation_pattern))
    * Maak een methode met dit signatuur: `public void save(BlogEntry blogEntry, List<Long> tagIds)`  
    Eerst moet de methode het object `blogEntry` aanpassen door de juiste tags erin te zetten.
    De tags haal je op uit de juiste repository op basis van de tags IDs.  
    Vervolgens bewaar je `blogEntry` in de juiste repository. 
  * `TagService`: ook een transactional service.
    * Je hebt enkel een attribuut nodig voor de `TagRepository`.
    * Maak ook een `findAll` methode (zoals bij `BlogEntryService`).

###d)Controllers
* Neem `NewEntryController` en `HomeController` onder handen.
Je vervangt referenties naar `Blog` door referenties naar de
nodige **services**.  
**Let op:** laat je controllers niet rechtstreeks met je repositories
praten, enkel met je services. (Controller --> Service --> Repository)  
Maak gebruik van dependency injection (zoals eerder met `Blog`).

###Configuratie
Voeg het volgende toe in `application.properties`.
Via deze configuratie geven we aan dat we een embedded
(in memory) H2 databank gebruiken die geïnitialiseerd
wordt met gegevens uit `data.sql`. Daarnaast willen we
de gegenereerde SQL statement in de log kunnen terugvinden
en willen we ook op `localhost` kunnen surfen naar
`/h2-console` om een kijkje te nemen in de databank.
```
# default user = sa; default password is empty
spring.datasource.url=jdbc:h2:mem:blog
spring.datasource.data=classpath:sql/data.sql
spring.datasource.initialize=true

spring.jpa.show-sql=true
spring.jpa.generate-ddl=true
spring.jpa.hibernate.ddl-auto=create-drop

spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

##Deel 2
###Aanpassing HomeControllerTests
* Het `@MockBean` pas je aan van `Blog` naar `BlogEntryService`.
* Je past je `given` statement aan zodat je je service methode
_stubt_ i.p.v. de vroegere `Blog::getEntries`
###NewEntryControllerTests
* Maak een nieuwe klasse `be.kdg.blog.tests.mvc.NewEntryControllerTests`
* Je _mockt_ zowel `BlogEntryService` als `TagService`.
* Maak een `@Test` methode genaamd `testAddBlogPage`:
  * TODO
###BlogEntryServiceTests
* Maak een nieuwe klasse `be.kdg.blog.tests.services.BlogEntryServiceTests`
* Je _mockt_ zowel `BlogEntryRepository` als `TagRepository`.
* Dit is **geen** MVC test. We gaan de `BlogEntryService` klasse
testen en hebben dus een referentie nodig naar een object van
deze klasse. Maak een attribuut aan van het type `BlogEntryService`
dat je initialiseert met __*attribute dependency injection*__
(in de context van een test is dat best OK).
* Maak een `@Test` methode genaamd `testSaveBlogEntry`:
  * TODO

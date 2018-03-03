Opdracht 5
==
Voorbereiding
--
* Doorloop volgende officiële Spring guide:
    * [Securing a Web Application](https://spring.io/guides/gs/securing-web/)  
* Breng je _fork_ synchroon met de originele repository.
* Lees hoofdstuk 9 van het boek:
  * Sectie 9.1 is minder belangrijk omdat we Spring Boot gebruiken
  * Secties 9.2.2 en 9.2.3 zijn interessant, maar mag je overslaan
  * Sectie 9.2.4 is van toepassing op onze blog,
  zeker lezen dus!
  * Ook sectie 9.3 is voor ons van toepassing (met uitzondering van 9.3.2)
  * Ook de overige secties (9.4 en 9.5) zijn van toepassing.
  Enkel de JSP voorbeelden kan je overslaan aangezien we
  Thymeleaf gebruiken.

Deel 1
--
We gaan gebruiker toevoegen aan de blog, dus we beginnen
met een nieuwe entiteit `User`, gevolgd door een `UserRepository`
en een `UserService`.

**User**  
* In `model` maak je een `@Entity` genaamd `User`.
  * Attributen (gebruik de juiste Hibernate annotaties):
    * `id` een `long`
    * `name` een verplichte `String` (dit is een login
    identifier, geen gewone voornaam/achternaam)
    * `password` een verplichte `String`
  * Voeg getters toe voor de naam en het wachtwoord
  * Maak een default constructor en een constructor die
  enkel een parameter heeft voor de naam (deze laatste
  kan handig zijn bij het schrijven van tests)

**UserRepository**  
* Maak deze interface aan als een `JpaRepository` waarmee
je `User` objecten kan _fetchen_.
* Je voorziet een methode in deze interface waarmee
gebruikers opgehaald kunnen worden op basis van een
gegeven login-naam

**UserService**  
* Deze service bouwen we zoals we de voorgaande services
hebben gemaakt, dus gebruik de juiste annotaties.
* De service is afhankelijk van `UserRepository`. Deze
dependency los je zoals steeds op via _dependency injection_.
* Omdat we willen inhaken op de bestaande security-functionaliteit
van Spring moeten we deze service de interface
`org.springframework.security.core.userdetails.UserDetailsService` laten implementeren.
* Doe een `@Override` van `loadUserByUsername` met volgende
functionaliteit:
  * Je haalt de gebruiker met de gegeven login-naam op
  uit de repository.
  * Als de gebruiker niet gevonden kon worden gooi je een
  `UsernameNotFoundException` (Spring Exception)
  * Als de gebruiker wel gevonden kon worden return je een
  nieuw object van het type `org.springframework.security.core.userdetails.User`:
    * **Let op**: deze klasse noemt ook `User`, net zoals
    onze eigen `User` klasse. Gebruik op deze plek in je
    code dus de [fully qualified name](https://stackoverflow.com/questions/11907245/java-import-statement-vs-fully-qualified-name).
    * Maak het object aan met de naam en het wachtwoord van
    de opgehaalde `User`. Als derde parameter kan je deze list meegeven:
```
List<GrantedAuthority> authorities = new ArrayList<>();
authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
```
* Kijk in de documentatie na wat de relatie is tussen
`org.springframework.security.core.userdetails.UserDetails`
en `org.springframework.security.core.userdetails.User`.

**LoginController**  
(Deze controller roepen we in het leven om ervoor te zorgen
dat we kunnen surfen naar `/login` om zo de login pagina
te zien)
* Maak een nieuwe controller aan genaamd `LoginController`.
* Een GET request naar `/login` moet ervoor zorgen dat
de view genaamd "login" gebruikt wordt. (We stoppen niets
in het Spring Model)

**login.html**
* Maak een nieuwe Thymeleaf template genaamd `login.html`.
* Voeg de gebruikelijke hoofdingen, `meta` tags en `title`
tag toe.
* Voeg ook de `nav` toe zoals in de andere templates,
om vlot te kunnen navigeren.
* Vul aan met dit login-formulier:
```
<h1>Sign in</h1>

<form method="POST" th:action="@{/login}">
    <div>
        <label for="username">User name</label>
        <input type="text" name="username" id="username"/>
    </div>
    <div>
        <label for="password">Password</label>
        <input type="password" name="password" id="password"/>
    </div>
    <button type="submit">Sign in</button>
</form>
```

**WebSecurityConfig**
* Maak een nieuwe klasse `be.kdg.blog.config.WebSecurityConfig`
met volgende inhoud:
```
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserService userService;

    @Autowired
    public WebSecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/new_entry").authenticated()
                .antMatchers(HttpMethod.GET, "/edit_entry").authenticated()
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                .and()
                .csrf()
                .ignoringAntMatchers("/h2-console/**")
                .and()
                .headers()
                .frameOptions()
                .sameOrigin();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(authProvider());
    }

    @Bean
    public DaoAuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(this.userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```
* Lees secties 9.3 (behalve 9.3.2) en 9.4 van het boek indien je dit nog
niet hebt gedaan. Dit is essentieel om te begrijpen wat er staat en
om er toe in staat te zijn wijzigingen aan te brengen in deze klasse.
  * Over `passwordEncoder` kan je [in deze guide](http://www.baeldung.com/spring-security-registration-password-encoding-bcrypt)
  of op p.254-256 van het boek informatie terugvinden.

**Thymeleaf aanvullingen**  
Op elk van de drie templates voer je volgende veranderen door:
* Je voegt bovenaan de Thymeleaf Spring Security namespace toe:
`xmlns:sec="http://www.thymeleaf.org/thymeleaf-extras-springsecurity3"`
* Je voegt volgende `meta` tags toe om
[Cross-Site Request Forgery](https://www.owasp.org/index.php/Cross-Site_Request_Forgery_(CSRF))
tegen te gaan:
```
<meta name="_csrf" th:content="${_csrf.token}"/>
<meta name="_csrf_header" th:content="${_csrf.headerName}"/>
```
* Je breidt de `nav` tag uit met drie extra `list items`:
  * De eerste `li` tag bevat een link naar de login pagina:
  `<a th:href="@{/login}">Sign in</a>`.  
  De `li` mag enkel
  verschijnen indien je nog **niet** ingelogd bent. Met
  het attribuut `sec:authorize="!isAuthenticated()"` speel je
  dit klaar.
  * De tweede `li` tag is geen echte link, maar deze vermeldt
  als wie je bent ingelogd:  
  `<span>Signed in as <span sec:authentication="name"></span></span>`  
  Zorg ervoor dat de `li` tag enkel verschijnt indien je **wel**
  ingelogd bent.
  * De derde `li` tag bevat volgend mini-formuliertje dat je toelaat om
  uit te loggen. Zorg ervoor dat ook deze `li` tag enkel verschijnt
  indien je **wel** ingelogd bent.
```
<form th:action="@{/logout}" method="POST">
    <button type="submit">Sign out</button>
</form>
```


(Ga meteen verder met deel 2. Je zal nog niet kunnen testen zonder
gebruikers in je databank.)

Deel 2
--
Het doel van het inloggen is niet enkel om gegevens of functionaliteit
af te schermen. We willen elke `BlogEntry` ook koppelen aan de gebruikers
van het systeem. Door `BlogEntry` en de bestaande services, controllers
en DTO's aan te pakken kunnen we dit implementerem.

**BlogEntry**
* Voeg een attribuut `user` van het type `User` toe.
* Gebruik de `@ManyToOne` en `@JoinColumn` annotaties. Zorg ervoor (zonder
een `@Column`) dat deze _foreign key_ verplicht is.
* Maak een getter en setter voor dit nieuwe attribuut.
* Je kan ook een extra constructor voorzien of een bestaande constructor
aanpassen om later vlot te kunnen testen.

**BlogEntryDto**
* Voeg een attribuut `userName` toe. Dankzij de _modelmapper_ naming
conventions zal dit automatisch "gemapt" worden.
* Maak een getter + setter voor dit nieuwe attribuut.

**NewEntryController**
* Pas je POST methode aan door er een parameter aan toe te voegen:
`@AuthenticationPrincipal UserDetails userDetails`
  * De klasse `UserDetails` hadden we eerder gebruikt in de `UserService`
  en zou je dus bekend moeten voorkomen.
  * Kijk naar de gegeven `WebSecurityConfig` klasse. Hier kan je terugvinden
  dat een POST naar `/new_entry` steeds "authenticated" moet zijn.
  Bijgevolg kunnen we in deze methode opvragen **als wie** de gebruiker
  ingelogd is. Dat doet net deze parameter, die Spring wederom
  automatisch invult.
* Geen de **naam** van de ingelogde gebruiker door als parameter
aan de methode `save` van `BlogEntryService`.  
We passen nu `BlogEntryService` aan ...

**BlogEntryService**
* In de `save` methode:
  * Voeg een parameter toe voor de login-naam.
  * Haal de `User` op uit de `UserRepository` (een toe te voegen
  dependency voor deze klasse).
  * Als de `User` niet bestaat gooi je opnieuw een `UsernameNotFoundException`
  * Als de `User` wel bestaat pas je je `BlogEntry` object aan
  door er de gebruiker in in te stellen.

**home.html**
* Je toont momenteel al de _subject_, tags, _message_ en datum van de entry.
Vul aan door nu ook de `userName` (zie DTO klasse) te tonen in een aparte
`span` tag (plaatsen naast de datum).

**data.sql**
* Neem de SQL statements over van `data_v2_security.sql`. Je kan één van de
twee SQL bestanden verwijderen. In `application.properties` staat vermeld
welk bestand er in gebruik is.

Je kan je applicatie nu uitproberen, maar de Java tests zullen nog niet compileren.
Je kan ervoor kiezen om enkele tests voorlopig in commentaar te zetten.

Deel 3
--
**HomeControllerTests**
* In de `@Before` schakel je Spring Security aan:
  * Voeg `.apply(springSecurity())` toe net voor je `build` aanroept.

**NewEntryControllerTests**
* In de `@Before` schakel je Spring Security aan:
  * Voeg `.apply(springSecurity())` toe net voor je `build` aanroept.
* In de methode waar je je POST methode test:
  * Voeg bij de methode de annotatie `@WithMockUser(username = "jos", password = "jos", roles = "USER")`
  toe. (Voor de URL/method in kwestie moeten we tenslotte ingelogd zijn)
  * Voeg `.with(csrf())` toe aan de `post` die je uitvoert
  * Met een _captor_ vang je nu ook de login-naam op die meegegeven wordt aan de `save`
  methode.
  * Verifieer (`assertThat`) of de opgevangen login-naam gelijk is aan "jos"
* Maak een nieuwe `@Test` methode **zonder** `@WithMockUser`
  * Doe een correcte POST naar `/new_entry`, met geldige parameters e.d.
  en eveneens met CSRF.
  * Je verwacht een response 3XX (redirection)
  * Je verwacht naar "/login" doorgestuurd te worden (Tip: `redirectedUrlPattern`)

**BlogEntryServiceTests**
* Je _mockt_ nu ook een `UserRepository`.
* Methode `testSaveBlogEntry`:
  * Je voorziet _stubbing_ voor je zelf-gedefinieerde methode van de klasse
  `UserRepository` (de methode om een gebruiker op basis van zijn/haar
  login-naam op te halen)
    * Met `given` leg je vast dat deze methode een zeker `User` object teruggeeft
    indien ze aangroepen wordt met een welbepaalde login-naam.
  * Bij de aanroep van de `save` methode geeft je dezelfde login-naam mee
  als parameter.
  * Verifier (`assertThat`) achteraf ook of de de `User` van de opgeslagen/opgevangen `BlogEntry`
  gelijk is aan jouw `User` object.
* Maak een nieuwe `@Test` methode genaamd `testSaveBlogEntryUserNotFound`. Deze methode
moet nagaan of `UsernameNotFoundException` gegooid wordt indien de `save` methode
wordt aangeroepen voor een onbestaande gebruiker.

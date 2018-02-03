Opdracht 1
==
Deel 1
--
Maak een **controller** aan genaamd `be.kdg.blog.controllers.ResponseBodyController` (niet de beste naam, maar deze controller zal op termijn vervangen worden).

* Zorg voor een `final` attribuut van het type `Blog` dat via de constructor meegegeven wordt. De verantwoordelijkheid voor het aanmaken van de blog ligt **niet** bij deze controller. Gebruik dus dependency injection bij de constructor.
    * De annotatie die je terugvindt bij de `Blog` klasse staat daar niet toevallig. Zie sectie 2.2.4 van het boek.

Je controller moet volgende functionaliteiten voorzien. Maak een methode voor beide functionaliteiten:

* GET op `/`
    * Je controller stuurt **plain text** terug naar de client. De tekst die je terug stuurt is "`Hello, world!`" (zonder quotes).
    * Zorg voor een correcte invulling van `Content-Type` in je HTTP header. Dit kan je makkelijk oplossen door de juiste parameter mee te geven aan de `GetMapping` annotatie.
    * Het resultaat dat je methode teruggeeft is de content die de client in de HTTP **body** zou moeten terugvinden. Hiervoor bestaat de annotatie ([ResponseBody](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/bind/annotation/ResponseBody.html)).
* GET op `/html`
    * Je controller stuurt **html** terug naar de client. De tekst die je terug stuurt is "`<html><head><title>Hello!</title></head><body><h1>Hello, world!</h1></body></html>`" (zonder quotes).
    * Zorg voor een correcte invulling van `Content-Type` in je HTTP header.
    * De content moet opnieuw in de HTTP body terecht komen.

Vul de test-klasse `ResponseBodyControllerTests` aan met volgende tests, één methode per test:

* Doe een GET request naar `/` en accepteer enkel **plain text**.
    * Print het resultaat af om makkelijk fouten op te sporen
    * Verwacht een HTTP status code 200 (OK)
    * Verwacht dat de inhoud ([content](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/test/web/servlet/result/MockMvcResultMatchers.html#content--)) van deze string het volgende is:  
      `Hello, world!`

* Doe een GET request naar `/html` en accepteer enkel **html**.
    * Print het resultaat af om makkelijk fouten op te sporen
    * Verwacht een HTTP status code 200 (OK)
    * Verwacht dat de inhoud van deze string volgende string **bevat** ([containsString](http://hamcrest.org/JavaHamcrest/javadoc/1.3/org/hamcrest/Matchers.html#containsString(java.lang.String))):  
      `<h1>Hello, world!</h1>`

__**Tip:**__ Voor opdracht 1 hoef je zelf **geen** mocking gedrag te bepalen. De methode waar dit wel nodig is is gegeven (`testApi`). Je komt dus toe met één lange call naar `this.mvc.perform`.

Deel 2
--
Vul je controller aan met volgende functionaliteit:

* GET op `/blog`
    * Je controller stuurt **html** terug naar de client.  
      Je overloopt de entries van de blog en hun tags en stelt een HTML string samen met o.a. enkele `h1`, `h2`, `p`, `ul`, `li` en `span` tags. Je kan je baseren op dit voorbeeld:  
      ![Screenshot HTML](/images/image1.png?raw=true "HTML voorstelling in Chrome")  
      De layout is **niet** belangrijk!
        * __**Tip:**__ `StringBuilder`
    * Zorg voor een correcte invulling van `Content-Type` in je HTTP header.
    * De content moet in de HTTP body terecht komen.

Deel 3
--
Vul je controller aan met volgende functionaliteit:

* GET op `/api/blog/entries/{entryId}`
    * Je controller stuurt **json** terug naar de client.  
      Het laatste deel van het pad (*entryId*) is een getal dat de ID van de gewenste blog entry voorstelt. De methode van je controller heeft een parameter nodig (ook met de naam *entryId*) die geannoteerd is met `PathVariable`.  
      Informatie over `PathVariable` kan je o.a. terugvinden in sectie 5.3.2 van het boek. De voorbeelden daar
      zijn niet uitgewerkt met `ResponseBody`, maar daarnaast heel toepasselijk.  
      Je gaat op zoek naar de juiste blog entry en stuurt deze terug in JSON formaat.  
      __**Tip:**__ gebruik [Gson](http://www.javadoc.io/doc/com.google.code.gson/gson/2.8.2) om het object om te zetten naar een string in JSON formaat.
    * Zorg voor een correcte invulling van `Content-Type` in je HTTP header.
    * De content moet in de HTTP body terecht komen.
    * Voer de test van methode `testApi` uit. Deze zou moeten slagen.

Extra
--
__**Request parameters**__

Naast het vastleggen van parameters in het pad van een URL kan je ook gebruik maken van *request parameters*. Request parameters, deel van de *[querystring](https://nl.wikipedia.org/wiki/Querystring)*, volgen in de URL na het pad, na een `?`.
* Probeer de entry ID mee te geven als request parameter m.b.v. de annotatie [RequestParam](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/web/bind/annotation/RequestParam.html), in plaats van via `PathVariable`. Zie ook sectie 5.3.1 van het boek.

__**ResponseEntity**__

Indien de client een onbestaande ID doorstuurt treedt er een HTTP error 500 op (die dan nog in HTML-vorm naar de client wordt verstuurd). Meer correct gedrag zou zijn om een 404 (not found) code terug te sturen.
* Wrap je teruggegeven resultaat-String in een [ResponseEntity](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/http/ResponseEntity.html) en gebruik code 404 indien de ID niet gevonden was.

# app_entwicklung_MFAX422A

# Einleitung: 
Ich habe die Benutzerfreundlichkeit und Anpassungsährtikeit durch meine erweiterung verbessert. Diese wären, die Settings erweitert, Es gibt jetzt mehrsprachen zur auswahl und die ChatGpt 
version lässt sich in den einstellung ändern. Zu dem gibt es die Möglichkeit die TTS aus oder an zu schalten. Der Reset button kann auch den TTS stoppen und haben ein neues Design.
	
# Anforderungen:
In den Einstellung unter den Reiter Sprache, soll es eine möglichkeit geben die Sprache über die Einstellung zu ändern, die standart Sprache ist die Gerätsprache. Die Einstellung werden direkt in 
selben Fenster übersetzt und ChatGpt und der Google Voice assistent der das gesprochende aufnimmt benutzen die ausgewählte sprache. Die Sprachen werden über eine Liste angzeigt und mit einem Radio Button ausgewählt. 
Die TTS soll über eine Checkbox an oder ausgestellt werden. Unter den Reiter API kann wie voher der API Key übergeben werden zu dem kann jetzt aber auch dort über ein Radio Button	die Version von ChatGpt geändert werden,
Standartgemäß ist ChatGpt4. Die Buttons haben ein Unicode als inhalt bekommen damit Sie damit sie verständlicher ist. Zu dem kann der Resetbutton die TTS stoppen. 


# Umsetzung:
## Spracheinstellungen:
1.Auswahl der Sprache:
  -Benutzer können nun in den Einstellungen eine Sprache auswählen.
  -Die Standard-Sprache entspricht der Gerätesprache.
  -Eine Liste von verfügbaren Sprachen wird angezeigt, und Benutzer können ihre Auswahl über Radio Buttons treffen.
  
2.Text-to-Speech (TTS):
  -Benutzer haben die Möglichkeit, die TTS-Funktion ein- oder auszuschalten.
  -Die TTS-Sprache wird ebenfalls basierend auf der ausgewählten Sprache festgelegt.
  
## ChatGpt-Version:
API-Version ändern:
  -Benutzer können in den Einstellungen die Version von ChatGpt über einen Radio Button auswählen.
  -Der API-Schlüssel kann weiterhin übergeben werden.
  
## Reset-Button:
Stoppen der TTS:
  -Der Reset-Button kann jetzt auch die TTS stoppen.

##UI-Elemente:
Unicode-Symbole:
  -Sie haben Unicode-Symbole für verschiedene Buttons hinzugefügt, um die Bedeutung klarer zu machen.

## Code-Snippets:
1. Initialisierung von ChatGpt:
  -Die ChatGpt-Klasse wird mit dem API-Token initialisiert.
2. Spracheinstellungen in der App:
  -Die App-Konfiguration wird auf die ausgewählte Sprache eingestellt.
  -Die Sprache wird auch für die Text-to-Speech-Funktion festgelegt.
3. Sprache ändern in den Einstellungen:
  -Die ausgewählte Sprache kann direkt in den Einstellungen geändert werden.
  -Bei Änderung wird die App neu gestartet, um die Spracheffekte zu übernehmen.
  
4. Einstellungen in der XML-Datei:
  -XML-Dateien werden aktualisiert, um die neuen Einstellungen und Symbole zu berücksichtigen.
  
5.Gradle-Dateien:
  -Die Versionsnummer der Android-Gradle-Plugin wurde auf 8.1.2 aktualisiert.

## API-Version in ChatGpt:
  -Die ChatGpt-Klasse wird mit einer bestimmten ChatGpt-Version initialisiert.

## Ressourcendateien:
  -Strings und Listen für verschiedene Sprachen und ChatGpt-Versionen wurden in den Ressourcendateien hinzugefügt.

## Einstellungen-Bildschirm:
  -Ein PreferenceScreen wurde erstellt, um Benutzern die Auswahl von Sprache, TTS und ChatGpt-Version zu ermöglichen.


# Probleme/Lessons learned:

Bei der Umsetzung meiner Erweiterungen stellte ich fest, dass ich weniger erreichen konnte als ursprünglich erhofft. Ich habe mir zu weniger Zeit für die Fehlersuche eingeplant hatte, 
und diese Suche sich als zeitaufwendiger herausstellte als erwartet. Ich habe dennnoch versucht so viele erweiterung wie möglich zu schreiben. Um die Benutzerfreundlichkeit zu verbessern
Es war jedoch klar, dass eine gründlichere Planung und Zeitallokation für die Fehlerbehebung in Zukunft notwendig sein wird, um ein reibungsloses Implementieren von Funktionen zu gewährleisten.
 
# Fazit:

Wie bereits bei Probleme/Lessone learned erwähnt. Habe ich viel zu viel Zeit verloren um Fehler zu beheben und konnte nicht so viel erweiterung implemntieren wie gehoft. 
Da man an manchen fehlern mehrere stunden dran gessessen hat. Diese Zeit habe ich mir nicht eingeplant. Denn noch habe ich es geschaft gute Erweiterung für die App zu schreiben. 

Wie schon im Abschnitt "Probleme/Lessons Learned" erwähnt, habe ich ziemlich viel Zeit verloren, um Fehler beheben und konnte. Das hat mir mein Zeitplan durcheinandergebracht, 
und ich konnte nicht so viele Erweiterungen in die App einbauen, wie ich gehofft hatte. Bei manche Fehler haben mich Stunden gekostet, dass hatte ich nicht eingeplant Aber 
trotzdem hab ich es  hingekriegt viele Erweiterung zu Implementieren. 

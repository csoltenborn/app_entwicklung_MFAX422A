# Einleitung

Mein Vorhaben umfasst die Integration einer Übersetzungsfunktion, die in der Lage ist, sowohl einzelne Wörter als auch vollständige Sätze oder umfangreichere Texte zu übersetzen. Zusätzlich besteht die Flexibilität, die Eingangssprache sowie die Ausgangssprache nach individuellem Bedarf anzupassen. Die herausragenden Übersetzungsfähigkeiten von ChatGPT machen diese Erweiterung zu einer besonders signifikanten Verbesserung.

# Umsetzung

## root_preferences.xml

In der Datei root_preferences.xml wurden die erforderlichen Einstellungen vorgenommen. Für die Eingangssprache wurden folgende Konfigurationen vorgenommen: 
**ListPreference für "language" (Sprache):**

- `defaultValue="de"`: Standardwert ist auf "de" (Deutsch) gesetzt.
- `entries="@array/language_entries"`: Die Auswahlmöglichkeiten (Anzeigewerte) für den Benutzer sind in einem Ressourcen-Array mit dem Namen "language_entries" festgelegt.
- `entryValues="@array/language_values"`: Die tatsächlichen Werte, die im Hintergrund verwendet werden, wenn der Benutzer eine Auswahl trifft, sind in einem Ressourcen-Array mit dem Namen "language_values" festgelegt.
- `key="language"`: Der Schlüssel, unter dem dieser Wert in den Einstellungen gespeichert wird.
- `title="@string/language"`: Der Titel oder die Bezeichnung, die dem Benutzer angezeigt wird.
- `useSimpleSummaryProvider="true"`: Eine vereinfachte Zusammenfassung wird verwendet, um den ausgewählten Wert anzuzeigen.

Für "Chaptgpt_language" sind die Änderungen hauptsächlich auf den Wechsel zu den entsprechenden Ressourcenarrays (`chatgpt_language_entries` und `chatgpt_language_values`) und den zugehörigen Zeichenkettenressourcen für Titel und Standardwert.

## getChatgptLanguage

Der Code ruft die ausgewählte ChatGPT-Sprache aus den Anwendungseinstellungen ab und verwendet dann einen Switch-Block, um verschiedene Beschreibungen für die Sprachauswahl zu generieren. Wenn die ausgewählte Sprache "German" ist, wird die Beschreibung "Übersetzt mir das auf deutsch:" zurückgegeben. Bei "English" lautet die Beschreibung "Translate it in English:", bei "Italian" "Translate the following to Italy:", und bei "French" "Translate the following to France:". Falls keine der vordefinierten Sprachen ausgewählt wurde, wird eine Laufzeit-Ausnahme mit dem Hinweis "Output Language not supported" ausgelöst. Der Standardwert für die Sprache ist "English".

```java
public String getChatgptLanguage() {  
String chatgpt_language = PreferenceManager.getDefaultSharedPreferences(context).getString  
            ("chatgpt_language", "English");  
    switch(chatgpt_language) {  
        case "German":  
            return "Übersetzt mir das auf deutsch:";  
        case "English":  
            return "Translate it in English:";  
        case "Italian":  
            return "Translate the following to italy:";  
        case "French":  
            return "Translate the following to france:";  
        default:  
            throw new RuntimeException("Output Language not supported: " + chatgpt_language);  
   }  
}
````

## getLocale

Der Code ruft die ausgewählte Benutzersprache aus den Anwendungseinstellungen ab und verwendet einen Switch-Block, um die entsprechende `Locale` für die Sprache zurückzugeben. Wenn die ausgewählte Sprache "de" ist, wird `Locale.GERMAN` zurückgegeben. Für "en" wird `Locale.ENGLISH` zurückgegeben, für "ita" wird `Locale.ITALIAN` zurückgegeben und für "franc" wird `Locale.FRANCE` zurückgegeben. Falls keine der vordefinierten Sprachen ausgewählt wurde, wird eine Laufzeit-Ausnahme mit dem Hinweis "Locale not supported" ausgelöst. Der Standardwert für die Sprache ist "de" (Deutsch).

```java
public Locale getLocale() {  
    String language = PreferenceManager.getDefaultSharedPreferences(context).getString("language", "de");  
    switch (language) {  
        case "de":  
           return Locale.GERMAN;  
        case "en":  
            return Locale.ENGLISH;  
        case "ita":  
            return Locale.ITALIAN;  
        case "franc":  
            return Locale.FRANCE;  
        default:  
            throw new RuntimeException("Local not supported: " + language);  
    }  
}
````

## getTextFromSpeech

In der Klasse MainFragment.java wird die Funktion getChatgptLanguage() in die Variable getTextFromSpeech implementiert.:
``chat.addMessage(new Message(Author.System, prefs.getChatgptLanguage()));``

# Problem

Ich stieß auf ein Problem, das ich glücklicherweise erfolgreich behoben habe. Ursprünglich musste man den Reset-Knopf verwenden, damit der Wechsel der Sprache für ChatGPT erkannt wurde. In der Methode createNewChat() hatte ich den folgenden Code:
``chat.addMessage(new Message(Author.System, prefs.getChatgptLanguage()));``
Dieser Code befand sich jedoch an der falschen Stelle und wurde dann direkt in die Variable getTextFromSpeech eingefügt, siehe oben.

# Fazit

Es ist mir gelungen, alles wie geplant umzusetzen. Zunächst dachte ich, dass ich das Problem nicht lösen könnte, aber zum Glück konnte ich es schließlich doch bewältigen. Der zeitliche Aufwand war im Grunde genommen optimal. Am Anfang hatte ich einige Schwierigkeiten, weil ich nicht genau wusste, wie ich beginnen sollte. Nachdem ich jedoch wusste, wo ich was tun musste, ging es recht schnell voran, und bis auf das beschriebene Problem hat alles reibungslos funktioniert.

# ChatGPT Projekt "Weather"
Meine Erweiterung für unsere ChatGPT-App ermöglicht es dem Nutzer, Echtzeit-Wetterinformationen abzurufen, indem er ChatGPT einfach nach dem Wetter fragt. Diese Informationen stammen von der OpenWeatherMap-API und umfassen eine Vielzahl von Wetterdaten wie Temperatur, Luftfeuchtigkeit, Windgeschwindigkeit und noch mehr. Die Integration dieser Funktion zielt darauf ab, das Benutzererlebnis zu verbessern und ChatGPT zu einem noch vielseitigeren Assistenten zu machen.

## Anforderungen
Die Erweiterung soll dem Nutzer ermöglichen, durch einfaches Fragen nach dem Wetter das aktuelle Wetter zu erfahren. ChatGPT soll die API-URL zurückgeben, die App fragt den API-Endpunkt an und übergibt die rohen JSON Wetter-Daten wieder an ChatGPT, um dem Nutzer eine formulierte Antwort zu liefern. Dabei sollen normale Anfragen an ChatGPT unverändert bleiben, nur Fragen nach dem Wetter sollen den API-Zugriff auslösen.

## Umsetzung
Ich habe das Message-Modell "Message" um einen MessageType erweitert, damit ein Präfix vor den Benutzerprompt gesetzt werden kann, wenn dieser ins ChatGPT-Message-Modell "ChatMessage" umgewandelt wird. Um Wetterdaten zu erhalten, habe ich eine WebAPI-Klasse erstellt, die den API-Endpunkt anfragt und bei Fehlern eine vordefinierte Fehlermeldung an den Nutzer zurückgibt. Der API-Schlüssel ist in der "root_preferences.xml" konfiguriert. Damit die Wetterdaten überhaupt angefragt werden, überprüfe ich im Mainfragment, ob ChatGPT's Antwort "api.openweathermap.org" enthält, und frage dann die API zusammen mit dem API-Key und der Einheit an. Die Wetterdaten im JSON-Format werden wiederum an ChatGPT gegeben, um dem Nutzer eine formulierte Antwort zu liefern. Die Anfragen benötigen etwas Zeit, daher wurde mir vorgeschlagen, einen Fortschrittsbalken in der "fragment_main.xml" zu konfigurieren. Hilfe dazu wurde mir im Branch "e19_progress_bar" zur Verfügung gestellt.

## Probleme/Lessons learned
Beim ersten Versuch mit Systemnachrichten fielen die Antworten inkonsistent aus, da ChatGPT häufig eigenständig auf Anfragen reagierte und sich kreativ das aktuelle Wetter ausdachte, anstatt die gewünschte API-URL zurückzugeben. Um dieses Problem zu beheben, habe ich mich dafür entschieden, in jedem Prompt Präfixe zu verwenden. Dadurch sollte klar und explizit kommuniziert werden, was genau von ChatGPT erwartet wird. Diese Lösung hat sich als effektiv erwiesen, da sie die Wahrscheinlichkeit verringert, dass ChatGPT eigenständig improvisierte Antworten liefert. Zusätzlich habe ich während des Prompt Engineerings festgestellt, dass das Verfassen des Prompts in englischer Sprache einen sehr großen Unterschied macht und ChatGPT die Anweisungen anscheinend besser versteht.

## Mögliche Zukünftige Verbesserungen
- Einheiten (metrisch oder imperial) in den Einstellungen oder in der Frage konfigurierbar machen
- Sprache von ChatGPT einstellen können
- Fortschrittsbalken optimieren
- Wetterdaten für die Zukunft oder Vergangenheit abfragen können
- Grundlegende Verbesserungen der App (TTS (de)aktivieren, TTS stoppen, etc.)

## Fazit
Die Entwicklung der Wettererweiterung für ChatGPT war spannend, und das Ziel, dem Nutzer mühelosen Zugriff auf Echtzeit-Wetterinformationen zu ermöglichen, wurde erreicht. Einige Features haben noch Verbesserungspotenzial. Das Prompt Engineering war zeitintensiver als erwartet, blieb jedoch im akzeptablen Rahmen. Insgesamt verlief der Entwicklungsprozess größtenteils innerhalb der erwarteten Zeitrahmen.

# ChatGPT App - Sicherheitsfeature Update

## Einleitung
In diesem Update haben ich mich auf die Verbesserung der Sicherheit und Benutzerfreundlichkeit der ChatGPT App konzentriert. Das Hauptziel war es, die App durch eine Passwortsicherung zu schützen.

### Verbesserungen
- **Passwortschutz:** Dieses Feature ermöglicht es den Benutzern, ihre Sitzungen durch ein individuelles Passwort zu sichern, was einen zusätzlichen Schutz gegen unbefugten Zugriff bietet.

## Anforderungen
Die Erweiterung zielt darauf ab, folgende Funktionen zu bieten:
- **Sicheres Login:** Ein robustes Login-System, das die Benutzerdaten durch ein Passwort schützt.
- **Modularität:** Ein Modul vorzubereiten um später ein Multi user system zu implementieren

## Umsetzung
### Programmierung
- **`LoginActivity`:** Eine neue Aktivität, die den Login-Prozess steuert. Hier wurde ein benutzerfreundliches Interface implementiert, das die Eingabe von Benutzername und Passwort ermöglicht.
- **`LoginDataSource`:** Ein neue Klasse, die eine anbindung an eine Datenbank/backend ermöglicht.
- **`LoggedInUser`:** Ein neue Klasse, die zum speichern des Users verwendet wird.

### Konfiguration
- **XML-Dateien:** Anpassungen in den XML-Dateien wurden vorgenommen, um das Layout und die Einstellungen der neuen Features zu unterstützen. Dies beinhaltet das Design der Login-Seite sowie die Struktur der Einstellungsmenüs.

## Probleme/Lessons Learned
### Herausforderungen
- **Zugriff auf App-Einstellungen:** Es gab Schwierigkeiten beim Implementieren der Einstellungen, insbesondere beim Speichern und Abrufen der Benutzerpräferenzen.
- **Startaktivität ändern:** Die Änderung der Startaktivität der App stellte eine Herausforderung dar, da dies eine tiefgreifende Anpassung des App-Flows erforderte.

### Lösungen und Erkenntnisse
- **Erkenntnisse:** Eine gründlichere Planung und Testphase hätte einige der Probleme verhindern können.

## Fazit
Das Projekt wurde nicht ganz erfolgreich umgesetzt. Der zeitliche Aufwand war größer als erwartet, aber das Endergebnis ist zufriedenstellend. Zukünftige Entwicklungen könnten die Implementierung von Firebase und zusätzliche Benutzereinstellungen umfassen.

## Zukünftige Entwicklungen
- **Firebase:** Für eine verbesserte Datenverwaltung und Authentifizierung.
- **Registrierungsmöglichkeit:** Ermöglicht es neuen Benutzern, eigene Konten zu erstellen.
- **Speichern von benutzerdefinierten Einstellungen:** Für eine personalisierte App-Erfahrung.

<!--
vim:foldmethod=marker 
-->

# _DistroTailor_

## Einleitung

### Überblick

_DistroTailor_ ist eine Erweiterung der bisher entwickelten 'Proof-Of-Concept' GPT-Client-App,
welche den Prozess der Linux-Installation für Anfänger vereinfacht.

### Motivation

Der Installationsprozess kann, gerade bei manuell konfigurierten Distros wie Arch,
Gentoo oder sogar DIY Distros wie LFS, für Anfänger eine große Herausforderung darstellen.
ChatGPT kann da recht zuverlässig helfen.
Der Prozess ist aber mit wichtigen Custom Instructions verbunden, welche nicht
immer einen Nutzen für andere Anliegen haben. _DistroTailor_ löst dieses Problem.

## Anforderungen

### Was soll _DistroTailor_ leisten?

Distro Tailor soll aufgrund von einfacher und verständlicher Konfiguration
in einem Konfigurationsmenü eine simpel formatierte, GPT-Markdown
unterstützende Linux-Installation ermöglichen.

Der User kann in der Konfiguration folgende Eigenschaften auswählen

- Distribution
- Kernel
- GPU Hersteller
- Display Server
- Desktop Environment
- Login Manager
- Window Manager
- Packages
- Firewall

_DistroTailor_ ist so konzipiert, dass Eigenschaften und
Konfigurationsmöglichkeiten, sowie der Befehl selbst,
einfach angepasst und aktualisiert werden können.

## Umsetzung
### UI/UX
- Settings angepasst:
    - API-Key und Sprache in seperates Untermenü (PreferenceScreen)
    - PreferenceCategories für alle benötigten Konfigurationsmöglichkeiten
    - Distro:
        - Auswahl für Custom Distro wenn in ListPref 'Custom' ausgewählt wird
    - Packages:
        - Multi Select List mit Custom Summary Logik
### Logik
- Speech to Text und Text to Speech entfernt
#### Methoden etc. (nur essentiell)
- getQueryInstructions()
    - (gibt allg. Custom Instructions zurück)
- getSelectedDistribution() 
    - (gibt Distribution, egal ob Standard oder Custom zurück)
- getSelectedValueFor() 
    - (gibt Inhalt für übergebenen key)
- getSelectedPackages() 
    - (gibt gewählte packages in string durch stringbuilder aus Set (Hash Map) zurück)
- displayResponseAsMarkdown() 
    - (Rendert Response als Markdown in TextView)
- applyLanguage()
- processQuery() 
    - (angepasst für usecase)
##### PrefsActivity
- onNavigateToScreen() 
    - (Logik für sub-PreferenceScreen)
- languagePref ChangeListener 
    - (wartet auf Änderungen der Language Pref)
- distributionPref ChangeListener 
    - (wartet auf Änderungen der Distro Pref, respektiert Custom Setting)
- packagesPref SummaryProvider 
    - (custom SummaryProvider für packages, konvertiert in Hash Map)
##### ChatGPT
- Model zu GPT-4 geändert
- Duration auf 600 Sekunden geändert 😡

### XML
- root_preferences wie oben beschrieben angepasst
- arrays.xml mit möglichen Werten für Settings gefüllt
- strings.xml mit **allen** Übersetzungen gefüllt

## Probleme / 'Lessons learned'
### Zu viele Features geplant
Ich habe mir zu viel vorgenommen.
- Jede Settings sollte ihren eigenen Enum bzw. eigene Klasse haben, um Type Safety und Future Proofing zu garantieren.
- User sollten Konfigurationen abspeichern und aufrufen können
- Selektion der Konfigurationen sollte per grid-like UI ermöglicht werden
### GPT Duration Setting
- Zu viel Zeit damit verschwendet, lange response times zu unterstützen
### UI
- Keine Zeit für Design Language, Dark Theme, Theme Switcher, Material You

## Fazit
Dieses Projekt war meine erste 'richtige', nutzvolle Android Native App. Ich habe versucht Boiler Plate Code zu vermeiden, bin letzten Endes aber doch in der Repetition-Hölle gelandet - glaube ich zumindest. 

Auf eine, nur bedingt verfügbare API mit sehr sensiblen Secret-Exposion Regeln, angewiesen zu sein, hat sich als sehr anstrengend in einer Entwicklungsumgebung entpuppt.

Im Großen und Ganzen bin ich aber zufrieden mit meiner Leistung und werde definitiv das nächste Projekt starten, sobal ich wieder Freizeit habe.

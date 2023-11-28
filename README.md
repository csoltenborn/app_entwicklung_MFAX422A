<!--
vim:foldmethod=marker 
-->

# Distro Tailor
<!-- {{{ Übersicht -->
## Anwendungsarchitektur - UML-Übersicht

### Klassen

#### MainActivity
- Erweitert: `AppCompatActivity`
- Methoden: `onCreate(Bundle)`, `onCreateOptionsMenu(Menu)`, `onOptionsItemSelected(MenuItem)`
- Assoziationen: Verbindungen zu `PrefsActivity` und `MainFragment`

#### MainFragment
- Erweitert: `Fragment`
- Attribute: `prefs`, `textToSpeech`, `chat`
- Methoden: `onCreateView(LayoutInflater, ViewGroup, Bundle)`, `onViewCreated(View, Bundle)`, `onPause()`, `onSaveInstanceState(Bundle)`, `onDestroy()`
- Assoziationen: Verwendet `PrefsFacade`, `TextToSpeechTool`, `Chat`, `LaunchSpeechRecognition`

#### PrefsActivity
- Erweitert: `AppCompatActivity`
- Innere Klasse: `SettingsFragment` (erweitert `PreferenceFragmentCompat`)
- Methoden: `onCreate(Bundle)`, `onOptionsItemSelected(MenuItem)`

#### PrefsFacade
- Attribute: `context`
- Methoden: `getApiToken()`
- Assoziationen: Wird von `MainFragment` verwendet

#### LaunchSpeechRecognition
- Erweitert: `ActivityResultContract`
- Innere Klasse: `SpeechRecognitionArgs`
- Methoden: `createIntent(Context, SpeechRecognitionArgs)`, `parseResult(int, Intent)`

#### TextToSpeechTool
- Implementiert: `TextToSpeech.OnInitListener`
- Attribute: `textToSpeech`, `ttsAvailable`, `locale`
- Methoden: `onInit(int)`, `speak(String)`, `stop()`, `destroy()`
- Assoziationen: Wird von `MainFragment` verwendet

#### ChatGpt
- Attribute: `apiToken`
- Methoden: `getChatCompletion(Chat)`
- Assoziationen: Verwendet `OpenAiService`, interagiert mit `Chat`

#### Author
- Typ: Aufzählung
- Werte: `User`, `Assistant`, `System`

#### Chat
- Implementiert: `Parcelable`
- Attribute: `messages` (Liste von `Message`)
- Methoden: `addMessage(Message)`, `getMessages()`

#### Message
- Implementiert: `Parcelable`
- Attribute: `date`, `author` (Author), `message` (String)
- Methoden: Konstruktoren, Parcelable-Methoden

### Assoziationen
- `MainActivity` navigiert zu `PrefsActivity` und `MainFragment`.
- `MainFragment` verwendet `PrefsFacade`, `TextToSpeechTool`, `Chat` und `LaunchSpeechRecognition`.
- `PrefsFacade` wird von `MainFragment` für die Verwaltung von Einstellungen verwendet.
- `TextToSpeechTool` wird von `MainFragment` für Text-zu-Sprache-Funktionalitäten verwendet.
- `ChatGpt` interagiert mit `Chat` zur Verarbeitung von Chatnachrichten.
<!-- }}} -->

## Einleitung
## Anforderungen
## Umsetzung
## Probleme / 'Lessons learned'
## Fazit

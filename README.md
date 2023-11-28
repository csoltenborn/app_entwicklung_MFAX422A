# Distro Tailor

## Application Architecture - UML Overview

### Classes

#### MainActivity
- Extends: `AppCompatActivity`
- Methods: `onCreate(Bundle)`, `onCreateOptionsMenu(Menu)`, `onOptionsItemSelected(MenuItem)`
- Associations: Links to `PrefsActivity` and `MainFragment`

#### MainFragment
- Extends: `Fragment`
- Attributes: `prefs`, `textToSpeech`, `chat`
- Methods: `onCreateView(LayoutInflater, ViewGroup, Bundle)`, `onViewCreated(View, Bundle)`, `onPause()`, `onSaveInstanceState(Bundle)`, `onDestroy()`
- Associations: Uses `PrefsFacade`, `TextToSpeechTool`, `Chat`, `LaunchSpeechRecognition`

#### PrefsActivity
- Extends: `AppCompatActivity`
- Inner Class: `SettingsFragment` (extends `PreferenceFragmentCompat`)
- Methods: `onCreate(Bundle)`, `onOptionsItemSelected(MenuItem)`

#### PrefsFacade
- Attributes: `context`
- Methods: `getApiToken()`
- Associations: Used by `MainFragment`

#### LaunchSpeechRecognition
- Extends: `ActivityResultContract`
- Inner Class: `SpeechRecognitionArgs`
- Methods: `createIntent(Context, SpeechRecognitionArgs)`, `parseResult(int, Intent)`

#### TextToSpeechTool
- Implements: `TextToSpeech.OnInitListener`
- Attributes: `textToSpeech`, `ttsAvailable`, `locale`
- Methods: `onInit(int)`, `speak(String)`, `stop()`, `destroy()`
- Associations: Used by `MainFragment`

#### ChatGpt
- Attributes: `apiToken`
- Methods: `getChatCompletion(Chat)`
- Associations: Uses `OpenAiService`, interacts with `Chat`

#### Author
- Type: Enumeration
- Values: `User`, `Assistant`, `System`

#### Chat
- Implements: `Parcelable`
- Attributes: `messages` (List of `Message`)
- Methods: `addMessage(Message)`, `getMessages()`

#### Message
- Implements: `Parcelable`
- Attributes: `date`, `author` (Author), `message` (String)
- Methods: Constructors, Parcelable methods

### Associations
- `MainActivity` navigates to `PrefsActivity` and `MainFragment`.
- `MainFragment` uses `PrefsFacade`, `TextToSpeechTool`, `Chat`, and `LaunchSpeechRecognition`.
- `PrefsFacade` is used by `MainFragment` for preference management.
- `TextToSpeechTool` is used by `MainFragment` for text-to-speech functionalities.
- `ChatGpt` interacts with `Chat` for processing chat messages.

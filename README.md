# _ChatGpt E-Mail generierung_

## Einleitung
Meine App-Erweiterung soll das Leben, mit dem Schreiben von beliebigen E-Mails, erleichtern. Sie kann im Alltag als Unterstützung benutzt werden. 
Via TextToSpeech kann man sein gewünschten Inhalt einreden und die Erweiterung wird einem viel Zeit beim Nachdenken und schreiben sparen.

## Anforderungen
### Was soll meine App-Erweiterung genau leisten?

Die Erweiterung generiert mit der Hilfe von ChatGpt, in Sekundenschnelle E-Mails. Nachdem die gewünschte E-Mail generiert wurde, wird der Inhalt ins E-Mail Programm übertragen.
Der Nutzer kann in den _Settings_ den gewünschten Empfänger sowie seinen eigenen Namen festlegen. 

## Umsetzung
### Konfigurationen:
- **`root_preferences.xml`**
Hier wurde der Umfang durch Empfänger (`emailTo`) und dem Namen (`emailAuthor`) erweitert. Sie dienen dem Nutzer, um seine E-Mails zu individualisieren.
### Programmierung:
### **MainFragment**
Um ein gleiches Muster beim generieren von E-Mails zu erhalten, wird mit Hilfe von System Nachrichten ein Format vorgegeben. Der Name wird mit `prefs.getEmailAuthor()` ausgelesen und mit geschickt an ChatGpt.
``` java
    public void createChat(){
        chat = new Chat();
        String msg =
                "Die erste Zeile der Email soll der Betreff sein. " +
                "Die Email darf max. 2-3 Zeilen lang sein." +
                "Die Email soll formal geschrieben sein. " +
                "Der Verfasser der Email bin ich:";

        chat.addMessage(new Message(Author.System,msg + prefs.getEmailAuthor()));
    }
 ```
 Nachdem wir die generierte E-Mail mit den **Anforderungen** erhalten haben, wird der Betreff und die Nachricht getrennt.
 Mit Hilfe vom `endIndex` (Zeilen Umbruch) und dem `startIndex` (Startet beim ":"), wird der Betreff (`subject`) und die Nachricht (`message`) separat aus der ChatGpt Nachricht extrahiert und übergeben.  
 ``` java
   int endIndex = answer.indexOf("\n");
   int startIndex = answer.indexOf(":") +2;
   if(endIndex < 0 || startIndex < 0){
    return;
   }

   String subject = answer.substring(startIndex,endIndex);
   String message = answer.substring(endIndex+2);
   SendEmail sendEmail = new SendEmail(prefs.getEmailTo(),subject,message);
   sendEmail.launchEmail(getContext());
```
### **SendEmail**
Damit man eine bessere Übersicht hat, habe ich noch eine neue Klasse erstellt: `SendEmail`.
Hier werden die Strings ( `email`, `subject` und `text`) über den Constructor der Klasse übergeben.
In dieser Klasse finden wir auch die Funktion:`launchEmail()`. Die Funktion erzeugt ein neues Intent mit dem Typen **_SENDTO_**. Dem Intent werden folgende Daten übergeben: `email`,`subject` und `text`.
Am Ende der Funktion wird das Intent mit der `startActivity` gestartet.
``` java 
 public class SendEmail {

    public String email;
    public String subject;
    public String text;

    public SendEmail(String email,String subject,String text){
        this.email = email;
        this.subject = subject;
        this.text = text;
    }
    public void launchEmail(Context context) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:"));
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(intent);
    }
}
```
Wenn das E-Mail Programm erfolgreich gestartet ist, sollten alle vorhandenen Felder erfolgreich befüllt sein. Wenn man keinen `namen` oder `emailTo` in den Einstellungen festgelegt hat, wird das Feld leer bleiben und der Name wird zu [DEIN NAME].

## Probleme
### Problem
Bei der Übergabe zum E-Mail Programm wurde die Empfänger-Email (`emailTo`) nicht übertragen und somit war das Feld leider leer.
### Problem-Lösung
Beim Übergeben der E-Mail muss die E-Mail ein Stringarray sein (`intent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});`).
Das hat den Übertragungsfehler direkt behoben.
Sonst hatte ich keine weiteren Probleme.

## Fazit
Die Erweiterung wurde erfolgreich umgesetzt. Die Umsetzung hat sehr viel Spaß gemacht und ich habe sehr viel neues gelernt.
Beim Umsetzen der Erweiterung habe ich mehr Ideen bekommen, die man bei Gelegenheit umsetzen könnte z.B:
- **Kontakte Implementieren**: So könnte man über TTS den Namen erfassen und darauf hin, es in den Kontakten überprüfen. Nachdem der Kontakt überprüft wurde, soll die E-Mail-Adresse automatisch zurückgegeben werden. Somit könnte man sich die `emailTo` in den Einstellungen sparen. 
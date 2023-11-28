# Einleitung (ImageToText)

Die jüngste Aktualisierung hat eine bedeutende Erweiterung der App hinzugefügt. Durch diese Verbesserung können Benutzer direkte **Bilder** von ihren mobilen Geräten auswählen. Die App kann den **Text aus ausgewählten Bildern** extrahieren und ihn dann reibungslos a **ChatGPT** weiterleiten. Die Erweiterung verbessert die App erheblich, indem sie Anwendern die Möglichkeit bietet, beispielsweise mathematische Aufgaben von einem Blatt auszulesen und sofort die Lösung zu erhalten.

# Anforderungen
Die neueste Erweiterung, die eingeführt wurde, ermöglicht es nun, **Bilder auszuwählen** und ChatGPT den enthaltenen Text zu senden. Die Benutzer können mit dieser Funktion Textinformationen aus Bildern extrahieren und eine Antwort von ChatGPT erhalten.

# Umsetzung
Mithilfe von **Firebase ML Vision** kann die Erweiterung Bilder aus dem Handy auswählen und den darin enthaltenen Text auslesen. Die Methode **getScanButton().setOnClickListener** startet die Bildauswahl-Intent.

Nachdem das Bild ausgewählt wurde, wird die Methode **processImage(Uri imageUri)** verwendet. In diesem Fall wird das ausgewählte Bild in eine **Bitmap** umgewandelt und der Text wird mit **Firebase ML Vision extrahiert**. Der extrahierte Text wird dann in der Chat-Ansicht als Benutzernachricht angezeigt.

Um eine Antwort zu erhalten, wird der extrahierte Text dann a ChatGPT weitergeleitet. Die Kommunikation mit ChatGPT wird durch eine API-Anfrage mit einem API ermöglicht. ChatGPT-Nachrichten mit den Antworten werden dann angezeigt und vorgelesen.

Zusätzlich zur genannten Funktionalität habe ich im **XML-Layout** des **MainFragment** einen **Button** erstellt. Dieser Button dient dazu, den Auswahlprozess für das Bild zu starten und somit die oben beschriebene Prozedur auszulösen. Darüber hinaus wurde ein **ImageView** hinzugefügt, um das ausgewählte Bild anzuzeigen. Dies bietet den Benutzern eine visuelle Rückmeldung über das ausgewählte Bild, bevor der Text extrahiert und verarbeitet wird. In Kombination mit dem Button ermöglicht dies eine benutzerfreundliche Interaktion mit der Bildverarbeitungsfunktion der App.

### Firebase
Um die App nutzen zu können, muss zunächst Firebase in der Anwendung aktiviert werden. Die Bilder werden in der Datenbank gespeichert.

####  Aktivierung von Firebase
1.  Öffnen Sie die **Toolbar**, die sich oben befindet.
2.  Wählen Sie **Tools**.
3.  Unter **Tools** wählen Sie **Firebase** aus.
4.  Es erscheint eine umfangreiche Liste.
5.  Suchen Sie nach **Firebase ML** und wählen Sie es aus.
6.  Innerhalb von **Firebase ML** wählen Sie **Use Firebase ML to recognize text in image [Java]**.
7.  Aktivieren Sie die Schritte **1. Connect your app to Firebase** und **3. Add Firebase ML to your app**.
8.  Fertig!


# Probleme

Da das geplante Ziel erfolgreich erreicht wurde, gab es keine Probleme. Die gewünschten Funktionen wurden reibungslos eingeführt, und alle notwendigen Aspekte, wie die Auswahl von Bildern, die Extrahierung von Texten und die Kommunikation mit ChatGPT, wurden erfolgreich integriert. Daher verlief das Projekt ohne große Schwierigkeiten und die gewünschten Features wurden erfolgreich umgesetzt.
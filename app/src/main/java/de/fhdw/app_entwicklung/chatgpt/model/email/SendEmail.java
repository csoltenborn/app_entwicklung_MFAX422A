package de.fhdw.app_entwicklung.chatgpt.model.email;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;


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

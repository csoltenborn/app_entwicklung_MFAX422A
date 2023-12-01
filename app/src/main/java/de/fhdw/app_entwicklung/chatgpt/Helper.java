package de.fhdw.app_entwicklung.chatgpt;

import java.util.Calendar;

public class Helper {

    public static int getDayOfWeek() {
        return  getDayOfWeek(-1);
    }

    public static int getDayOfWeek(long time) {
        final Calendar calendar = Calendar.getInstance();
        if (time >= 0)
            calendar.setTimeInMillis(time);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
        return (dayOfWeek + 5) % 7;
    }
}

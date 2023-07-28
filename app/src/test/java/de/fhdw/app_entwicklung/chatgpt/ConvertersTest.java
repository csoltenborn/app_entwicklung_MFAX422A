package de.fhdw.app_entwicklung.chatgpt;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import java.util.Date;

import de.fhdw.app_entwicklung.chatgpt.database.Converters;

/**
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ConvertersTest {

    @Test
    public void toDatetime_isCorrect() {
        //noinspection deprecation
        Date date = new Date(2023, 3, 24, 13, 27, 5);

        assertEquals(date.getTime(), Converters.dateToTimestamp(date).longValue());
        assertEquals(date, Converters.fromTimestamp(date.getTime()));

        long timestamp = Converters.dateToTimestamp(date);
        Date newDate = Converters.fromTimestamp(timestamp);
        assertEquals(date, newDate);
        assertEquals(timestamp, Converters.dateToTimestamp(newDate).longValue());
    }
}
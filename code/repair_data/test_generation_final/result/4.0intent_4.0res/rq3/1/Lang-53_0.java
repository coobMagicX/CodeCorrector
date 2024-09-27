import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    private static final int[][] fields = {
        {Calendar.MILLISECOND},
        {Calendar.SECOND},
        {Calendar.MINUTE},
        {Calendar.HOUR_OF_DAY, Calendar.HOUR},
        {Calendar.DATE, Calendar.DAY_OF_MONTH, Calendar.AM_PM},
        {Calendar.MONTH, DateUtils.SEMI_MONTH},
        {Calendar.YEAR},
        {Calendar.ERA}
    };

    public static final int SEMI_MONTH = 1001;

    private static void modify(Calendar val, int field, boolean round) {
        if (val.get(Calendar.YEAR) > 280000000) {
            throw new ArithmeticException("Calendar value too large for accurate calculations");
        }

        if (field == Calendar.MILLISECOND) {
            return;
        }

        Date date = val.getTime();
        long time = date.getTime();
        boolean done = false;

        // truncate milliseconds
        int millisecs = val.get(Calendar.MILLISECOND);
        if (!round || millisecs < 500) {
            time -= millisecs;
            if (field == Calendar.SECOND) {
                done = true;
            }
        }

        // truncate seconds
        int seconds = val.get(Calendar.SECOND);
        if (!done && (!round || seconds < 30)) {
            time -= seconds * 1000L;
            if (field == Calendar.MINUTE) {
                done = true;
            }
        }

        // truncate minutes
        int minutes = val.get(Calendar.MINUTE);
        if (!done && (!round || minutes < 30)) {
            time -= minutes * 60000L;
        }

        // reset time
        if (date.getTime() != time) {
            date.setTime(time);
            val.setTime(date);
        }

        boolean roundUp = false;
        for (int[] fieldGroup : fields) {
            for (int actualField : fieldGroup) {
                if (actualField == field) {
                    // This is our field... we stop looping
                    if (round && roundUp) {
                        if (field == SEMI_MONTH) {
                            if (val.get(Calendar.DATE) == 1) {
                                val.add(Calendar.DATE, 15);
                            } else {
                                val.add(Calendar.DATE, -15);
                                val.add(Calendar.MONTH, 1);
                            }
                        } else {
                            val.add(actualField, 1);
                        }
                    }
                    return;
                }
            }
        }
        throw new IllegalArgumentException("The field " + field + " is not supported");
    }
}
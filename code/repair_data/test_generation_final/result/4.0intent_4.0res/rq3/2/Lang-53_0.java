import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static final int[][] fields = {
        { Calendar.MILLISECOND },
        { Calendar.SECOND },
        { Calendar.MINUTE, Calendar.HOUR_OF_DAY, Calendar.HOUR },
        { Calendar.DATE, Calendar.DAY_OF_MONTH, Calendar.AM_PM },
        { Calendar.MONTH, Calendar.YEAR, Calendar.ERA }
    };

    private static final int SEMI_MONTH = 1001;

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
            time = time - millisecs;
            if (field == Calendar.SECOND) {
                done = true;
            }
        }

        // truncate seconds
        int seconds = val.get(Calendar.SECOND);
        if (!done && (!round || seconds < 30)) {
            time = time - (seconds * 1000L);
            if (field == Calendar.MINUTE) {
                done = true;
            }
        }

        // truncate minutes
        int minutes = val.get(Calendar.MINUTE);
        if (!done && (!round || minutes < 30)) {
            time = time - (minutes * 60000L);
        }

        // reset time
        if (date.getTime() != time) {
            date.setTime(time);
            val.setTime(date);
        }

        boolean roundUp = false;
        for (int[] fieldArray : fields) {
            for (int fieldElement : fieldArray) {
                if (fieldElement == field) {
                    if (round && roundUp) {
                        if (field == SEMI_MONTH) {
                            if (val.get(Calendar.DATE) == 1) {
                                val.add(Calendar.DATE, 15);
                            } else {
                                val.add(Calendar.DATE, -15);
                                val.add(Calendar.MONTH, 1);
                            }
                        } else {
                            val.add(field, 1);
                        }
                    }
                    return;
                }
            }
        }

        int offset = 0;
        boolean offsetSet = false;

        for (int[] fieldArray : fields) {
            if (fieldArray[0] == field) {
                switch (field) {
                    case SEMI_MONTH:
                        offset = val.get(Calendar.DATE) - 1;
                        if (offset >= 15) {
                            offset -= 15;
                        }
                        roundUp = offset > 7;
                        offsetSet = true;
                        break;
                    case Calendar.AM_PM:
                        offset = val.get(Calendar.HOUR_OF_DAY);
                        if (offset >= 12) {
                            offset -= 12;
                        }
                        roundUp = offset > 6;
                        offsetSet = true;
                        break;
                }
                if (!offsetSet) {
                    int min = val.getActualMinimum(fieldArray[0]);
                    int max = val.getActualMaximum(fieldArray[0]);
                    offset = val.get(fieldArray[0]) - min;
                    roundUp = offset > ((max - min) / 2);
                }
                if (offset != 0) {
                    val.set(fieldArray[0], val.get(fieldArray[0]) - offset);
                }
                return;
            }
        }
        throw new IllegalArgumentException("The field " + field + " is not supported");
    }
}
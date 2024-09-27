import java.util.Calendar;

public class DateUtils {
    public static final int SEMI_MONTH = 1001;

    private static final int[][] fields = {
            {Calendar.MILLISECOND},
            {Calendar.SECOND},
            {Calendar.MINUTE},
            {Calendar.HOUR_OF_DAY, Calendar.AM_PM},
            {Calendar.DATE, SEMI_MONTH},
            {Calendar.MONTH, Calendar.YEAR}
    };

    private static void modify(Calendar val, int field, boolean round) {
        if (val.get(Calendar.YEAR) > 280000000) {
            throw new ArithmeticException("Calendar value too large for accurate calculations");
        }

        // Manually truncate milliseconds, seconds, and minutes
        val.set(Calendar.MILLISECOND, 0);
        val.set(Calendar.SECOND, 0);
        val.set(Calendar.MINUTE, 0);
        val.set(Calendar.HOUR_OF_DAY, 0); // Reset time to midnight

        boolean roundUp = false;
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                if (fields[i][j] == field) {
                    if (round && roundUp) {
                        if (field == SEMI_MONTH) {
                            if (val.get(Calendar.DATE) == 1) {
                                val.add(Calendar.DATE, 15);
                            } else {
                                val.add(Calendar.DATE, -15);
                                val.add(Calendar.MONTH, 1);
                            }
                        } else {
                            val.add(fields[i][0], 1);
                        }
                    }
                    return;
                }
            }

            int offset = 0;
            boolean offsetSet = false;
            switch (field) {
                case SEMI_MONTH:
                    if (fields[i][0] == Calendar.DATE) {
                        offset = val.get(Calendar.DATE) - 1;
                        if (offset >= 15) {
                            offset -= 15;
                        }
                        roundUp = offset > 7;
                        offsetSet = true;
                    }
                    break;
                case Calendar.AM_PM:
                    if (fields[i][0] == Calendar.HOUR_OF_DAY) {
                        offset = val.get(Calendar.HOUR_OF_DAY);
                        if (offset >= 12) {
                            offset -= 12;
                        }
                        roundUp = offset > 6;
                        offsetSet = true;
                    }
                    break;
            }
            if (!offsetSet) {
                int min = val.getActualMinimum(fields[i][0]);
                int max = val.getActualMaximum(fields[i][0]);
                offset = val.get(fields[i][0]) - min;
                roundUp = offset > ((max - min) / 2);
            }
            val.set(fields[i][0], val.get(fields[i][0]) - offset);
        }
        throw new IllegalArgumentException("The field " + field + " is not supported");
    }
}
import java.util.Calendar;

public class DateUtils {
    public static final int SEMI_MONTH = 1001; // Custom field for semi-month handling

    private static final int[][] fields = {
        {Calendar.MILLISECOND},
        {Calendar.SECOND},
        {Calendar.MINUTE},
        {Calendar.HOUR_OF_DAY, Calendar.AM_PM},
        {Calendar.DATE, SEMI_MONTH},
        {Calendar.MONTH},
        {Calendar.YEAR}
    };

    public static void modify(Calendar val, int field, boolean round) {
        if (val.get(Calendar.YEAR) > 280000000) {
            throw new ArithmeticException("Calendar value too large for accurate calculations");
        }

        // ----------------- Fix for LANG-59 ---------------------- START ---------------
        // Manually truncate milliseconds, seconds and minutes, rather than using Calendar methods.
        val.set(Calendar.MILLISECOND, 0);
        val.set(Calendar.SECOND, 0);
        val.set(Calendar.MINUTE, 0);
        val.set(Calendar.HOUR_OF_DAY, 0); // Resetting the hour to start of the day
        // ----------------- Fix for LANG-59 ---------------------- END ----------------

        boolean roundUp = false;
        for (int[] fieldArray : fields) {
            for (int aField : fieldArray) {
                if (aField == field) {
                    if (round && roundUp) {
                        if (field == SEMI_MONTH) {
                            if (val.get(Calendar.DATE) == 1) {
                                val.add(Calendar.DATE, 15);
                            } else {
                                val.add(Calendar.DATE, -15);
                                val.add(Calendar.MONTH, 1);
                            }
                        } else {
                            val.add(aField, 1);
                        }
                    }
                    return;
                }
            }
            if (fieldArray[0] == field) {
                int offset = val.get(field) - val.getActualMinimum(field);
                if (field == SEMI_MONTH) {
                    offset = val.get(Calendar.DATE) - 1;
                    if (offset >= 15) {
                        offset -= 15;
                    }
                    roundUp = offset > 7;
                } else if (field == Calendar.AM_PM) {
                    offset = val.get(Calendar.HOUR_OF_DAY);
                    if (offset >= 12) {
                        offset -= 12;
                    }
                    roundUp = offset > 6;
                } else {
                    roundUp = offset > ((val.getActualMaximum(field) - val.getActualMinimum(field)) / 2);
                }
                val.set(field, val.get(field) - offset);
                return;
            }
        }
        throw new IllegalArgumentException("The field " + field + " is not supported");
    }
}
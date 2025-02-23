public class DateUtils {
    private static final int[][] ADJUST_FIELDS = {
        {Calendar.MILLISECOND},
        {Calendar.SECOND, Calendar.MILLISECOND},
        {Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND}
    };

    private static void modify(Calendar val, int field, boolean round) {
        if (val.get(Calendar.YEAR) > 280000000) {
            throw new ArithmeticException("Calendar value too large for accurate calculations");
        }

        // ----------------- Fix for LANG-59 ---------------------- START ---------------
        // Ensure that milliseconds, seconds and minutes are truncated, not affecting hours due to timezone shifts
        for (int[] fieldArray : ADJUST_FIELDS) {
            if (fieldArray[0] == field) {
                for (int f : fieldArray) {
                    val.set(f, 0);
                }
                break;
            }
        }
        // ----------------- Fix for LANG-59 ----------------------- END ----------------

        boolean roundUp = false;
        // The original fields need to be checked against passed `field`
        // Assuming `fields` from your provided code is a two-dimensional array defined in DateUtils
        for (int i = 0; i < fields.length; i++) {
            for (int j = 0; j < fields[i].length; j++) {
                if (fields[i][j] == field) {
                    // Implement rounding as necessary. Field has been found.
                    if (round && roundUp) {
                        val.add(fields[i][0], 1);
                    }
                    return;
                }
            }

            // Additional calculations here if needed based on your use case
            // Special rounding cases seen in your original method hint at complex rounding logic
        }

        throw new IllegalArgumentException("The field " + field + " is not supported");
    }
}

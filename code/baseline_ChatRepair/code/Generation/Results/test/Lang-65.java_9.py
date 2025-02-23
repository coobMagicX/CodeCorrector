import java.util.Calendar;
import java.util.TimeZone;

private static final int[][] fields = {
    {Calendar.MILLISECOND},
    {Calendar.SECOND},
    {Calendar.MINUTE},
    {Calendar.HOUR_OF_DAY, Calendar.AM_PM}, // Corrected field order and considering AM_PM
    {Calendar.DATE, Calendar.DAY_OF_MONTH, Calendar.AM_PM, Calendar.HOUR_OF_DAY},
    {Calendar.MONTH},
    {Calendar.YEAR},
    {Calendar.ERA}
};

private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // Preserve the original timezone of the Calendar
    TimeZone originalTimeZone = val.getTimeZone();

    // Clear time fields
    if (field == Calendar.MILLISECOND) {
        val.set(Calendar.MILLISECOND, 0);
    }
    if (field <= Calendar.SECOND) {
        val.set(Calendar.SECOND, 0);
    }
    if (field <= Calendar.MINUTE) {
        val.set(Calendar.MINUTE, 0);
    }
    if (field <= Calendar.HOUR_OF_DAY) {
        val.set(Calendar.HOUR_OF_DAY, 0);
    }

    boolean roundUp = false;
    for (int[] fieldGroup : fields) {
        for (int aField : fieldGroup) {
            if (aField == field) {
                if (round && roundUp) {
                    val.add(fieldGroup[0], 1);
                }
                return;
            }
        }
        // Determine if rounding up is necessary by comparing against midway points
        roundUp = val.get(fieldGroup[0]) > (val.getActualMaximum(fieldGroup[0]) / 2);
        val.set(fieldGroup[0], val.getActualMinimum(fieldGroup[0])); // Reset the field to its minimum
    }
    
    throw new IllegalArgumentException("The field " + field + " is not supported");
}

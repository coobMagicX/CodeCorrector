private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }
    
    // Set milliseconds to zero
    if (field <= Calendar.MILLISECOND) {
        val.set(Calendar.MILLISECOND, 0);
    }
    // Set seconds to zero
    if (field <= Calendar.SECOND) {
        val.set(Calendar.SECOND, 0);
    }
    // Set minutes to zero
    if (field <= Calendar.MINUTE) {
        val.set(Calendar.MINUTE, 0);
    }
    // Set hours to zero
    if (field <= Calendar.HOUR_OF_DAY) {
        val.set(Calendar.HOUR_OF_DAY, 0);
    }
    // Set date to first of the month
    if (field <= Calendar.DATE) {
        val.set(Calendar.DATE, 1);
    }
    // Set month to January
    if (field <= Calendar.MONTH) {
        val.set(Calendar.MONTH, 0);
    }

    // Adjustment for Daylight Saving Time or other timezone effects
    if (field <= Calendar.HOUR_OF_DAY) {
        TimeZone zone = val.getTimeZone();
        long timeInMillis = val.getTimeInMillis();
        int offsetBefore = zone.getOffset(timeInMillis);
        int offsetAfter = zone.getOffset(timeInMillis - offsetBefore);
        val.setTimeInMillis(timeInMillis - offsetBefore + offsetAfter);
    }
    
    // Rounding not necessary for truncating time fields, removed for correction

    // We do not handle years here; it's usually not truncated in typical scenarios
    // throw error if field larger than YEAR
    if (field > Calendar.YEAR) {
        throw new IllegalArgumentException("Field " + field + " is not supported for truncation");
    }
}

private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // Preserving the time zone to avoid unwanted effects during DST transition
    TimeZone originalTimeZone = val.getTimeZone();
    boolean originalTimeZoneDst = val.get(Calendar.DST_OFFSET) != 0;
    
    if (field == Calendar.MILLISECOND) {
        val.set(Calendar.MILLISECOND, 0);
    }
    if (field == Calendar.SECOND) {
        val.set(Calendar.SECOND, 0);
        val.set(Calendar.MILLISECOND, 0);
    }
    if (field == Calendar.MINUTE) {
        val.set(Calendar.MINUTE, 0);
        val.set(Calendar.SECOND, 0);
        val.set(Calendar.MILLISECOND, 0);
    }
    
    // Restore original time zone settings
    val.setTimeZone(originalTimeZone);
    if (originalTimeZoneDst) {
        val.set(Calendar.DST_OFFSET, 3600000);
    }

    boolean roundUp = false;
    for (int i = 0; i < fields.length; i++) {
        for (int j = 0; j < fields[i].length; j++) {
            if (fields[i][j] == field) {
                if (round && roundUp) {
                    // Additional rounding logic as necessary
                }
                return;
            }
        }
    }

    throw new IllegalArgumentException("The field " + field + " is not supported");
}

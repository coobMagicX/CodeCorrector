private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // Reset the smaller fields to zero to avoid time zone issues due to day light saving
    if (field == Calendar.HOUR_OF_DAY) {
        val.set(Calendar.MINUTE, 0);
        val.set(Calendar.SECOND, 0);
        val.set(Calendar.MILLISECOND, 0);
    } else if (field == Calendar.MINUTE) {
        val.set(Calendar.SECOND, 0);
        val.set(Calendar.MILLISECOND, 0);
    } else if (field == Calendar.SECOND) {
        val.set(Calendar.MILLISECOND, 0);
    }

    // Add your code for rounding if necessary using roundUp flag and arithmetic operations
    // Implementation continues...

    boolean roundUp = false;
    int[][] fields = {
        {Calendar.MILLISECOND},
        {Calendar.SECOND, Calendar.MILLISECOND},
        {Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND},
        // Add other necessary fields...
    };

    for (int[] fieldGroup : fields) {
        for (int fieldInGroup : fieldGroup) {
            if (fieldInGroup == field) {
                if (round && roundUp) {
                    // handle special case for semi-month and other cases as previously discussed
                    // implementation continues...
                }

                // leaving the loop once the field is addressed
                return;
            }
        }
        // Manage offset and rounding up
        // implementation continues...
    }
    throw new IllegalArgumentException("The field " + field + " is not supported");
}

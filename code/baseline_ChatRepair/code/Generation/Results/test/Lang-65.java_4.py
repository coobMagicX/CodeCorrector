private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // Manual truncation for milliseconds, seconds, and minutes:
    if (field == Calendar.MILLISECOND) {
        val.set(Calendar.MILLISECOND, 0);
    } else if (field == Calendar.SECOND) {
        val.set(Calendar.SECOND, 0);
        val.set(Calendar.MILLISECOND, 0);
    } else if (field == Calendar.MINUTE) {
        val.set(Calendar.MINUTE, 0);
        val.set(Calendar.SECOND, 0);
        val.set(Calendar.MILLISECOND, 0);
    }

    // Adjust additional fields based on 'round' parameter and truncation requirements
    boolean roundUp = false;
    for (int[] fieldGroup : fields) {
        for (int aField : fieldGroup) {
            if (aField == field) {
                // When the desired 'field' is found, check rounding condition
                if (round && roundUp) {
                    if (field == DateUtils.SEMI_MONTH) {
                        // Handling SEMI_MONTH rounding
                        if (val.get(Calendar.DATE) == 1) {
                            val.add(Calendar.DATE, 15);
                        } else {
                            val.add(Calendar.DATE, -15);
                            val.add(Calendar.MONTH, 1);
                        }
                    } else {
                        // General field rounding up
                        val.add(fieldGroup[0], 1);
                    }
                }
                return; // Exit once the appropriate action is taken
            }
        }
    }
    // Handle edge cases like AM_PM transitions
    handleComplexCases(val, field, round);

    // If no valid field is recognized, throw an exception
    throw new IllegalArgumentException("The field " + field + " is not supported");
}

// You can decide to implement this method `handleComplexCases` if you have special rules for AM_PM, etc.
private static void handleComplexCases(Calendar val, int field, boolean round) {
    // Implement specific case handling here based on your requirements.
}

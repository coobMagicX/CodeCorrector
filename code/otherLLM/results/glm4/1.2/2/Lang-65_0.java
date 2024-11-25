private static void modify(Calendar val, int field, boolean round) {
    if (val == null) {
        throw new IllegalArgumentException("The Calendar must not be null");
    }
    if (field < 0 || field > Calendar.FIELD_COUNT) {
        throw new IllegalArgumentException("The field value is out of range");
    }

    // Truncate to specific fields
    switch (field) {
        case Calendar.YEAR:
            val.set(Calendar.MONTH, Calendar.JANUARY);
            val.set(Calendar.DAY_OF_MONTH, 1);
            val.set(Calendar.HOUR_OF_DAY, 0);
            val.set(Calendar.MINUTE, 0);
            val.set(Calendar.SECOND, 0);
            val.set(Calendar.MILLISECOND, 0);
            break;
        case Calendar.MONTH:
            val.set(Calendar.DAY_OF_MONTH, 1);
            val.set(Calendar.HOUR_OF_DAY, 0);
            val.set(Calendar.MINUTE, 0);
            val.set(Calendar.SECOND, 0);
            val.set(Calendar.MILLISECOND, 0);
            break;
        case Calendar.DATE:
            val.set(Calendar.HOUR_OF_DAY, 0);
            val.set(Calendar.MINUTE, 0);
            val.set(Calendar.SECOND, 0);
            val.set(Calendar.MILLISECOND, 0);
            break;
        case Calendar.HOUR:
        case Calendar.HOUR_OF_DAY:
            val.set(Calendar.MINUTE, 0);
            val.set(Calendar.SECOND, 0);
            val.set(Calendar.MILLISECOND, 0);
            break;
        case Calendar.MINUTE:
            val.set(Calendar.SECOND, 0);
            val.set(Calendar.MILLISECOND, 0);
            break;
        case Calendar.SECOND:
            val.set(Calendar.MILLISECOND, 0);
            break;
        case Calendar.MILLISECOND:
            break;
        // Add other fields as necessary
        default:
            throw new IllegalArgumentException("The field " + field + " is not supported");
    }

    // Handle round-up logic for SEMI_MONTH and other special cases if necessary
    if (round && field == DateUtils.SEMI_MONTH) {
        int date = val.get(Calendar.DATE);
        if (date == 1) {
            val.add(Calendar.MONTH, 1); // Set to the first day of the next month
        } else {
            val.set(Calendar.DATE, 16); // Set to the 16th day of this month
        }
    }
}
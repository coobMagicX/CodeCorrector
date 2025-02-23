private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // Truncate milliseconds
    if (field == Calendar.SECOND || field == Calendar.MINUTE || field == Calendar.HOUR ||
        field == DateUtils.SEMI_MONTH) {
        val.set(Calendar.MILLISECOND, 0);
    }

    // Truncate seconds
    if (field == Calendar.MINUTE || field == Calendar.HOUR || field == DateUtils.SEMI_MONTH) {
        val.set(Calendar.SECOND, 0);
    }

    // Truncate minutes
    if (field == Calendar.HOUR || field == DateUtils.SEMI_MONTH) {
        val.set(Calendar.MINUTE, 0);
    }

    // If needing to truncate to SEMI_MONTH check and modify the DAY_OF_MONTH accordingly
    if (field == DateUtils.SEMI_MONTH) {
        int dayOfMonth = val.get(Calendar.DAY_OF_MONTH);
        if (dayOfMonth >= 16) {
            val.set(Calendar.DAY_OF_MONTH, 16);
        } else {
            val.set(Calendar.DAY_OF_MONTH, 1);
        }
    }

    // Handle the reset of the hours if needed
    if (field == DateUtils.SEMI_MONTH) {
        val.set(Calendar.HOUR_OF_DAY, 0);
    }
    
    // Additional fields logic remains here with fields, roundUp logic ...
}

// Your previous code with fields, roundUp handling, and special case rounding logic is unchanged.

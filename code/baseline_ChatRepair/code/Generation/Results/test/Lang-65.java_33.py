private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // Reset the less significant time fields based on the provided field
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

    // Determine whether to round up, and adjust the field accordingly if rounding is enabled
    if (field == Calendar.MILLISECOND || field == Calendar.SECOND || field == Calendar.MINUTE) {
        int fieldValue = val.get(field);
        int min = val.getActualMinimum(field);
        int max = val.getActualMaximum(field);
        int offset = fieldValue - min;
        boolean roundUp = offset > ((max - min) / 2);
        if (round && roundUp && field != Calendar.MILLISECOND) {
            // Increment the field if rounding up, but not for milliseconds as they are already set to zero
            val.add(field, 1);
        }
    } else {
        throw new IllegalArgumentException("The field " + field + " is not supported for truncation");
    }
}

private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // Set milliseconds to zero
    if (val.get(Calendar.MILLISECOND) != 0) {
        val.set(Calendar.MILLISECOND, 0);
    }

    if (field == Calendar.MILLISECOND) {
        // Only truncate to milliseconds
        return;
    }

    // Set seconds and any lesser component to zero
    if (val.get(Calendar.SECOND) != 0) {
        val.set(Calendar.SECOND, 0);
    }

    if (field == Calendar.SECOND) {
        return;
    }

    // Set minutes and any lesser component to zero
    if (val.get(Calendar.MINUTE) != 0) {
        val.set(Calendar.MINUTE, 0);
    }

    if (field == Calendar.MINUTE) {
        return;
    }

    // Further adjustments for larger fields as necessary; the above handles up to minute precision
    // Truncate other fields based on your specifications and `field` parameter
}

private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // Check if field corresponds to milliseconds, seconds, or minutes and reset them
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

    boolean roundUp = false;
    switch (field) {
        case Calendar.SECOND:
        case Calendar.MINUTE:
            int fieldValue = val.get(field);
            int min = val.getActualMinimum(field);
            int max = val.getActualMaximum(field);
            int offset = fieldValue - min;
            roundUp = offset > ((max - min) / 2);
            if (round && roundUp) {
                val.add(field, 1);
            }
            break;
        default:
            throw new IllegalArgumentException("Field " + field + " not handled properly");
    }
}

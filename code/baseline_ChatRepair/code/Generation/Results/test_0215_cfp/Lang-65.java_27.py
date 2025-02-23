private static void modify(Calendar val, int field, boolean round) {
    if (val.get(Calendar.YEAR) > 280000000) {
        throw new ArithmeticException("Calendar value too large for accurate calculations");
    }

    // ----------------- Fix for LANG-59 ---------------------- START ---------------
    if (val.getActualMinimum(Calendar.MILLISECOND) != val.getActualMaximum(Calendar.MILLISECOND)) {
        val.set(Calendar.MILLISECOND, 0);
    }
    if (field <= Calendar.SECOND && val.getActualMinimum(Calendar.SECOND) != val.getActualMaximum(Calendar.SECOND)) {
        val.set(Calendar.SECOND, 0);
    }
    if (field <= Calendar.MINUTE && val.getActualMinimum(Calendar.MINUTE) != val.getActualMaximum(Calendar.MINUTE)) {
        val.set(Calendar.MINUTE, 0);
    }
    // Note: further handling for other fields such as HOUR should be added if necessary.
    // ----------------- Fix for LANG-59 ----------------------- END ----------------

    boolean roundUp = false;
    // Check and adjust the field per original code (omitted for brevity).

    // Implementation of rounding logic per original code (omitted for brevity).
}

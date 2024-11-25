public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (mTimeZoneForced) {
        calendar = (Calendar) calendar.clone();
        int offset = calculateOffset(calendar.getTimeInMillis());
        calendar.add(Calendar.MILLISECOND, offset);
        calendar.setTimeZone(mTimeZone);
    }
    return applyRules(calendar, buf);
}

private int calculateOffset(long millis) {
    // Assuming this method is already implemented
    // to calculate the time zone offset in milliseconds
}
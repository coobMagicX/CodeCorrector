protected BasePeriod(long duration) {
    this(duration, getTimeZone(), getCalendar().getTimeInMillis());
}
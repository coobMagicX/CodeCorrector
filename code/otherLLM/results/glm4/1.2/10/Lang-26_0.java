public String format(Date date) {
    Calendar c = new GregorianCalendar(mTimeZone);
    c.setTime(date);

    // Create an instance of FastDateFormat with the Swedish locale and pattern "week 'Week' w"
    FastDateFormat format = getInstance("week 'Week' w", mTimeZone, Locale.SWEDISH);

    return format.format(c);
}
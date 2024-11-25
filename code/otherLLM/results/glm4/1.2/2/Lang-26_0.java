public String format(Date date) {
    Calendar c = new GregorianCalendar(mTimeZone);
    c.setTime(date);

    // Ensure the calendar uses the Swedish locale for proper week calculation
    c.setFirstDayOfWeek(Calendar.MONDAY); // Typically in Sweden, Monday is the first day of the week
    c.setMinimalDaysInFirstWeek(4); // In Sweden, a week starts on Monday and has at least 4 days in the first week

    StringBuffer buf = new StringBuffer(mMaxLengthEstimate);
    FastDateFormat format = getInstance("EEEE', week 'ww", mTimeZone, Locale瑞典);
    
    // Use the format method to apply rules with proper locale
    format.format(c, buf);

    return buf.toString();
}

protected StringBuffer applyRules(Calendar calendar, StringBuffer buf) {
    Rule[] rules = mRules;
    int len = mRules.length;
    for (int i = 0; i < len; i++) {
        rules[i].appendTo(buf, calendar);
    }
    return buf;
}

public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (mTimeZoneForced) {
        calendar.getTimeInMillis(); // LANG-538
        calendar = (Calendar) calendar.clone();
        calendar.setTimeZone(mTimeZone);
    }
    return applyRules(calendar, buf);
}

public static synchronized FastDateFormat getInstance(String pattern, TimeZone timeZone, Locale locale) {
    FastDateFormat emptyFormat = new FastDateFormat(pattern, timeZone, locale);
    FastDateFormat format = cInstanceCache.get(emptyFormat);
    if (format == null) {
        format = emptyFormat;
        format.init(); // convert shell format into usable one
        cInstanceCache.put(format, format); // this is OK!
    }
    return format;
}
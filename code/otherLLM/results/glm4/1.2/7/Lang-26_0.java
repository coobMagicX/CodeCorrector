public String format(Date date) {
    Calendar c = new GregorianCalendar(mTimeZone);
    c.setTime(date);
    
    // Ensure the calendar uses the Swedish locale and is in the correct time zone if forced.
    if (mTimeZoneForced) {
        c.setTimeZone(mTimeZone);
    }

    return applyRules(c, new StringBuffer(mMaxLengthEstimate)).toString();
}
public StringBuffer format(Calendar calendar, StringBuffer buf) {
    String timeZoneStr = getTZString(calendar.getTimeZone());
    if (mTimeZoneForced && !timeZoneStr.equals(mTimeZone.getID())) {
        calendar = (Calendar) calendar.clone();
        calendar.setTimeZone(mTimeZone);
    }
    return applyRules(calendar, buf);
}

// Assuming this method is available in the context
public String getTZString(TimeZone timeZone) {
    // Return the time zone string as a string
}
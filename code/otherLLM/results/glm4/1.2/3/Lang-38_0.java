public StringBuffer format(Calendar calendar, StringBuffer buf) {
    Calendar originalCalendar = null;

    if (mTimeZoneForced) {
        // Clone the Calendar instance to avoid modifying the original object that may be used elsewhere.
        originalCalendar = (Calendar) calendar.clone();
        // Set the time zone for the cloned Calendar instance.
        originalCalendar.setTimeZone(mTimeZone);
    }

    // If mTimeZoneForced is true, use the cloned and modified Calendar; otherwise, use the original one.
    Calendar toUse = (mTimeZoneForced && originalCalendar != null) ? originalCalendar : calendar;

    return applyRules(toUse, buf);
}
public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (mTimeZoneForced) {
        // Safely clone the calendar to avoid side effects on the original calendar instance
        Calendar clonedCalendar = (Calendar) calendar.clone();
        clonedCalendar.setTimeZone(mTimeZone);  // Apply the forced timezone to the cloned calendar
        buf = applyRules(clonedCalendar, buf);  // Format the cloned calendar
    } else {
        buf = applyRules(calendar, buf);  // Format the original calendar
    }
    return buf;
}
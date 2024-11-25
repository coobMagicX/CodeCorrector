public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (mTimeZoneForced) {
        // Create a clone of the original calendar to avoid modifying it
        Calendar forcedCalendar = (Calendar) calendar.clone();
        
        // Set the time zone for the cloned calendar
        forcedCalendar.setTimeZone(mTimeZone);
        
        // Use the cloned calendar with the applied rules
        return applyRules(forcedCalendar, buf);
    } else {
        // If mTimeZoneForced is false, use the original calendar and its rules
        return applyRules(calendar, buf);
    }
}
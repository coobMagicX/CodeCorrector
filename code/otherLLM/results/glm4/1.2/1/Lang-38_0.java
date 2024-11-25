public StringBuffer format(Calendar calendar, StringBuffer buf) {
    // Check if time zone should be overridden by the user's setting
    if (getTimeZoneOverridesCalendar()) {
        Calendar clonedCalendar = (Calendar) calendar.clone();
        clonedCalendar.setTimeZone(mTimeZone);
        
        // Update the original calendar with the new time zone
        calendar = clonedCalendar;
    }
    
    // Now that any changes to the calendar's time zone are captured, proceed with formatting
    return applyRules(calendar, buf);
}
public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (getTimeZoneOverridesCalendar()) {
        Calendar forcedCalendar = (Calendar) calendar.clone();
        forcedCalendar.setTimeZone(mTimeZone);
        
        // Apply formatting rules based on the time zone of the forced calendar
        applyRules(forcedCalendar, buf);
    } else {
        // Apply formatting rules based on the original calendar's time zone
        applyRules(calendar, buf);
    }
    
    return buf;
}
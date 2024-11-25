public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (getTimeZoneOverridesCalendar()) {
        Calendar clonedCalendar = (Calendar) calendar.clone();
        clonedCalendar.setTimeZone(mTimeZone);
        calendar = clonedCalendar; // Assign the modified clone back to the original calendar instance.
    }
    
    return applyRules(calendar, buf);
}
public StringBuffer format(Calendar calendar, StringBuffer buf) {
    Calendar workingCalendar = calendar;
    if (mTimeZoneForced) {
        workingCalendar = (Calendar) calendar.clone(); // Assign cloned calendar to a new variable to preserve original calendar
        workingCalendar.setTimeZone(mTimeZone); // Correcting the timezone to the forced one on the working copy
    }
    return applyRules(workingCalendar, buf); // Apply formatting rules to the working calendar
}
public StringBuffer format(Calendar calendar, StringBuffer buf) {
    // Check if time zone needs to be forced and if not already set to the correct one
    if (mTimeZoneForced && calendar.getTimeZone() != mTimeZone) {
        // Clone the calendar to avoid changing the original instance
        calendar = (Calendar) calendar.clone();
        // Set the required time zone on the cloned calendar object
        calendar.setTimeZone(mTimeZone);
    }
    
    // Apply formatting rules after ensuring the correct time zone is used
    return applyRules(calendar, buf);
}
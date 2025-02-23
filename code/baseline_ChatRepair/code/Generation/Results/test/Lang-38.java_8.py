public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (mTimeZoneForced) {
        // Clone the calendar to avoid changing the original calendar
        calendar = (Calendar) calendar.clone();
        // Set the forced timezone onto the cloned calendar
        calendar.setTimeZone(mTimeZone);
        
        // Adjust to account for any potential discrepancy between time zones
        // by recalculating the calendar's time fields
        calendar.setTimeInMillis(calendar.getTimeInMillis());
    }
    
     // Apply formatting rules to the possibly modified calendar object
    return applyRules(calendar, buf);
}

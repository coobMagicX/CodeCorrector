public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (mTimeZoneForced) {
        // Clone the Calendar object to avoid modifying the original instance.
        Calendar clone = (Calendar) calendar.clone();
        
        // Update the timezone of the cloned Calendar object to match the expected GMT-8 timezone offset.
        clone.setTimeZone(TimeZone.getTimeZone("GMT-08:00"));
        
        // Now, use the updated cloned Calendar object for formatting date-time string.
        return applyRules(clone, buf);
    } else {
        return applyRules(calendar, buf);
    }
}
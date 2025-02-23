public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (calendar == null) {
        throw new IllegalArgumentException("Calendar object cannot be null.");
    }
    
    // Clone and adjust to avoid modifying the original calendar object
    if (mTimeZoneForced) {
        calendar = (Calendar) calendar.clone();
       
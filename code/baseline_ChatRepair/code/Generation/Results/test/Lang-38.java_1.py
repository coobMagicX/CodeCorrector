public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (calendar == null || buf == null) {
        throw new IllegalArgumentException("The calendar and buffer must not be null.");
    }
    
    // Clone the original Calendar to avoid modifying it
    Calendar calendarCopy = (Calendar) calendar.clone();

    // Force the timezone if it is set
    if (mTimeZoneForced) {
        calendarCopy.setTime
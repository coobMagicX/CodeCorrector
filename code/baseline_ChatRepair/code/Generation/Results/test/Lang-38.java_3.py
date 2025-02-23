public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (mTimeZoneForced) {
        if (calendar == null) {
            throw new IllegalArgumentException("Calendar object cannot be null");
        }
        calendar = (Calendar) calendar.clone();
        // Ensuring the
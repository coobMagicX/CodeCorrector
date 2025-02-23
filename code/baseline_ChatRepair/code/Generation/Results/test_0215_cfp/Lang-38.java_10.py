public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (calendar == null) {
        throw new IllegalArgumentException("The calendar must not be null");
    }

    // Clone and adjust timezone only if required
    if (mTimeZoneForced) {
        calendar = (Calendar) calendar.clone();
        calendar.setTimeZone(mTimeZone); // Ensure time zone is
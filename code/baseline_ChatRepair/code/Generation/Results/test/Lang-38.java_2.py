public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (calendar == null || buf == null) {
        throw new IllegalArgumentException("The calendar and buffer must not be null.");
    }

    // Clone the original Calendar to avoid modifying the original instance
    Calendar adjustedCalendar = (Calendar) calendar.clone();

    // If a specific timezone is forced, we change the calendar to this timezone
    if (mTimeZoneForced) {
        adjustedCalendar.setTimeZone(mTimeZone);
    }

    // Apply the formatting rules to the adjusted calendar
    return applyRules(adjustedCalendar, buf
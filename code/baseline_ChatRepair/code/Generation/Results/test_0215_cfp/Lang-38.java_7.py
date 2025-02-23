public StringBuffer format(Calendar calendar, StringBuffer buf) {
    Calendar adjustedCalendar = calendar;
    if (mTimeZoneForced) {
        // Clone the original calendar to avoid modifications on the original object
        adjustedCalendar = (Calendar) calendar.clone();
        // Set the new time zone
        adjustedCalendar.setTimeZone(mTimeZone);

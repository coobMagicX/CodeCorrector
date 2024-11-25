public StringBuffer format(Calendar calendar, StringBuffer buf) {
    // Clone the Calendar object to avoid modifying the original instance
    Calendar calendarToFormat = (calendar == null ? Calendar.getInstance() : (Calendar) calendar.clone());

    // Apply rules if timeZoneForced is true, otherwise use the provided calendar's time zone
    if (mTimeZoneForced) {
        calendarToFormat.setTimeZone(mTimeZone);
    }

    // Format the Calendar object into a StringBuffer using the appropriate pattern and time zone
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    
    // Check if timeZoneForced is true, if so, ensure the formatted date-time string reflects this forced time zone
    if (mTimeZoneForced) {
        dateFormat.setTimeZone(mTimeZone);
    }

    // Append the formatted date to the buffer
    buf.append(dateFormat.format(calendarToFormat));

    return buf;
}
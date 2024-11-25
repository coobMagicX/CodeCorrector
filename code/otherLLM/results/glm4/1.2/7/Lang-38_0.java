public StringBuffer format(Calendar calendar, StringBuffer buf) {
    // Clone the Calendar to avoid modifying the original object.
    Calendar clonedCalendar = (Calendar) calendar.clone();

    if (mTimeZoneForced) {
        // If timeZone is forced, set the clone's time zone to the forced one.
        clonedCalendar.setTimeZone(mTimeZone);
    }

    // Define the ISO 8601 pattern.
    String isoPattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    
    // Create a SimpleDateFormat instance using the ISO pattern and set the calendar.
    SimpleDateFormat dateFormat = new SimpleDateFormat(isoPattern);
    dateFormat.setCalendar(clonedCalendar);

    // Append the formatted date to the buffer.
    buf.append(dateFormat.format(clonedCalendar));

    return buf;
}
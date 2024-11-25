public StringBuffer format(Calendar calendar, StringBuffer buf) {
    // Clone the Calendar to ensure thread safety and avoid modification of the input Calendar instance.
    if (mTimeZoneForced) {
        calendar = (Calendar) calendar.clone();
        // Explicitly set the TimeZone to GMT for this particular scenario.
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
    }
    // Check if applyRules returns null, which would indicate a problem with formatting.
    String formattedString = applyRules(calendar, buf);
    if (formattedString == null) {
        throw new IllegalStateException("Failed to format the date and time using applyRules method.");
    }
    // Append the result from applyRules to the buffer.
    buf.append(formattedString);
    return buf;
}
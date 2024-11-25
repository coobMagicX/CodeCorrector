public StringBuffer format(Calendar calendar, StringBuffer buf) {
    Rule[] rules = mRules;
    int len = mRules.length;
    calendar.setTimeZone(TimeZone.getTimeZone("GMT-8")); // Apply GMT-8 time zone for formatting
    for (int i = 0; i < len; i++) { // Apply formatting rules directly to the original calendar object
        rules[i].appendTo(buf, calendar);
    }
    return buf;
}
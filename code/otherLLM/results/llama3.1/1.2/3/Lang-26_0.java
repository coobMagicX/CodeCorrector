public String format(Date date) {
    Calendar c = new GregorianCalendar(mTimeZone);
    c.setTime(date);

    StringBuffer buf = new StringBuffer(mMaxLengthEstimate);
    applyRules(c, buf); // Call the existing method to populate the buffer
    return buf.toString(); // Format the string directly from the populated buffer
}

protected void applyRules(Calendar calendar, StringBuffer buf) {
    Rule[] rules = mRules;
    int len = mRules.length;
    for (int i = 0; i < len; i++) {
        rules[i].appendTo(buf, calendar);
    }
}
public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (mTimeZoneForced) {
        calendar = (Calendar) calendar.clone();
        calendar.setTimeZone(mTimeZone);
    }
    return applyRules(calendar, buf);
}

protected StringBuffer applyRules(Calendar calendar, StringBuffer buf) {
    Rule[] rules = mRules;
    int len = mRules.length;
    for (int i = 0; i < len; i++) {
        rules[i].appendTo(buf, calendar);
    }
    return buf;
}
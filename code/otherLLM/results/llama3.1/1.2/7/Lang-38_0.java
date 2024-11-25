public StringBuffer format(Calendar calendar, StringBuffer buf) {
    if (mTimeZoneForced && calendar.getTimeZone().getID() != mTimeZone.getID()) {
        calendar = (Calendar) calendar.clone();
        calendar.setTimeZone(mTimeZone);
    } else if (mTimeZoneForced) {
        // apply timezone conversion rules here
        calendar.add(Calendar.HOUR_OF_DAY, -8);  // assuming GMT-8 offset
    }
    return applyRules(calendar, buf);
}
public String format(Date date) {
    Calendar c = new GregorianCalendar();
    if (mTimeZoneForced) {
        c.setTimeZone(mTimeZone);
    }
    c.setTime(date);
    return format(c, new StringBuffer(mMaxLengthEstimate)).toString();
}
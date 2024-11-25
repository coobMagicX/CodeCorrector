public String format(Date date) {
    Calendar c = new GregorianCalendar(mTimeZone);
    c.setTime(date);
    
    if (mLocale != null) { // added locale handling
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatString, mLocale);
        return simpleDateFormat.format(c.getTime());
    } else {
        return applyRules(c, new StringBuffer(mMaxLengthEstimate)).toString();
    }
}
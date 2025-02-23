public String format(Date date) {
    Calendar c = new GregorianCalendar(mTimeZone);
    // Set the minimum days in the first week (common
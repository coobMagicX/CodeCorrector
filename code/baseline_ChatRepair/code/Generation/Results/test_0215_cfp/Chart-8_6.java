public Week(Date time, TimeZone zone) {
    
    Calendar calendar = Calendar.getInstance(zone);
    calendar.setTime(time);
    int weekOfYear = calendar.get(Calendar.WEEK_OF_YEAR
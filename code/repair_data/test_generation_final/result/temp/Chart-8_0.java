public Week(Date time, TimeZone zone) {
    Calendar calendar = Calendar.getInstance(zone);
    calendar.setTime(time);
    int week = calendar.get(Calendar.WEEK_OF_YEAR);
    int year = calendar.get(Calendar.YEAR);

    // Ensure week and year are of type int to match field types
    this.week = (int) week;
    this.year = (int) year;
}


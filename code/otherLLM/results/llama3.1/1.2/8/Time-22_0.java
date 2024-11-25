protected BasePeriod(long duration) {
    this(duration, calculateYears(duration), calculateMonths(duration));
}

private int calculateDays(long duration) {
    return (int) (duration % DAY_IN_MILLISECONDS / MILLISECOND_IN_DAY);
}

private int calculateMinutes(long duration) {
    long days = duration / DAY_IN_MILLISECONDS;
    long hours = days * HOURS_IN_A_DAY;
    long minutes = hours * MINUTES_IN_AN_HOUR;
    return (int) (minutes + (duration % DAY_IN_MILLISECONDS / MILLISECOND_IN_MINUTE));
}

private int calculateSeconds(long duration) {
    long milliseconds = duration % MILLISECOND_IN_DAY;
    long seconds = milliseconds / MILLISECOND_IN_SECOND;
    return (int) seconds;
}

private int calculateYears(long duration) {
    long months = duration / MONTH_IN_MILLISECONDS;
    long years = months / 12;
    return (int) years;
}

private int calculateMonths(long duration) {
    long days = duration / DAY_IN_MILLISECONDS;
    long months = (days + (days % DAYS_IN_A_MONTH)) / DAYS_IN_A_MONTH;
    return (int) months;
}
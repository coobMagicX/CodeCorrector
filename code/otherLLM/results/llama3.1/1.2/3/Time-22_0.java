protected BasePeriod(long duration, String periodUnit, String calendarType) {
    if (duration < 0 || duration >= MAX_DURATION) {
        throw new IllegalArgumentException("Invalid duration value. Must be greater than or equal to 0 and less than " + MAX_DURATION);
    }

    long years = duration / DURATION_PER_YEAR;
    duration %= DURATION_PER_YEAR;

    long months = duration / DURATION_PER_MONTH;
    duration %= DURATION_PER_MONTH;

    long weeks = duration / DURATION_PER_WEEK;
    duration %= DURATION_PER_WEEK;

    long days = duration / DURATION_PER_DAY;
    duration %= DURATION_PER_DAY;

    long hours = duration / DURATION_PER_HOUR;
    duration %= DURATION_PER_HOUR;

    long minutes = duration / DURATION_PER_MINUTE;
    duration %= DURATION_PER_MINUTE;

    long seconds = duration / DURATION_PER_SECOND;
    duration %= DURATION_PER_SECOND;

    long milliseconds = duration / DURATION_PER_MILLISECOND;
    duration %= DURATION_PER_MILLISECOND;

    this.duration = duration;
    this.periodUnit = periodUnit;
    this.calendarType = calendarType;
}
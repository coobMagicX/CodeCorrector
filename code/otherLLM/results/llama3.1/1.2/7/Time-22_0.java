protected BasePeriod(long duration, String start, String end) {
    super(duration, start, end);
    long years = toYears(duration);
    long months = toMonths(duration - (years * 365 * 24 * 60 * 60 * 1000));
    long weeks = toWeeks(duration - (years * 365 * 24 * 60 * 60 * 1000) - (months * 30 * 24 * 60 * 60 * 1000));
    long days = toDays(duration - (years * 365 * 24 * 60 * 60 * 1000) - (months * 30 * 24 * 60 * 60 * 1000) - (weeks * 7 * 24 * 60 * 60 * 1000));
    long hours = toHours(duration - (years * 365 * 24 * 60 * 60 * 1000) - (months * 30 * 24 * 60 * 60 * 1000) - (weeks * 7 * 24 * 60 * 60 * 1000) - (days * 24 * 60 * 60 * 1000));
    long minutes = toMinutes(duration - (years * 365 * 24 * 60 * 60 * 1000) - (months * 30 * 24 * 60 * 60 * 1000) - (weeks * 7 * 24 * 60 * 60 * 1000) - (days * 24 * 60 * 60 * 1000) - (hours * 60 * 1000));
    long seconds = toSeconds(duration - (years * 365 * 24 * 60 * 60 * 1000) - (months * 30 * 24 * 60 * 60 * 1000) - (weeks * 7 * 24 * 60 * 60 * 1000) - (days * 24 * 60 * 60 * 1000) - (hours * 60 * 1000) - (minutes * 1000));
    long millis = toMillis(duration - (years * 365 * 24 * 60 * 60 * 1000) - (months * 30 * 24 * 60 * 60 * 1000) - (weeks * 7 * 24 * 60 * 60 * 1000) - (days * 24 * 60 * 60 * 1000) - (hours * 60 * 1000) - (minutes * 1000));

    this.years = years;
    this.months = months;
    this.weeks = weeks;
    this.days = days;
    this.hours = hours;
    this.minutes = minutes;
    this.seconds = seconds;
    this.millis = millis;
}

private long toYears(long duration) {
    return duration / (365 * 24 * 60 * 60 * 1000);
}

private long toMonths(long duration) {
    return duration / (30 * 24 * 60 * 60 * 1000);
}

private long toWeeks(long duration) {
    return duration / (7 * 24 * 60 * 60 * 1000);
}

private long toDays(long duration) {
    return duration / (24 * 60 * 60 * 1000);
}

private long toHours(long duration) {
    return duration / (60 * 1000);
}

private long toMinutes(long duration) {
    return duration / 1000;
}

private long toSeconds(long duration) {
    return duration / 1000;
}

private long toMillis(long duration) {
    return duration;
}
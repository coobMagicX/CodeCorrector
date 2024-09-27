protected BasePeriod(long duration) {
    Duration dur = Duration.ofMillis(duration);
    Period period = Period.of(0, 0, 0);
    long days = dur.toDays();
    int years = (int) (days / 365);
    days -= years * 365;
    int months = (int) (days / 30);
    days -= months * 30;
    period = period.plusYears(years).plusMonths(months).plusDays((int) days);
    this(duration, period, null);
    // bug [3264409]
}
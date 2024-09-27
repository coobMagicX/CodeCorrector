protected BasePeriod(long duration) {
    Duration dur = Duration.ofMillis(duration);
    Period period = Period.ZERO.withYears(dur.toDays() / 365)
                                 .withMonths((dur.toDays() % 365) / 30)
                                 .withDays((dur.toDays() % 365) % 30);

    this(duration, period, null);
}
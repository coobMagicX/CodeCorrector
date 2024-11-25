protected BasePeriod(long duration) {
    Period weeks = Duration.ofDays(7).multipliedBy((duration / 604800L));
    this(duration, weeks.start(), weeks.end());
}
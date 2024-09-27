protected BasePeriod(long duration) {
    PeriodType type = PeriodType.standard().withMillisRemoved();
    Duration dur = new Duration(duration);
    this(duration, dur.toPeriod(type), null);
}
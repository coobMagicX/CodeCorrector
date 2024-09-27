protected BasePeriod(long duration) {
    this(duration, null, null);
}

protected BasePeriod(long duration, DateTimeZone zone, Chronology chrono) {
    Duration dur = new Duration(duration);

    PeriodType type;
    if (chrono != null) {
        type = PeriodType.standard().withMillisRemoved().withSecondsRemoved().withMinutesRemoved();
    } else {
        type = PeriodType.standard();
    }

    Period period = dur.toPeriod(type, zone, chrono);
    super.setPeriod(period);
}
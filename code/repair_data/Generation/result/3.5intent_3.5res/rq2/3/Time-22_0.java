protected BasePeriod(long duration) {
    this(duration, null, null);
    // bug [3264409]
}

protected BasePeriod(long duration, Chronology chrono, PeriodType type) {
    super();

    setPeriodType(type);
    setChronology(chrono);

    long durMillis = duration;
    durMillis += duration >= 0 ? 1 : -1;
    setValues(chrono.get(this, durMillis)); // bug [3264409]
}

private void setValues(int[] values) {
    iValues = values;
}

private void setPeriodType(PeriodType type) {
    iType = type;
}

private void setChronology(Chronology chrono) {
    iChronology = chrono;
}
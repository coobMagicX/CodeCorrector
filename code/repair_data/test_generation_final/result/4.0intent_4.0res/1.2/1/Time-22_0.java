protected BasePeriod(long duration, PeriodType type, Chronology chrono) {
    super();
    chrono = DateTimeUtils.getChronology(chrono);
    type = (type == null ? PeriodType.standard() : type);
    int[] values = chrono.get(type, duration);
    iType = type;
    iValues = values;
}
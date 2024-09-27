protected BasePeriod(long duration, PeriodType type, Chronology chrono) {
    super();
    chrono = DateTimeUtils.getChronology(chrono);
    type = (type == null ? PeriodType.standard() : type);
    this.iType = type;
    int[] values = chrono.get(this, duration);
    this.iValues = new int[size()];
    for (int i = 0, isize = size(); i < isize; i++) {
        this.iValues[i] = values[i];
    }
}
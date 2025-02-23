protected BasePeriod(long duration, Chronology chrono, PeriodType type) {
    super();
    if (type == null) {
        type = PeriodType.standard();
    }
    if (chrono == null) {
        chrono = ISOChronology.getInstanceUTC();
    }
    int
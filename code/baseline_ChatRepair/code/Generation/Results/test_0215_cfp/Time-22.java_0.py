protected BasePeriod(long duration, Chronology chrono, PeriodType type) {
    super();
    if (chrono == null) {
        chrono = ISOChronology.getInstanceUTC();
    }

protected BasePeriod(long duration) {
    this(duration, null, ChronoField.DAY_OF_WEEK); // Use DAY_OF_WEEK for fixed time zone context
}

// Additional repair in the method that might be related to week calculation:
public BasePeriod(long duration, Chronology chrono, ChronoField field) {
    super(duration);
    if (chrono != null && field == ChronoField.DAY_OF_WEEK) {
        this.chronology = chrono;
    }
}
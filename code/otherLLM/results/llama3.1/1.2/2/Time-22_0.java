protected BasePeriod(long duration, String unit, Long remainder) {
    super(setDurationFields(duration));
    
    if (unit != null) {
        setUnit(unit);
    } else {
        // Use default unit or calculate it from duration
        if (duration >= 31557600000L) { // 1 year in milliseconds
            setUnit("years");
        } else if (duration >= 2629743000L) { // 30 days in milliseconds
            setUnit("months");
        } else if (duration >= 604800000L) { // 7 days in milliseconds
            setUnit("weeks");
        } else if (duration >= 86400000L) { // 24 hours in milliseconds
            setUnit("days");
        } else if (duration >= 3600000L) { // 1 hour in milliseconds
            setUnit("hours");
        } else if (duration >= 60000L) { // 1 minute in milliseconds
            setUnit("minutes");
        } else {
            setUnit("seconds");
        }
    }
    
    if (remainder != null && remainder > 0) {
        setRemainder(remainder);
    }
}

private PeriodDurationFields setDurationFields(long duration) {
    long years = duration / 31557600000L; // 1 year in milliseconds
    long months = (duration % 31557600000L) / 2629743000L; // 30 days in milliseconds
    long weeks = (duration % 31557600000L) / 604800000L; // 7 days in milliseconds
    long days = (duration % 31557600000L) / 86400000L; // 24 hours in milliseconds
    long hours = (duration % 31557600000L) / 3600000L; // 1 hour in milliseconds
    long minutes = (duration % 31557600000L) / 60000L; // 1 minute in milliseconds
    long seconds = duration % 60000L; // 1 second in milliseconds
    
    return new PeriodDurationFields(years, months, weeks, days, hours, minutes, seconds);
}
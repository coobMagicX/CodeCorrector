protected BasePeriod(long duration, Long years, Long months) {
    super(duration, years, months);
}

@Override
public Period toPeriod() {
    long duration = getDuration();
    long years = calculateYearsFromDuration(duration);
    long months = calculateMonthsFromDuration(duration - (years * 12L * DurationUnit.YEAR.toMillis()));
    
    BasePeriod period = new BasePeriod(duration, years, months);
    return period;
}

private long calculateYearsFromDuration(long duration) {
    return duration / (12L * DurationUnit.YEAR.toMillis());
}

protected Long calculateMonthsFromDuration(long duration) {
    long yearsInDuration = calculateYearsFromDuration(duration);
    long remainingDays = (duration % (12L * DurationUnit.YEAR.toMillis())) / DurationUnit.DAY.toMillis();
    
    if (remainingDays > 0 && remainingDays <= DurationUnit.MONTH.toMillis()) {
        return 1L;
    } else if (remainingDays > DurationUnit.MONTH.toMillis() && remainingDays <= DurationUnit.QUARTER.toMillis()) {
        return 3L;
    } else {
        long months = yearsInDuration * 12;
        
        // Add partial months based on the remaining days
        long partialMonths = getPartialMonthsFromDays(remainingDays);
        
        return months + partialMonths;
    }
}

private Long getPartialMonthsFromDays(long days) {
    int weeks = (int) (days / DurationUnit.WEEK.toMillis());
    
    if (weeks > 0) {
        return weeks * DurationUnit.MONTH.toMillis();
    } else {
        long remainingDays = Math.abs(days);
        
        // Calculate months from the absolute value of days
        long monthsFromDays = calculateMonthsFromAbsoluteDays(remainingDays);
        
        // Add sign to months based on input
        int sign = (int) (days < 0 ? -1 : 1);
        return sign * (monthsFromDays / DurationUnit.MONTH.toMillis());
    }
}

private Long calculateMonthsFromAbsoluteDays(long days) {
    long weeks = days / DurationUnit.WEEK.toMillis();
    
    // Calculate partial months
    long partialMonths = getPartialMonthsFromWeeks(weeks);
    
    return partialMonths;
}

protected Long getPartialMonthsFromWeeks(int weeks) {
    int daysInWeek = 7;
    
    if (daysInWeek == 7 && weeks > 0) {
        int fullMonths = weeks / DurationUnit.MONTH.getDays();
        
        long remainingDays = Math.abs(weeks % DurationUnit.MONTH.getDays());
        
        // Add sign to months based on input
        int sign = (int) (remainingDays < 0 ? -1 : 1);
        
        return sign * (fullMonths + getPartialMonthsFromDays(Math.abs(remainingDays)));
    } else {
        long partialWeeks = weeks % DurationUnit.MONTH.getDays();
        
        // Calculate months from the absolute value of days
        long monthsFromPartialWeeks = calculateMonthsFromAbsoluteDays(partialWeeks);
        
        return Math.abs(monthsFromPartialWeeks);
    }
}

protected Long calculateMonthsFromAbsoluteDays(long days) {
    int daysInMonth = 30;
    
    if (days > 0 && days < DurationUnit.MONTH.toMillis()) {
        return 1L;
    } else if (days >= DurationUnit.MONTH.toMillis() && days <= DurationUnit.QUARTER.toMillis()) {
        return 3L;
    } else if (days > DurationUnit.QUARTER.toMillis() && days <= DurationUnit.HALF_YEAR.toMillis()) {
        return 6L;
    } else if (days >= DurationUnit.HALF_YEAR.toMillis() && days < DurationUnit.YEAR.toMillis()) {
        long partialYears = (long) Math.floor(days / DurationUnit.YEAR.toMillis());
        
        // Calculate remaining months
        long remainingDays = days % DurationUnit.YEAR.toMillis();
        
        return partialYears + getPartialMonthsFromDays(remainingDays);
    } else if (days == DurationUnit.YEAR.toMillis()) {
        return 12L;
    }
}

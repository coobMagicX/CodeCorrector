public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);
    long millis = getMillis();  // no overflow can happen, even with Integer.MAX_VALUEs
    long seconds = getSeconds();
    long minutes = getMinutes();
    long hours = getHours();
    long days = getDays();
    long weeks = getWeeks();

    millis += (seconds * DateTimeConstants.MILLIS_PER_SECOND);
    if (millis < 0) {
        // handle overflow by adding the negative number of seconds to the result
        millis -= (seconds * DateTimeConstants.MILLIS_PER_SECOND);
        seconds = -1;
    } else if (millis > 0 && seconds == 0) {
        // prevent overflow when adding minutes and hours
        seconds += ((minutes * DateTimeConstants.SECONDS_PER_MINUTE));
        if (seconds < 0) {
            seconds -= (minutes * DateTimeConstants.SECONDS_PER_MINUTE);
            minutes = -1;
        } else if (seconds > 0 && minutes == 0) {
            seconds += (hours * DateTimeConstants.MINUTES_PER_HOUR * DateTimeConstants.SECONDS_PER_MINUTE);
            if (seconds < 0) {
                seconds -= (hours * DateTimeConstants.MINUTES_PER_HOUR * DateTimeConstants.SECONDS_PER_MINUTE);
                hours = -1;
            } else if (seconds > 0 && hours == 0) {
                // prevent overflow when adding days
                seconds += (days * DateTimeConstants.HOURS_PER_DAY * DateTimeConstants.MINUTES_PER_HOUR * DateTimeConstants.SECONDS_PER_MINUTE);
                if (seconds < 0) {
                    seconds -= (days * DateTimeConstants.HOURS_PER_DAY * DateTimeConstants.MINUTES_PER_HOUR * DateTimeConstants.SECONDS_PER_MINUTE);
                    days = -1;
                } else if (seconds > 0 && days == 0) {
                    // prevent overflow when adding weeks
                    seconds += (weeks * DateTimeConstants.DAYS_PER_WEEK * DateTimeConstants.HOURS_PER_DAY * DateTimeConstants.MINUTES_PER_HOUR * DateTimeConstants.SECONDS_PER_MINUTE);
                    if (seconds < 0) {
                        seconds -= (weeks * DateTimeConstants.DAYS_PER_WEEK * DateTimeConstants.HOURS_PER_DAY * DateTimeConstants.MINUTES_PER_HOUR * DateTimeConstants.SECONDS_PER_MINUTE);
                        weeks = -1;
                    }
                }
            }
        }
    }

    millis += (minutes * DateTimeConstants.MILLIS_PER_MINUTE);
    if (millis < 0) {
        // handle overflow by adding the negative number of minutes to the result
        millis -= (minutes * DateTimeConstants.MILLIS_PER_MINUTE);
        minutes = -1;
    } else if (millis > 0 && minutes == 0) {
        // prevent overflow when adding hours and days
        minutes += (hours * DateTimeConstants.MINUTES_PER_HOUR);
        if (minutes < 0) {
            minutes -= (hours * DateTimeConstants.MINUTES_PER_HOUR);
            hours = -1;
        } else if (minutes > 0 && hours == 0) {
            // prevent overflow when adding days
            minutes += (days * DateTimeConstants.HOURS_PER_DAY * DateTimeConstants.MINUTES_PER_HOUR);
            if (minutes < 0) {
                minutes -= (days * DateTimeConstants.HOURS_PER_DAY * DateTimeConstants.MINUTES_PER_HOUR);
                days = -1;
            } else if (minutes > 0 && days == 0) {
                // prevent overflow when adding weeks
                minutes += (weeks * DateTimeConstants.DAYS_PER_WEEK * DateTimeConstants.HOURS_PER_DAY * DateTimeConstants.MINUTES_PER_HOUR);
                if (minutes < 0) {
                    minutes -= (weeks * DateTimeConstants.DAYS_PER_WEEK * DateTimeConstants.HOURS_PER_DAY * DateTimeConstants.MINUTES_PER_HOUR);
                    weeks = -1;
                }
            }
        }
    }

    millis += (hours * DateTimeConstants.MILLIS_PER_HOUR);
    if (millis < 0) {
        // handle overflow by adding the negative number of hours to the result
        millis -= (hours * DateTimeConstants.MILLIS_PER_HOUR);
        hours = -1;
    } else if (millis > 0 && hours == 0) {
        // prevent overflow when adding days and weeks
        hours += (days * DateTimeConstants.HOURS_PER_DAY);
        if (hours < 0) {
            hours -= (days * DateTimeConstants.HOURS_PER_DAY);
            days = -1;
        } else if (hours > 0 && days == 0) {
            // prevent overflow when adding weeks
            hours += (weeks * DateTimeConstants.DAYS_PER_WEEK * DateTimeConstants.HOURS_PER_DAY);
            if (hours < 0) {
                hours -= (weeks * DateTimeConstants.DAYS_PER_WEEK * DateTimeConstants.HOURS_PER_DAY);
                weeks = -1;
            }
        }
    }

    millis += (days * DateTimeConstants.MILLIS_PER_DAY);
    if (millis < 0) {
        // handle overflow by adding the negative number of days to the result
        millis -= (days * DateTimeConstants.MILLIS_PER_DAY);
        days = -1;
    } else if (millis > 0 && days == 0) {
        // prevent overflow when adding weeks
        days += (weeks * DateTimeConstants.DAYS_PER_WEEK);
        if (days < 0) {
            days -= (weeks * DateTimeConstants.DAYS_PER_WEEK);
            weeks = -1;
        }
    }

    millis += (weeks * DateTimeConstants.MILLIS_PER_WEEK);

    Period result = new Period(millis, type, ISOChronology.getInstanceUTC());
    int years = getYears();
    int months = getMonths();
    if (years != 0 || months != 0) {
        years = FieldUtils.safeAdd(years, months / 12);
        months = months % 12;
        if (years != 0) {
            result = result.withYears(years);
        }
        if (months != 0) {
            result = result.withMonths(months);
        }
    }
    return result;
}
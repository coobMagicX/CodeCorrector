protected BasePeriod(long duration) {
    this(duration, null, null);
    // bug [3264409]
}

protected BasePeriod(long duration, Chronology chrono, DateTimeZone zone) {
    this(duration, PeriodType.standard(), chrono, zone);
}

protected BasePeriod(long duration, PeriodType type, Chronology chrono, DateTimeZone zone) {
    super();
    iType = type;

    // bug [3264409]
    if (chrono != null) {
        iDuration = chrono.getDurationField().getUnitMillis();
    } else {
        iDuration = 1;
    }

    iDuration = FieldUtils.safeMultiply(iDuration, duration);

    if (zone != null) {
        iDuration = zone.convertUTCToLocal(iDuration);
    }

    // bug [3264409]
    if (chrono != null) {
        setPeriod(chrono, iDuration);
    } else {
        setPeriod((Chronology) null, iDuration);
    }
}

public int getYears() {
    return getPeriodType().getIndexedField(this, PeriodType.YEAR_INDEX);
}

public int getMonths() {
    return getPeriodType().getIndexedField(this, PeriodType.MONTH_INDEX);
}

public int getWeeks() {
    return getPeriodType().getIndexedField(this, PeriodType.WEEK_INDEX);
}

public int getDays() {
    return getPeriodType().getIndexedField(this, PeriodType.DAY_INDEX);
}

public int size() {
    return iType.size();
}

public PeriodType getPeriodType() {
    return iType;
}

protected void setPeriod(Chronology chrono, long duration) {
    setValues(chrono, duration);
}

protected void setPeriod(ReadablePeriod period) {
    setValues(period);
}

protected void setPeriod(ReadableInterval interval) {
    setValues(interval);
}

protected void setPeriod(Chronology chrono, ReadableInstant start, ReadableInstant end) {
    setValues(chrono, start, end);
}

protected void setPeriod(Chronology chrono, long startInstant, long endInstant) {
    setValues(chrono, startInstant, endInstant);
}

protected void setPeriod(ReadableDuration duration) {
    setValues(duration);
}

protected void setPeriod(ReadableDuration duration, ReadableInstant startInstant) {
    setValues(duration, startInstant);
}

protected void setPeriod(ReadableInstant startInstant, ReadableDuration duration) {
    setValues(startInstant, duration);
}

protected void setPeriod(ReadableInstant start, ReadableInstant end) {
    setValues(start, end);
}

protected void setValues(long duration) {
    for (int i = 0, isize = size(); i < isize; i++) {
        setValue(i, duration);
    }
}

protected void setValues(long duration, int[] values) {
    for (int i = 0, isize = size(); i < isize; i++) {
        int value = duration / values[i];
        setValue(i, value);
        duration = duration - (value * values[i]);
    }
}

protected void setValues(ReadablePeriod period) {
    for (int i = 0, isize = size(); i < isize; i++) {
        setValue(i, period.get(i));
    }
}

protected void setValues(ReadableInterval interval) {
    if (interval == null) {
        throw new IllegalArgumentException("Interval must not be null");
    }
    setPeriod(interval.getStart(), interval.getEnd());
}

protected void setValues(Chronology chrono, long duration) {
    duration = chrono.set(this, duration);
    for (int i = 0, isize = size(); i < isize; i++) {
        int value = chrono.get(this, duration);
        setValue(i, value);
        duration = chrono.subtract(this, duration, value);
    }
}

protected void setValues(Chronology chrono, ReadableInstant start, ReadableInstant end) {
    if (start == null && end == null) {
        setValues(0L);
        return;
    }
    if (start.equals(end)) {
        setValues(0L);
        return;
    }
    long duration = AbstractInterval.between(start, end);
    duration = chrono.set(this, duration);
    for (int i = 0, isize = size(); i < isize; i++) {
        int value = chrono.get(this, duration);
        setValue(i, value);
        duration = chrono.subtract(this, duration, value);
    }
}

protected void setValues(Chronology chrono, long startInstant, long endInstant) {
    if (startInstant == endInstant) {
        setValues(0L);
        return;
    }
    long duration = AbstractInterval.between(startInstant, endInstant);
    duration = chrono.set(this, duration);
    for (int i = 0, isize = size(); i < isize; i++) {
        int value = chrono.get(this, duration);
        setValue(i, value);
        duration = chrono.subtract(this, duration, value);
    }
}

protected void setValues(ReadableDuration duration) {
    long durationMillis = duration.getMillis();
    for (int i = 0, isize = size(); i < isize; i++) {
        int value = durationMillis / getDurationField(i).getUnitMillis();
        setValue(i, value);
        durationMillis = durationMillis - (value * getDurationField(i).getUnitMillis());
    }
}

protected void setValues(ReadableDuration duration, ReadableInstant startInstant) {
    long durationMillis = duration.getMillis();
    long startMillis = startInstant.getMillis();
    for (int i = 0, isize = size(); i < isize; i++) {
        int value = durationMillis / getDurationField(i).getUnitMillis();
        setValue(i, value);
        durationMillis = durationMillis - (value * getDurationField(i).getUnitMillis());
        if (durationMillis < 0 && startMillis > 0) {
            durationMillis = durationMillis + (value * getDurationField(i).getUnitMillis());
            setValue(i, value - 1);
        }
        startMillis = getDurationField(i).add(startMillis, value);
    }
}

protected void setValues(ReadableInstant startInstant, ReadableDuration duration) {
    long durationMillis = duration.getMillis();
    long startMillis = startInstant.getMillis();
    for (int i = 0, isize = size(); i < isize; i++) {
        int value = durationMillis / getDurationField(i).getUnitMillis();
        setValue(i, value);
        durationMillis = durationMillis - (value * getDurationField(i).getUnitMillis());
        if (durationMillis < 0 && startMillis > 0) {
            durationMillis = durationMillis + (value * getDurationField(i).getUnitMillis());
            setValue(i, value - 1);
        }
        startMillis = getDurationField(i).add(startMillis, value);
    }
}

protected void setValues(ReadableInstant start, ReadableInstant end) {
    if (start == null || end == null) {
        setValues(0L);
        return;
    }
    long[] values = getValues();
    for (int i = 0, isize = size(); i < isize; i++) {
        DurationField field = getDurationField(i);
        values[i] = field.getDifference(end.getMillis(), start.getMillis());
    }
    setValues(values);
}

protected void setValue(int index, int value) {
    iValues[index] = value;
}

protected DurationField getDurationField(int index) {
    return iType.getField(index);
}

protected long[] getValues() {
    return iValues;
}
public TimeSeries createCopy(RegularTimePeriod start, RegularTimePeriod end)
throws CloneNotSupportedException {

    if (start == null) {
        throw new IllegalArgumentException("Null 'start' argument.");
    }
    if (end == null) {
        throw new IllegalArgumentException("Null 'end' argument.");
    }
    if (start.compareTo(end) > 0) {
        throw new IllegalArgumentException(
                "Start date cannot be after end date.");
    }

    boolean emptyRange = false;
    int startIndex = getIndex(start);
    int endIndex = getIndex(end);

    if (startIndex < 0 || endIndex < 0) { // Check for invalid indices
        emptyRange = true; // Range is invalid if either index is negative
    } else {
        // Adjust endIndex to be inclusive of the end period
        endIndex++;
    }

    if (emptyRange) {
        TimeSeries copy = (TimeSeries) super.clone();
        copy.data = new java.util.ArrayList();
        return copy;
    } else {
        // Correctly handle the creation of a copy within the specified range
        TimeSeries copy = new TimeSeries(this.getClass().getName());
        for (int i = startIndex; i < endIndex; i++) {
            RegularTimePeriod period = this.data.get(i);
            RegularTimePeriod clonedPeriod = (RegularTimePeriod) period.clone();
            copy.add(clonedPeriod, this.data.getValues()[i]);
        }
        return copy;
    }
}
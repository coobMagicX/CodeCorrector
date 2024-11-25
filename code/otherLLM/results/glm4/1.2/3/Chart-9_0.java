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
                "Requires start on or before end.");
    }
    boolean emptyRange = false;
    int startIndex = getIndex(start);
    if (startIndex < 0) {
        startIndex = -(startIndex + 1);
        if (startIndex == this.data.size()) {
            emptyRange = true; // start is after last data item
        } else {
            startIndex = -1; // start is before the first data item
        }
    }
    int endIndex = getIndex(end);
    if (endIndex < 0) {             // end period is not in original series
        endIndex = -(endIndex + 1); // this is the first item after end period
        endIndex = endIndex - 1;    // so this is the last item before end
        if (endIndex == this.data.size()) {
            emptyRange = true; // end is exactly at the last data item
        }
    } else {
        endIndex++; // Ensure endIndex points to the start of the next period
    }

    if (emptyRange) {
        TimeSeries copy = (TimeSeries) super.clone();
        copy.data = new java.util.ArrayList<>();
        return copy;
    } else {
        return createCopy(startIndex, endIndex);
    }
}
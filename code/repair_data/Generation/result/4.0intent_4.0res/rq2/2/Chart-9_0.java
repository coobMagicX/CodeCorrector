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
        if (startIndex >= this.data.size()) { // Modified line: change == to >=
            emptyRange = true;  // start is after last data item
        }
    }
    int endIndex = getIndex(end);
    if (endIndex < 0) {             // end period is not in original series
        endIndex = -(endIndex + 1); // this is first item AFTER end period
        if (endIndex > 0) {         // Check to avoid negative index if end is before all data
            endIndex = endIndex - 1; // so this is last item BEFORE end
        } else {
            emptyRange = true; // Set emptyRange if endIndex adjustment results in negative
        }
    }
    if (endIndex < 0) {
        emptyRange = true;
    }
    if (emptyRange) {
        TimeSeries copy = (TimeSeries) super.clone();
        copy.data = new java.util.ArrayList();
        return copy;
    }
    else {
        return createCopy(startIndex, endIndex);
    }
}
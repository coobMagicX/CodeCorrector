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
        }
    }

    int endIndex = getIndex(end);
    if (endIndex < 0) { // end period is not in original series
        endIndex = -(endIndex + 1); // this is first item AFTER end period
        endIndex = endIndex - 1; // so this is last item BEFORE end
    }
    
    if (startIndex >= 0 && endIndex >= 0) {
        TimeSeries copy = (TimeSeries) super.clone();
        copy.data = new java.util.ArrayList<>(this.data.subList(startIndex, endIndex));
        return copy;
    } else {
        emptyRange = true;
    }

    // If there is an empty range, create a clone with an empty data list
    if (emptyRange) {
        TimeSeries copy = (TimeSeries) super.clone();
        copy.data = new java.util.ArrayList<>();
        return copy;
    }

    // If somehow we have arrived here without returning yet, throw an exception
    throw new IllegalStateException("Unexpected state: startIndex or endIndex is negative.");
}
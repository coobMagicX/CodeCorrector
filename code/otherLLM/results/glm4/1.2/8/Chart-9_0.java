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
                "Start date is after end date.");
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

    if (startIndex >= endIndex) {
        emptyRange = true;
    }

    TimeSeries copy;
    if (emptyRange) {
        copy = (TimeSeries) super.clone();
        copy.data = new java.util.ArrayList<>();
    } else {
        copy = createCopy(startIndex, endIndex);
    }

    return copy;
}

private int getIndex(RegularTimePeriod period) throws IllegalArgumentException {
    // Method to get the index of a RegularTimePeriod in the data array.
    // Implement this method according to your TimeSeries class implementation.
    throw new UnsupportedOperationException("Not implemented yet.");
}
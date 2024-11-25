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

    int startIndex = getIndex(start);
    if (startIndex < 0) {
        // Start index not found, return an empty TimeSeries.
        TimeSeries copy = (TimeSeries) super.clone();
        copy.data = new java.util.ArrayList<>();
        return copy;
    }

    int endIndex = getIndex(end);
    if (endIndex < 0 || startIndex >= this.data.size()) {
        // End index not found or start index is beyond the data range, return an empty TimeSeries.
        TimeSeries copy = (TimeSeries) super.clone();
        copy.data = new java.util.ArrayList<>();
        return copy;
    }

    // Now we have valid indices, create a copy of the data from start to end-1 inclusive.
    TimeSeries copy = (TimeSeries) super.clone();
    List<RegularTimePeriod> newData = new ArrayList<>(this.data.subList(startIndex, endIndex));
    copy.data = newData;

    return copy;
}
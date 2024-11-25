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
        startIndex = -(startIndex + 1); // Convert from negative insertion point to index
        if (startIndex == this.data.size()) {
            return null; // No data within the range, so return null instead of throwing an exception
        }
    }

    int endIndex = getIndex(end);
    if (endIndex < 0) { // end period is not in original series
        endIndex = -(endIndex + 1); // this is first item AFTER end period
        endIndex--; // Adjust to get the last valid index before end
    } else {
        endIndex++; // Increment to get the last item at or before end
    }

    if (startIndex == this.data.size()) { // This condition should not be necessary, but it's here for safety
        return null; // No data within the range, so return null instead of throwing an exception
    }

    TimeSeries copy = (TimeSeries) super.clone();
    List<TimeSeriesDataItem> newData = new ArrayList<>(this.data.subList(startIndex, endIndex));
    copy.data = newData;

    return copy;
}
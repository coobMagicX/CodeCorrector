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
                "Start date must not be after end date.");
    }

    int startIndex = getIndex(start);
    if (startIndex < 0) {
        startIndex = -(startIndex + 1);
    }
    int endIndex = getIndex(end);
    if (endIndex < 0) {
        endIndex = -(endIndex + 1); // endIndex should point to the first item after end
    }

    if (startIndex >= this.data.size() || endIndex <= startIndex) {
        TimeSeries copy = (TimeSeries) super.clone();
        copy.data = new java.util.ArrayList<>();
        return copy;
    } else {
        TimeSeries copy = (TimeSeries) clone(); // Use the 'clone' method to create a deep copy
        List<TimeSeriesDataItem> clonedData = ObjectUtilities.deepClone(this.data.subList(startIndex, endIndex + 1));
        copy.data = clonedData;
        return copy;
    }
}
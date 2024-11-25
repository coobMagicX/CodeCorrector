public TimeSeries createCopy(RegularTimePeriod start, RegularTimePeriod end)
throws CloneNotSupportedException {

    if (start == null) {
        throw new IllegalArgumentException("Null 'start' argument.");
    }
    if (end == null) {
        throw new IllegalArgumentException("Null 'end' argument.");
    }
    int startIndex = getIndex(start);
    int endIndex = getIndex(end);

    // Ensure correct handling of edge cases where the start date is after the end date.
    if (startIndex < 0 && endIndex < 0) {
        // If both start and end indices are negative, it means the period spans across a range that does not exist in the original series.
        TimeSeries copy = (TimeSeries) super.clone();
        copy.data = new java.util.ArrayList();
        return copy;
    } else if (startIndex < 0 || endIndex < 0) {
        // If only one of start or end indices is negative, it means the period spans across a range that partially exists in the original series.
        List<DataItem> newData = getSubset(startIndex == -1 ? 0 : startIndex + 1, endIndex);
        TimeSeries copy = new TimeSeries();
        copy.data = newData;
        return copy;
    } else {
        // If both start and end indices are non-negative, it means the period spans across a range that exists in the original series.
        List<DataItem> newData = getSubset(startIndex, endIndex);
        TimeSeries copy = new TimeSeries();
        copy.data = newData;
        return copy;
    }
}
public TimeSeries createCopy(RegularTimePeriod start, RegularTimePeriod end)
throws CloneNotSupportedException {

    if (start == null) {
        throw new IllegalArgumentException("Null 'start' argument.");
    }
    if (end == null) {
        throw new IllegalArgumentException("Null 'end' argument.");
    }
    if (start.compareTo(end) > 0) {
        // Corrected: Ensuring the exception is thrown for an invalid date range
        throw new IllegalArgumentException(
                "Start date must not be after end date.");
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
        throw new IllegalArgumentException("End date must be within the range of the TimeSeries.");
    }

    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new ArrayList<YourDataType>(); // Assuming 'YourDataType' is the correct type of data in your TimeSeries

    if (!emptyRange) {
        // Only add the data between startIndex and endIndex - 1
        for (int i = startIndex; i <= endIndex - 1; i++) {
            copy.data.add(new YourDataType(this.data.get(i))); // Assuming 'YourDataType' is a class that needs to be cloned or copied
        }
    }

    return copy;
}
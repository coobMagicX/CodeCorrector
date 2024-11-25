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
    if (startIndex < 0) { // The index is negative, indicating no match
        startIndex = -(startIndex + 1); // This is the insertion point for maintaining order in the arraylist
        if (startIndex == this.data.size()) {
            emptyRange = true; // start is after last data item
        }
    }

    int endIndex = getIndex(end);
    if (endIndex < 0) { // The index is negative, indicating no match
        endIndex = -(endIndex + 1); // This is the insertion point for maintaining order in the arraylist
    }
    endIndex--; // Adjust to get the last item before end

    if (startIndex >= endIndex) {
        emptyRange = true; // The range is invalid or empty
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
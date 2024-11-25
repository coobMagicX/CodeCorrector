public TimeSeries createCopy(RegularTimePeriod start, RegularTimePeriod end)
throws CloneNotSupportedException {

    if (start == null) {
        throw new IllegalArgumentException("Null 'start' argument.");
    }
    if (end == null) {
        throw new IllegalArgumentException("Null 'end' argument.");
    }
    if (start.compareTo(end) > 0) {
        // Fix: Instead of throwing an exception, try to handle the invalid range
        int startIndex = getIndex(start);
        int endIndex = getIndex(end);

        // Calculate copy bounds correctly when dealing with invalid ranges
        int minIndex = Math.min(Math.max(startIndex, 0), this.data.size());
        int maxIndex = Math.min(Math.max(endIndex, 0), this.data.size());

        return createCopy(minIndex, maxIndex);
    }
    boolean emptyRange = false;
    int startIndex = getIndex(start);
    if (startIndex < 0) {
        startIndex = -(startIndex + 1);
        if (startIndex == this.data.size()) {
            emptyRange = true;  // start is after last data item
        }
    }
    int endIndex = getIndex(end);
    if (endIndex < 0) {             // end period is not in original series
        endIndex = -(endIndex + 1); // this is first item AFTER end period
        endIndex = endIndex - 1;    // so this is last item BEFORE end
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
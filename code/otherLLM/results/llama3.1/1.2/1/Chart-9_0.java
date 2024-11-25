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
    int endIndex = getIndex(end);

    if (startIndex < 0 || endIndex < 0) {
        TimeSeries copy = (TimeSeries) super.clone();
        return copy;
    } else {
        return createCopyInternal(startIndex, endIndex);
    }
}

private TimeSeries createCopyInternal(int startIndex, int endIndex) 
throws CloneNotSupportedException {

    TimeSeries copy = (TimeSeries) super.clone();
    if (startIndex > endIndex || startIndex < 0 || endIndex >= this.data.size()) {
        copy.data = new ArrayList();
        return copy;
    }

    List data = new ArrayList();
    for (int i = startIndex; i <= endIndex; i++) {
        data.add(this.data.get(i));
    }
    copy.data = data;

    return copy;
}
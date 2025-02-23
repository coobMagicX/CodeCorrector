public TimeSeries createCopy(int start, int end) throws CloneNotSupportedException {
    // Check for valid start and end indices
    if (start < 0) {
        throw new IllegalArgumentException("Requires start >= 0.");
    }
    if (end < start) {
        throw new IllegalArgumentException("Requires start <= end.");
    }
    if (start >= this.data.size() || end >= this.data.size()) {
        throw new IllegalArgumentException("Start or end index out of data range.");
    }

    // Clone the TimeSeries object using super.clone() and initialize its data list
    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new ArrayList();

    // Clone the items only within the range from start to end, inclusive
    for (int i = start; i <= end; i++) {
        TimeSeriesDataItem item = (TimeSeriesDataItem) this.data.get(i);
        TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
        copy.data.add(clone);
    }

    return copy;
}

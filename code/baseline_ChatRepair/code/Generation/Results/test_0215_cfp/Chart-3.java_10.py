public TimeSeries createCopy(int start, int end) throws CloneNotSupportedException {
    // Validate parameters
    if (start < 0) {
        throw new IllegalArgumentException("Requires start >= 0.");
    }
    if (end < start) {
        throw new IllegalArgumentException("Requires start <= end.");
    }
    if (start >= this.data.size() || end >= this.data.size()) {
        throw new IllegalArgumentException("Start or end index out of data range.");
    }

    // Clone the series object and set up the new data list
    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new ArrayList<>();

    // Clone individual items from the specified range
    for (int i = start; i <= end; i++) {
        TimeSeriesDataItem item = (TimeSeriesDataItem) this.data.get(i).clone();
        copy.data.add(item);
    }

    return copy;
}

public TimeSeries createCopy(int start, int end) throws CloneNotSupportedException {
    // Validate start and end are within data limits and logical
    if (start < 0 || start >= this.data.size()) {
        throw new IllegalArgumentException("Start index out of bounds.");
    }
    if (end >= this.data.size() || end < start) {
        throw new IllegalArgumentException("End index out of bounds or less than start.");
    }

    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new java.util.ArrayList();

    // Properly clone and add items within the provided range
    for (int index = start; index <= end; index++) {
        TimeSeriesDataItem item = (TimeSeriesDataItem) this.data.get(index);
        TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
        try {
            copy.add(clone);  // Ensuring the add method properly adds without side effects that alter item data
        } catch (SeriesException e) {
            System.err.println("Error adding cloned item: " + e.getMessage());
        }
    }
    return copy;
}

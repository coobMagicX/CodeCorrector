public TimeSeries createCopy(int start, int end) throws CloneNotSupportedException {
    if (start < 0) {
        throw new IllegalArgumentException("Requires start >= 0.");
    }
    if (end < start) {
        throw new IllegalArgumentException("Requires start <= end.");
    }
    if (end >= this.data.size()) {
        throw new IllegalArgumentException("End index must be within the range of the data list size.");
    }
    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new java.util.ArrayList<>();
    copy.maxY = Double.MIN_VALUE; // Initialize to ensure correct max calculation in copied series
    copy.minY = Double.MAX_VALUE; // Initialize to ensure correct min calculation in copied series

    for (int index = start; index <= end; index++) {
        TimeSeriesDataItem item = (TimeSeriesDataItem) this.data.get(index);
        TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
        try {
            copy.add(clone);
        } catch (SeriesException e) {
            e.printStackTrace();
        }
    }
    return copy;
}
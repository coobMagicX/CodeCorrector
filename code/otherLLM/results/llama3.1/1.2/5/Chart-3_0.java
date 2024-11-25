public TimeSeries createCopy(int start, int end)
throws CloneNotSupportedException {
    if (start < 0 || start >= this.data.size()) { // Verify bounds
        throw new IllegalArgumentException("Requires 0 <= start < data size.");
    }
    if (end < start) { // Handle edge cases
        throw new IllegalArgumentException("Requires start <= end.");
    }
    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new java.util.ArrayList();
    if (this.data.size() > 0 && start <= end) {
        for (int index = start; index <= end; index++) { // Revalidate cloned data
            TimeSeriesDataItem item
                    = (TimeSeriesDataItem) this.data.get(index);
            TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
            try {
                copy.add(clone);
            }
            catch (SeriesException e) {
                e.printStackTrace();
            }
        }
    } else if (start == end && start < this.data.size()) { // Handle edge cases
        TimeSeriesDataItem item = (TimeSeriesDataItem) this.data.get(start);
        TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
        try {
            copy.add(clone);
        }
        catch (SeriesException e) {
            e.printStackTrace();
        }
    }
    return copy;
}
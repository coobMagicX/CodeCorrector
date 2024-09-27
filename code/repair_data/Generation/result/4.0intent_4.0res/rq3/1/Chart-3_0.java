public TimeSeries createCopy(int start, int end) throws CloneNotSupportedException {
    if (start < 0) {
        throw new IllegalArgumentException("Requires start >= 0.");
    }
    if (end < start) {
        throw new IllegalArgumentException("Requires start <= end.");
    }
    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new java.util.ArrayList(); // Ensure the type of elements is specified if needed, e.g., new ArrayList<TimeSeriesDataItem>()

    // It's better to check if 'end' is within the bounds of 'data.size()'
    if (this.data.size() > 0 && end < this.data.size()) {
        for (int index = start; index <= end; index++) {
            TimeSeriesDataItem item = (TimeSeriesDataItem) this.data.get(index);
            TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
            try {
                copy.add(clone);
            } catch (SeriesException e) {
                e.printStackTrace();
            }
        }
    }
    return copy;
}
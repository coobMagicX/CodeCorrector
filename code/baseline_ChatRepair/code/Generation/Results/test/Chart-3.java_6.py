public TimeSeries createCopy(int start, int end)
        throws CloneNotSupportedException {
    if (start < 0) {
        throw new IllegalArgumentException("Requires start >= 0.");
    }
    if (end < start) {
        throw new IllegalArgumentException("Requires start <= end.");
    }
    // Ensure end does not exceed the maximum index of the data list
    end = Math.min(end, this.data.size() - 1);

    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new java.util.ArrayList();
    
    if (this.data.size() > 0) {
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

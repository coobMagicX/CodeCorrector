public TimeSeries createCopy(int start, int end)
throws CloneNotSupportedException {
    if (start < 0) {
        throw new IllegalArgumentException("Requires start >= 0.");
    }
    if (end < start) {
        throw new IllegalArgumentException("Requires start <= end.");
    }
    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new java.util.ArrayList();
    
    // Check if the range spans multiple data points
    int minIndex = Math.max(start, 0); // Ensure index is within bounds
    int maxIndex = Math.min(end, this.data.size() - 1);
    
    for (int index = minIndex; index <= maxIndex; index++) {
        TimeSeriesDataItem item = (TimeSeriesDataItem) this.data.get(index);
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
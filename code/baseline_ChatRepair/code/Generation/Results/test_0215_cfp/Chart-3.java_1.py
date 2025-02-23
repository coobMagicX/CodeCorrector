public TimeSeries createCopy(int start, int end) throws CloneNotSupportedException {
    if (start < 0) {
        throw new IllegalArgumentException("Requires start >= 0.");
    }
    if (end >= this.data.size()) {
        throw new IllegalArgumentException("End index must be less than the size of the data series.");
    }
    if (end < start) {
        throw new IllegalArgumentException("Requires start <= end.");
    }
    
    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new java.util.ArrayList<>(end - start + 1);
    
    for (int index = start; index <= end; index++) {
        TimeSeriesDataItem item = (TimeSeriesDataItem) this.data.get(index);
        TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
        copy.data.add(clone);
    }
    
    return copy;
}

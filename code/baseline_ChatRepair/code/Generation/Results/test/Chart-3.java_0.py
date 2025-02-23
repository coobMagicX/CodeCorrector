public TimeSeries createCopy(int start, int end) throws CloneNotSupportedException {
    if (start < 0) {
        throw new IllegalArgumentException("Requires start >= 0.");
    }
    if (end >= data.size()) {
        throw new IllegalArgumentException("End index must be less than the size of the data series.");
    }
    if (end < start) {
        throw new IllegalArgumentException("Requires start <= end.");
    }

    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new java.util.ArrayList();
    
    for (int index = start; index <= end; index++) {
        TimeSeriesDataItem item = (TimeSeriesDataItem) this.data.get(index).clone();
        copy.data.add(item);  // Directly adding cloned item to copy's data list
    }
    
    return copy;
}

public TimeSeries createCopy(int start, int end)
    throws CloneNotSupportedException {
    if (start < 0) {
        throw new IllegalArgumentException("Start index must be non-negative.");
    }
    if (end < start) {
        throw new IllegalArgumentException("End index must be greater than or equal to start index.");
    }
    TimeSeries copy = (TimeSeries) super.clone();
    List<TimeSeriesDataItem> newData = new ArrayList<>();
    for (int index = start; index <= end; index++) {
        if (index < this.data.size()) {
            TimeSeriesDataItem item = this.data.get(index);
            TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
            newData.add(clone);
        }
    }
    copy.data = newData;
    return copy;
}
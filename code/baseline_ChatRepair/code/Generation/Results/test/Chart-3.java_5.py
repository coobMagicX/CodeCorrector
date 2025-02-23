public TimeSeries createCopy(int start, int end) throws CloneNotSupportedException {
    if (start < 0) {
        throw new IllegalArgumentException("Requires start >= 0.");
    }
    if (end < start) {
        throw new IllegalArgumentException("Requires start <= end.");
    }
    if (start >= this.data.size()) {
        throw new IllegalArgumentException("Start index exceeds data size.");
    }
    if (end >= this.data.size()) {
        end = this.data.size() - 1; // Adjust end to the last index in the data
    }

    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new ArrayList();

    for (int index = start; index <= end; index++) {
        TimeSeriesDataItem item = (TimeSeriesDataItem) this.data.get(index);
        TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
        copy.data.add(clone);
    }

    return copy;
}

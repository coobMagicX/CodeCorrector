public TimeSeries createCopy(int start, int end)
throws CloneNotSupportedException {
    if (start < 0) {
        throw new IllegalArgumentException("Requires start >= 0.");
    }
    if (end < start) {
        // fix edge case where end index is less than start index
        if (this.data.size() > 0 && this.data.get(start) != null) {
            TimeSeriesDataItem item = this.data.get(start);
            TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
            copy.add(clone);
        }
        return copy;
    } else if (end == start) {
        // handle case where end index is equal to start index
        if (this.data.size() > 0 && this.data.get(start) != null) {
            TimeSeriesDataItem item = this.data.get(start);
            TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
            copy.add(clone);
        }
    }

    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new java.util.ArrayList();
    if (this.data.size() > 0) {
        for (int index = start; index <= end; index++) {
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
    }
    return copy;
}
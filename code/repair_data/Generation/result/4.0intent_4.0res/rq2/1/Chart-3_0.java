public TimeSeries createCopy(int start, int end)
        throws CloneNotSupportedException {
    if (start < 0 || start >= this.data.size()) {
        throw new IllegalArgumentException("Requires 0 <= start < size of data.");
    }
    if (end < start || end >= this.data.size()) {
        throw new IllegalArgumentException("Requires start <= end < size of data.");
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
                copy.updateBoundsForAddedItem(clone);
            }
            catch (SeriesException e) {
                e.printStackTrace();
            }
        }
    }
    copy.minY = Double.NaN;
    copy.maxY = Double.NaN;
    for (TimeSeriesDataItem item : copy.data) {
        copy.updateBoundsForAddedItem(item);
    }
    return copy;
}
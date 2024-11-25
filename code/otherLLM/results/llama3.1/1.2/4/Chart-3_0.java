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
    if (this.data.size() > 0 && start <= this.data.size()) {
        for (int index = Math.max(start, 0); index <= Math.min(end, this.data.size()); index++) {
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
    // Update minY and maxY for the copied time series
    if (!copy.data.isEmpty()) {
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;
        for (TimeSeriesDataItem item : copy.data) {
            minY = Math.min(minY, item.getY());
            maxY = Math.max(maxY, item.getY());
        }
        copy.minY = minY;
        copy.maxY = maxY;
    }
    return copy;
}
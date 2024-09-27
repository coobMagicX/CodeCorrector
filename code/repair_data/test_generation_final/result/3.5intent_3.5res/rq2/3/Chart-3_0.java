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
    copy.minY = Double.POSITIVE_INFINITY; // Initialize minimum value
    copy.maxY = Double.NEGATIVE_INFINITY; // Initialize maximum value
    if (this.data.size() > 0) {
        for (int index = start; index <= end; index++) {
            TimeSeriesDataItem item = (TimeSeriesDataItem) this.data.get(index);
            TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
            try {
                copy.add(clone);
                double y = clone.getValue().doubleValue();
                if (y < copy.minY) {
                    copy.minY = y;
                }
                if (y > copy.maxY) {
                    copy.maxY = y;
                }
            }
            catch (SeriesException e) {
                e.printStackTrace();
            }
        }
    }
    return copy;
}
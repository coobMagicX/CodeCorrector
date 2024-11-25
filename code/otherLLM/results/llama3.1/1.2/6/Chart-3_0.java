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
    if (this.data.size() > 0) {
        for (int index = Math.max(start, 0); index <= Math.min(end, this.data.size() - 1); index++) {
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
    int minStartIndex = Math.max(start, 0);
    int maxEndIndex = Math.min(end, this.data.size() - 1);
    double minY = Double.POSITIVE_INFINITY;
    double maxY = Double.NEGATIVE_INFINITY;
    for (int index = minStartIndex; index <= maxEndIndex; index++) {
        TimeSeriesDataItem item
                = (TimeSeriesDataItem) this.data.get(index);
        if (item.getY() < minY)
            minY = item.getY();
        if (item.getY() > maxY)
            maxY = item.getY();
    }
    copy.minY = minY;
    copy.maxY = maxY;
    return copy;
}
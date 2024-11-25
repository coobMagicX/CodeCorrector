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
        for (int index = start; index <= Math.min(end, this.data.size()); index++) {
            TimeSeriesDataItem item
                    = (TimeSeriesDataItem) this.data.get(index);
            TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
            try {
                copy.add(clone.getTimePeriod(), clone.getValue(), false); // add method added here
            }
            catch (SeriesException e) {
                e.printStackTrace();
            }
        }
    }
    return copy;
}
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
        for (int index = start; index <= end; index++) {
            if (index < this.data.size()) { // Check if index is within bounds
                TimeSeriesDataItem item = (TimeSeriesDataItem) this.data.get(index);
                TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
                try {
                    copy.add(clone);
                    copy.updateBoundsForAddedItem(clone); // Update bounds for the added item
                } catch (SeriesException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    return copy;
}
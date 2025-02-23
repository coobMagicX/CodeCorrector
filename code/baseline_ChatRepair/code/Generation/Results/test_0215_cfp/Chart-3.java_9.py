public TimeSeries createCopy(int start, int end) throws CloneNotSupportedException {
    // Check for valid start index
    if (start < 0) {
        throw new IllegalArgumentException("Requires start >= 0.");
    }
    // Ensure end is not less than start
    if (end < start) {
        throw new IllegalArgumentException("Requires start <= end.");
    }
    // The start and end indices must be within the bounds of the data list
    if (end >= this.data.size() || start >= this.data.size()) {
        throw new IndexOutOfBoundsException("Start or end index out of bound.");
    }

    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new java.util.ArrayList();
    
    if (this.data.size() > 0) {
        for (int index = start; index <= end; index++) {
            TimeSeriesDataItem item = (TimeSeriesDataItem) this.data.get(index);
            TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
            try {
                copy.add(clone);
            } catch (SeriesException e) {
                e.printStackTrace();
            }
        }
    }
    return copy;
}

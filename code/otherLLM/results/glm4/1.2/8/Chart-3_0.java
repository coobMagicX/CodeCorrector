public TimeSeries createCopy(int start, int end)
        throws CloneNotSupportedException {
    if (start < 0) {
        throw new IllegalArgumentException("Requires start >= 0.");
    }
    if (end < start) {
        throw new IllegalArgumentException("Requires start <= end.");
    }

    // Retrieve the segment of data to copy
    java.util.ArrayList<TimeSeriesDataItem> copiedSegment = this.data.subList(start, end + 1);

    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new java.util.ArrayList<>();

    for (TimeSeriesDataItem item : copiedSegment) {
        try {
            TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
            copy.add(clone);
        } catch (CloneNotSupportedException | SeriesException e) {
            e.printStackTrace();
        }
    }

    return copy;
}
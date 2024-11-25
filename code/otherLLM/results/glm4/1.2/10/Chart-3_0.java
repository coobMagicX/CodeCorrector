public TimeSeries createCopy(int start, int end)
throws CloneNotSupportedException {
    if (start < 0) {
        throw new IllegalArgumentException("Start index cannot be less than zero.");
    }
    if (end > this.data.size() || end < start) {
        throw new IllegalArgumentException("Invalid range: start must be less than or equal to end and within data bounds.");
    }
    
    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new java.util.ArrayList<>();
    List<TimeSeriesDataItem> itemsToCopy = this.data.subList(start, Math.min(end + 1, this.data.size()));

    for (TimeSeriesDataItem item : itemsToCopy) {
        TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
        try {
            copy.add(clone);
        } catch (SeriesException e) {
            e.printStackTrace();
        }
    }

    // Initialize min and max values only if the copy is not empty
    if (!copy.data.isEmpty()) {
        copy.minValue = itemsToCopy.stream().mapToInt(TimeSeriesDataItem::getValue).min().getAsInt();
        copy.maxValue = itemsToCopy.stream().mapToInt(TimeSeriesDataItem::getValue).max().getAsInt();
    }

    return copy;
}
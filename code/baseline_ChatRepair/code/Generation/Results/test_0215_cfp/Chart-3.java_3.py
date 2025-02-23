public TimeSeries createCopy(int start, int end) throws CloneNotSupportedException {
    if (start < 0) {
        throw new IllegalArgumentException("Requires start >= 0.");
    }
    if (end < start) {
        throw new IllegalArgumentException("Requires start <= end.");
    }
    
    // Check to not exceed the data bounds
    if (end >= data.size()) {
        throw new IllegalArgumentException("End index exceeds data size.");
    }

    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new java.util.ArrayList<>();

    for (int index = start; index <= end; index++) {
        TimeSeriesDataItem item = (TimeSeriesDataItem) this.data.get(index).clone();
        // Ensure items are truly cloned to avoid side-effects
        try {
            copy.add(item);
        } catch (SeriesException e) {
            System.err.println("Failed to add item: " + e.getMessage());
            e.printStackTrace();
        }
    }
    return copy;
}

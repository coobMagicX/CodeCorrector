public TimeSeries createCopy(int start, int end)
        throws CloneNotSupportedException {
    if (start < 0) {
        throw new IllegalArgumentException("Start index cannot be negative.");
    }
    if (end >= this.data.size() || end < start) {
        throw new IllegalArgumentException("End index must be within the series range and not less than start index.");
    }
    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new java.util.ArrayList<>();

    // Adjusting the loop to ensure it does not go out of bounds
    for (int index = start; index < end; index++) {
        try {
            TimeSeriesDataItem item = this.data.get(index);
            TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
            copy.add(clone); // Assuming add() is a method in the TimeSeries class that adds an element to the series
        } catch (CloneNotSupportedException e) {
            throw new CloneNotSupportedException("Failed to create a deep copy of the data item.");
        } catch (SeriesException e) {
            e.printStackTrace();
        }
    }
    return copy;
}
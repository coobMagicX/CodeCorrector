public TimeSeries createCopy(int start, int end)
throws CloneNotSupportedException {
    if (start < 0) {
        throw new IllegalArgumentException("Start index cannot be negative.");
    }
    if (end < start) {
        throw new IllegalArgumentException("End index must not be less than the start index.");
    }
    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new java.util.ArrayList<>();
    
    // Correcting the index increment inside the loop to ensure we're adding items up to and including 'end'
    for (int index = start; index <= end; index++) {
        TimeSeriesDataItem item = (TimeSeriesDataItem) this.data.get(index);
        if (item != null) { // Adding a check to prevent NullPointerException
            TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
            try {
                copy.add(clone); // Ensure that the add method is correctly called
            } catch (SeriesException e) {
                e.printStackTrace(); // It's generally better to log the exception or throw an appropriate error, but for simplicity it's printed here.
            }
        }
    }
    
    return copy;
}
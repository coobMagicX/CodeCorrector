public TimeSeries createCopy(int start, int end)
        throws CloneNotSupportedException {
    if (start < 0) {
        throw new IllegalArgumentException("Requires start >= 0.");
    }
    if (end < start) {
        throw new IllegalArgumentException("Requires start <= end.");
    }
    
    // Clone the TimeSeries object
    TimeSeries copy = (TimeSeries) super.clone();
    
    // Initialize a new list for the cloned data items
    copy.data = new java.util.ArrayList<>();
    
    // Calculate the min and max values before adding data to ensure they are correct
    Double minValue = null;
    Double maxValue = null;

    if (this.data.size() > 0) {
        // Iterate over the specified range to calculate min and max values
        for (int index = start; index <= end; index++) {
            TimeSeriesDataItem item = (TimeSeriesDataItem) this.data.get(index);
            
            // Clone the data item
            TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
            
            // Add the cloned item to the copy's list
            try {
                copy.add(clone); // Assuming add() method is correct
            }
            catch (SeriesException e) {
                e.printStackTrace();
            }

            // Update min and max values
            if (minValue == null || clone.getValue() < minValue) {
                minValue = clone.getValue();
            }
            if (maxValue == null || clone.getValue() > maxValue) {
                maxValue = clone.getValue();
            }
        }
    }
    
    // Set the calculated min and max values to the new TimeSeries instance
    copy.setMin(minValue);
    copy.setMax(maxValue);

    return copy;
}
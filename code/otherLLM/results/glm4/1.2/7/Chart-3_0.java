public TimeSeries createCopy(int start, int end)
        throws CloneNotSupportedException {
    if (start < 0) {
        throw new IllegalArgumentException("Requires start >= 0.");
    }
    if (end < start) {
        throw new IllegalArgumentException("Requires start <= end.");
    }
    TimeSeries copy = (TimeSeries) super.clone();
    
    // Reset the internal list to avoid sharing references with the original
    copy.data = new java.util.ArrayList<>(this.data.size());
    
    double min = Double.MAX_VALUE;
    double max = Double.MIN_VALUE;

    for (int index = start; index <= end; index++) {
        TimeSeriesDataItem item = this.data.get(index);
        
        // Check if we have not reached the end of the list to avoid ArrayIndexOutOfBoundsException
        if (index < this.data.size()) {
            TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
            
            // Initialize min and max with the cloned item's value
            double value = clone.getValue(); // Assuming getValue() is a method that returns the value of the data item
            if (value < min) {
                min = value;
            }
            if (value > max) {
                max = value;
            }

            try {
                copy.add(clone);
            } catch (SeriesException e) {
                e.printStackTrace();
            }
        }
    }
    
    // Set the minimum and maximum values in the new TimeSeries instance
    copy.setMin(min);
    copy.setMax(max);

    return copy;
}
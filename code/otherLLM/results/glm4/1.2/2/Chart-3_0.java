public TimeSeries createCopy(int start, int end)
        throws CloneNotSupportedException {
    if (start < 0) {
        throw new IllegalArgumentException("Start index cannot be negative.");
    }
    if (end >= this.data.size()) {
        throw new IllegalArgumentException("End index is out of bounds.");
    }
    TimeSeries copy = (TimeSeries) super.clone();
    // Ensure the data ArrayList is empty before adding new items
    copy.data.clear();

    // Iterate over the specified range to create a deep copy
    for (int index = start; index <= end; index++) {
        TimeSeriesDataItem item = this.data.get(index);
        // Clone the item and add it to the copy's data ArrayList
        try {
            TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
            // Check if cloning was successful before adding to the copy
            if (clone != null) {
                copy.add(clone);
            }
        } catch (CloneNotSupportedException e) {
            throw new CloneNotSupportedException("Failed to clone TimeSeriesDataItem.");
        } catch (SeriesException e) {
            e.printStackTrace();
        }
    }

    // Update the min and max values after the deep copy is complete
    updateMinMax(copy);

    return copy;
}

// Helper method to update the min and max values of the copied TimeSeries object
private void updateMinMax(TimeSeries series) {
    if (series.data.isEmpty()) {
        return; // No need to calculate if there's no data
    }
    
    double min = Double.MAX_VALUE;
    double max = Double.MIN_VALUE;

    for (TimeSeriesDataItem item : series.data) {
        if (item.value < min) {
            min = item.value;
        }
        if (item.value > max) {
            max = item.value;
        }
    }

    // Update the copy's min and max values
    series.minValue = min;
    series.maxValue = max;
}
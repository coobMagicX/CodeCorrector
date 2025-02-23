public TimeSeries createCopy(int start, int end) throws CloneNotSupportedException {
    // Input validation
    if (start < 0 || start >= this.data.size()) {
        throw new IllegalArgumentException("Requires start index to be within data bounds.");
    }
    if (end < start || end >= this.data.size()) {
        throw new IllegalArgumentException("Requires end index to be within data bounds and not before start.");
    }

    // Clone top-level TimeSeries structure
    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new ArrayList<>();

    // Add items from specified range in the source to the new copy
    for (int index = start; index <= end; index++) {
        TimeSeriesDataItem item = this.data.get(index);
        TimeSeriesDataItem itemClone = (TimeSeriesDataItem) item.clone();
        
        // when adding the cloned item, address any side effects
        copy.addOrUpdate(itemClone.getPeriod(), itemClone.getValue());
    }
    return copy;
}

// Assume addOrUpdate() is a method in TimeSeries that adds a data item
// or updates it if an item with the same period exists. Remove this assumption
// if your use case differs or implement if necessary.

public TimeSeries createCopy(int start, int end)
        throws CloneNotSupportedException {
    if (start < 0) {
        throw new IllegalArgumentException("Requires start >= 0.");
    }
    if (end < start) {
        throw new IllegalArgumentException("Requires start <= end.");
    }
    TimeSeries copy = (TimeSeries) super.clone();
    copy.data = new java.util.ArrayList<>();
    
    // Adjusting the index to be zero-based for ArrayList
    int adjustedStart = start;
    int adjustedEnd = end + 1; // Adding 1 because end is inclusive
    
    if (this.data.size() > 0) {
        for (int index = adjustedStart; index < adjustedEnd; index++) { // Changed condition to index < adjustedEnd
            TimeSeriesDataItem item = this.data.get(index);
            TimeSeriesDataItem clone = (TimeSeriesDataItem) item.clone();
            copy.add(clone); // Removed try-catch block, assuming add method handles exceptions
        }
    }
    
    return copy;
}
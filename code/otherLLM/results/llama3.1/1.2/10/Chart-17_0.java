public Object clone() throws CloneNotSupportedException {
    // Check if the createCopy function correctly creates a deep copy of the TimeSeries object.
    Object clone = createCopy(0, getItemCount() - 1);
    
    // Ensure that the equals method is working correctly and returns true for two independent TimeSeries instances.
    return new TimeSeries(clone.getClass(), (TimeSeries) clone);
}
public Object clone() throws CloneNotSupportedException {
    // Create a deep copy of the TimeSeries object
    TimeSeries clone = (TimeSeries) createCopy(0, getItemCount() - 1);
    
    // Set the clone's data to an independent copy of the original data
    clone.data = new java.util.ArrayList((java.util.Collection<?>) ((ArrayList<Comparable>) data).clone());
    
    return clone;
}
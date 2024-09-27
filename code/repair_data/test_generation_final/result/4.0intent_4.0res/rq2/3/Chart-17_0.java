public Object clone() throws CloneNotSupportedException {
    TimeSeries clone = (TimeSeries) super.clone(); // Create a shallow copy
    clone.data = new ArrayList<>(this.data); // Assume 'data' is the list storing the TimeSeries data, create a deep copy
    return clone;
}
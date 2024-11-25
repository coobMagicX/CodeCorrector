public Object clone() throws CloneNotSupportedException {
    // Call the createCopy method directly to ensure it's used consistently.
    // Assuming createCopy takes indices as parameters for potential partial cloning,
    // we pass 0 and getItemCount() - 1 to copy the entire series.
    TimeSeries original = this; // Store reference to the original object
    Object clone = createCopy(0, original.getItemCount() - 1);
    
    // If the TimeSeries class has its own specific deep cloning logic that needs to be applied,
    // it would be implemented here. This is a placeholder for such logic.
    if (original.isDeepCloneRequired()) {
        // Perform deep clone operations
        // ...
    }
    
    return clone;
}
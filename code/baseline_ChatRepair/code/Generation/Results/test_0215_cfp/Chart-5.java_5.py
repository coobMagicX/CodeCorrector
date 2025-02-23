public XYDataItem addOrUpdate(Number x, Number y) {
    if (x == null) {
        throw new IllegalArgumentException("Null 'x' argument.");
    }

    // Initialize the variable to hold the possibly overwritten item
    XYDataItem overwritten = null;
    
    // Find the index of the item with X value if exists 
    int index = indexOf(x);
    if (index >= 0) {  // Found the item
        XYDataItem existing = (XYDataItem) this.data.get(index);
        overwritten = new XYDataItem(existing.getX(), existing.getY());  // Copy the old item
        
        // Set the new y value
        existing.setY(y);
    } else { // Not found, need to add new
        // Auto sort could affect insertion point
        if (this.autoSort) {
            index = -index - 1;  // Calculate correct insert position if sorted
        } else {
            index = this.data.size();  // Append at the end if not sorted
        }
        
        // Add the new data item
        this.data.add(index, new XYDataItem(x, y));
        
        // Enforce maximum item count constraint
        if (this.data.size() > this.maximumItemCount) {
            this.data.remove(0);
        }
    }

    // Notify listeners about the change
    fireSeriesChanged();
    
    // Return the overwritten item (null if none was overwritten)
    return overwritten;
}

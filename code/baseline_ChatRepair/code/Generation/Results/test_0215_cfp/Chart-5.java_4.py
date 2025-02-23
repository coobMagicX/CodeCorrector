public XYDataItem addOrUpdate(Number x, Number y) {
    if (x == null) {
        throw new IllegalArgumentException("Null 'x' argument.");
    }

    XYDataItem overwritten = null;
    int index = indexOf(x);
    if (index >= 0) {  // existing X value is found
        XYDataItem existing = (XYDataItem) this.data.get(index);
        try {
            // Clone the existing item to return as the overwritten value
            overwritten = (XYDataItem) existing.clone();
        } catch (CloneNotSupportedException e) {
            throw new SeriesException("Couldn't clone XYDataItem!");
        }
        // Update the y-value of the existing item
        existing.setY(y);
    } else { // new X value
        // Calculate correct insertion index
        int insertIndex = -index - 1;

        // Insert the new item in the list
        this.data.add(insertIndex, new XYDataItem(x, y));

        // check if this addition will exceed the maximum item count...
        if (getItemCount() > this.maximumItemCount) {
            this.data.remove(0);
        }
    }
    // Notify listeners that the series has changed
    fireSeriesChanged();
    return overwritten;
}

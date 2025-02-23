public XYDataItem addOrUpdate(Number x, Number y) {
    if (x == null) {
        throw new IllegalArgumentException("Null 'x' argument.");
    }

    XYDataItem overwritten = null;
    int index = indexOf(x);
    if (index >= 0) {  // No check required for allowDuplicateXValues for assuming it intends always to be false in this context
        XYDataItem existing = (XYDataItem) this.data.get(index);
        try {
            overwritten = (XYDataItem) existing.clone(); // This clone will have the old Y value
        } catch (CloneNotSupportedException e) {
            throw new SeriesException("Couldn't clone XYDataItem!");
        }
        existing.setY(y); // Here the Y value of the existing point is updated
    } else {
        // Auto sorting or appending to the end depends on this.autoSort.
        if (this.autoSort) {
            index = -(index + 1);  // Converts the index to a positive value where the item should be inserted
        } else {
            index = this.data.size();  // Appends at the end of the list.
        }
        this.data.add(index, new XYDataItem(x, y));
        
        // Check if this addition will exceed the maximum item count...
        if (getItemCount() > this.maximumItemCount) {
            this.data.remove(0);
        }
    }
    fireSeriesChanged(); // Notify listeners of update.
    return overwritten;
}

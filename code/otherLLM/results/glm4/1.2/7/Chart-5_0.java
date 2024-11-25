public XYDataItem addOrUpdate(Number x, Number y) {
    if (x == null) {
        throw new IllegalArgumentException("Null 'x' argument.");
    }

    // if we get to here, we know that duplicate X values are not permitted
    XYDataItem overwritten = null;
    int index = indexOf(x);
    if (index >= 0 && !this.allowDuplicateXValues) {
        XYDataItem existing = (XYDataItem) this.data.get(index);
        try {
            overwritten = (XYDataItem) existing.clone();
        } catch (CloneNotSupportedException e) {
            throw new SeriesException("Couldn't clone XYDataItem!");
        }
        // Update the existing item without removing it, only if we are allowed to overwrite
        existing.setY(y);
    } else {
        // Handle the case where x value is not found or duplicates are allowed
        boolean inserted = false;
        if (index < 0) { // -1 index means we need to insert at the end of the sorted list
            this.data.add(new XYDataItem(x, y));
            inserted = true;
        } else if (this.autoSort && index >= 0) {
            // Insert into the sorted list at the correct position
            this.data.add(-index - 1, new XYDataItem(x, y));
            inserted = true;
        }
        
        // Check for exceeding maximum item count after insertion
        if (inserted && getItemCount() > this.maximumItemCount) {
            this.data.remove(0); // Remove oldest entry to maintain the limit
        }
    }

    fireSeriesChanged();
    return overwritten;
}
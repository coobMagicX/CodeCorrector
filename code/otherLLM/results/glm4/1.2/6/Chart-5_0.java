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
        // Update the existing item without creating a new one
        existing.setY(y);
    } else {
        if (index >= 0 || !this.allowDuplicateXValues) {
            // If not allowing duplicates, update the existing item if found
            XYDataItem existing = (XYDataItem) this.data.get(index);
            existing.setY(y);
        } else {
            // Add a new item if no duplicate is found and it's allowed or if the index is negative (new item to be added)
            if (this.autoSort) {
                this.data.add(-index - 1, new XYDataItem(x, y));
            } else {
                this.data.add(new XYDataItem(x, y));
            }
            // Check for maximum item count and remove oldest item if necessary
            if (getItemCount() > this.maximumItemCount) {
                this.data.remove(0);
            }
        }
    }
    fireSeriesChanged();
    return overwritten;
}
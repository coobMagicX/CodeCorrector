public XYDataItem addOrUpdate(Number x, Number y) {
    if (x == null) {
        throw new IllegalArgumentException("Null 'x' argument.");
    }

    XYDataItem overwritten = null;
    int index = indexOf(x);
    if (index >= 0) {
        if (!this.allowDuplicateXValues) {
            // Use the existing update method to handle updating the item
            update(x, y);
            XYDataItem existing = (XYDataItem) this.data.get(index);
            try {
                overwritten = (XYDataItem) existing.clone();
            }
            catch (CloneNotSupportedException e) {
                throw new SeriesException("Couldn't clone XYDataItem!");
            }
        }
        // This part ensures that if duplicate X values are allowed, the item is added and not updated
        else {
            this.data.add(new XYDataItem(x, y));
        }
    }
    else {
        if (this.autoSort) {
            this.data.add(-index - 1, new XYDataItem(x, y));
        } else {
            this.data.add(new XYDataItem(x, y));
        }
    }
    // Check if this addition will exceed the maximum item count
    if (getItemCount() > this.maximumItemCount) {
        this.data.remove(0);
    }
    fireSeriesChanged();
    return overwritten;
}
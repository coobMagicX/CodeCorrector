public XYDataItem addOrUpdate(Number x, Number y) {
    if (x == null) {
        throw new IllegalArgumentException("Null 'x' argument.");
    }

    XYDataItem overwritten = null;
    int index = indexOf(x);
    if (index >= 0) {
        if (!this.allowDuplicateXValues) {
            // Retrieve existing item to return as overwritten
            XYDataItem existing = getDataItem(index);
            try {
                overwritten = (XYDataItem) existing.clone();
            } catch (CloneNotSupportedException e) {
                throw new SeriesException("Couldn't clone XYDataItem!");
            }
            // Use update method to handle existing item updates
            update(x, y);
        } else {
            // Handling duplicate values when allowed
            this.data.add(new XYDataItem(x, y));
        }
    } else {
        // Handling sorted insertion or append
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
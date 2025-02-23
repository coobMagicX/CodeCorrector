public XYDataItem addOrUpdate(Number x, Number y) {
    if (x == null) {
        throw new IllegalArgumentException("Null 'x' argument.");
    }

    XYDataItem overwritten = null;
    int index = indexOf(x);
    if (index >= 0 && !this.allowDuplicateXValues) {
        XYDataItem existing = (XYDataItem) this.data.get(index);
        try {
            overwritten = (XYDataItem) existing.clone();
        } catch (CloneNotSupportedException e) {
            throw new SeriesException("Couldn't clone XYDataItem!");
        }
        existing.setY(y);
    } else {
        // Create a new XYDataItem
        XYDataItem newItem = new XYDataItem(x, y);
        if (this.autoSort) {
            if (index == -1)
                index = 0; // If index is -1, it means there are no items yet.
            else
                index = -index - 1; // Calculate proper insertion point
            this.data.add(index, newItem);
        } else {
            this.data.add(newItem);
        }
        
        // Now check if this addition exceeded the maximum item count...
        if (getItemCount() > this.maximumItemCount) {
            this.data.remove(0); // Remove the oldest item
        }
    }
    fireSeriesChanged();
    return overwritten;
}

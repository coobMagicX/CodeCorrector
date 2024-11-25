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
        updateByIndex(index, y); // Use updateByIndex method to set the new Y value
    } else if (index < 0 || this.autoSort) { // Adjust condition for auto-sorting behavior
        if (this.autoSort && index == Collections.binarySearch(this.data, x)) {
            this.data.add(-index - 1, new XYDataItem(x, y));
        } else {
            this.data.add(new XYDataItem(x, y));
        }
        fireSeriesChanged();
        // Check for exceeding maximum item count after adding the new item
        if (getItemCount() > this.maximumItemCount) {
            setMaximumItemCount(this.maximumItemCount); // Use setMaximumItemCount to maintain the list size
        }
    } else {
        // If not allowed duplicates and not auto-sorting, just update without changing order
        existing = (XYDataItem) this.data.get(index);
        try {
            overwritten = (XYDataItem) existing.clone();
        } catch (CloneNotSupportedException e) {
            throw new SeriesException("Couldn't clone XYDataItem!");
        }
        existing.setY(y);
    }

    return overwritten;
}
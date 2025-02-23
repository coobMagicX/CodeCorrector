public XYDataItem addOrUpdate(Number x, Number y) {
    if (x == null) {
        throw new IllegalArgumentException("Null 'x' argument.");
    }

    XYDataItem overwritten = null;
    int index = indexOf(x);
    if (index >= 0) {  // This means 'x' already exists in the data.
        // Attempt to clone the existing item which has the old Y value, so that it can be returned unchanged.
        XYDataItem existing = (XYDataItem) this.data.get(index);
        try {
            overwritten = (XYDataItem) existing.clone();
        } catch (CloneNotSupportedException e) {
            throw new SeriesException("Couldn't clone XYDataItem!");
        }
        // Do not update existing.setY(y), as we no longer want to update the Y-value for duplicates.
    } else {
        // Handling for when 'x' does not already exist in the data.
        if (this.autoSort) {
            index = -(index + 1);  // Correction for insertion when autoSorted
            this.data.add(index, new XYDataItem(x, y));
        } else {
            this.data.add(new XYDataItem(x, y));  // Append to the end if not sorting.
        }

        // Check if this addition will exceed the maximum item count...
        if (getItemCount() > this.maximumItemCount) {
            this.data.remove(0);
        }
    }
    fireSeriesChanged();
    return overwritten;
}

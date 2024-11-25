public XYDataItem addOrUpdate(Number x, Number y) {
    if (x == null) {
        throw new IllegalArgumentException("Null 'x' argument.");
    }

    // if duplicate X values are allowed, we do not need to search for an existing item.
    if (this.allowDuplicateXValues) {
        if (this.autoSort) {
            int insertionIndex = Collections.binarySearch(this.data, new XYDataItem(x, y), this.comparator);
            if (insertionIndex < 0) {
                insertionIndex = -insertionIndex - 1;
            }
            this.data.add(insertionIndex, new XYDataItem(x, y));
        } else {
            this.data.add(new XYDataItem(x, y));
        }
    } else {
        // if we get to here, we know that duplicate X values are not permitted
        int index = indexOf(x);
        if (index >= 0) { // Found the existing item at index
            XYDataItem existing = (XYDataItem) this.data.get(index);
            try {
                XYDataItem overwritten = (XYDataItem) existing.clone();
                existing.setY(y); // Update the Y value of the existing item
                return overwritten; // Return the cloned item before it was updated
            } catch (CloneNotSupportedException e) {
                throw new SeriesException("Couldn't clone XYDataItem!");
            }
        } else { // No duplicate X values are found, add a new one
            if (this.autoSort) {
                int insertionIndex = Collections.binarySearch(this.data, new XYDataItem(x, y), this.comparator);
                if (insertionIndex < 0) {
                    insertionIndex = -insertionIndex - 1;
                }
                this.data.add(insertionIndex, new XYDataItem(x, y));
            } else {
                this.data.add(new XYDataItem(x, y));
            }
        }
    }

    fireSeriesChanged();

    // If we're not allowing duplicates, return the updated existing item
    if (!this.allowDuplicateXValues && index >= 0) {
        return (XYDataItem) this.data.get(index);
    }
    
    // If duplicates are allowed or the new item is added, there's no item to overwrite
    return null;
}
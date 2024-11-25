public XYDataItem addOrUpdate(Number x, Number y) {
    if (x == null) {
        throw new IllegalArgumentException("Null 'x' argument.");
    }

    // if we get to here, we know that duplicate X values are not permitted
    int index = indexOf(x);
    if (index >= 0 && !this.allowDuplicateXValues) {
        XYDataItem existing = (XYDataItem) this.data.get(index);
        try {
            XYDataItem overwritten = (XYDataItem) existing.clone();
            // Update the existing item with new y value instead of creating a new one
            existing.setY(y);
            return overwritten;
        } catch (CloneNotSupportedException e) {
            throw new SeriesException("Couldn't clone XYDataItem!");
        }
    }

    if (index < 0 && !this.allowDuplicateXValues) {
        // If the index is negative and duplicate x values are not allowed, this should be an error.
        throw new SeriesException("Attempt to add a duplicate x value to the series.");
    }

    // Add or update the item in the data structure
    if (index >= 0 && y != null) {
        ((XYDataItem) this.data.get(index)).setY(y);
    } else {
        XYDataItem newItem = new XYDataItem(x, y == null ? 0 : y.longValue());
        if (this.autoSort) {
            // Adjust the index to be positive for insertion into a sorted list
            index = -index - 1;
        }
        this.data.add(index, newItem);
    }

    // If the series is full, remove the oldest item
    if (getItemCount() > this.maximumItemCount) {
        this.data.remove(0);
    }

    fireSeriesChanged();
    return null; // Return null since there's no overwritten item if not updating an existing one
}

// ... rest of the class ...
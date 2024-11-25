public XYDataItem addOrUpdate(Number x, Number y) {
    if (x == null) {
        throw new IllegalArgumentException("Null 'x' argument.");
    }

    // if we get to here, we know that duplicate X values are not permitted
    int index = indexOf(x);
    if (index >= 0 && !this.allowDuplicateXValues) {
        XYDataItem existing = (XYDataItem) this.data.get(index);
        throw new SeriesException("Duplicate x value is not allowed: " + x);
    } else {
        // Handle the case where we need to add a new item or update an existing one
        if (index < 0 || this.allowDuplicateXValues) {
            if (this.autoSort) {
                index = Collections.binarySearch(this.data, new XYDataItem(x, y), comparator);
                if (index < 0) {
                    index = -(index + 1);
                }
                this.data.add(index, new XYDataItem(x, y));
            } else {
                // If not auto-sorting, simply add the item to the end
                this.data.add(new XYDataItem(x, y));
            }

            // check if this addition will exceed the maximum item count...
            while (getItemCount() > getMaximumItemCount()) {
                this.data.remove(0); // Assuming data is a List and we remove from the start for simplicity
            }
        } else {
            // Update existing item with new y value
            XYDataItem existing = (XYDataItem) this.data.get(index);
            existing.setY(y);
        }
    }

    fireSeriesChanged();
    return null; // Return null as there's no need to return the overwritten item
}
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
        // Update the Y value for the existing item without removing it
        // to ensure itemCount does not decrease when updating an existing item
    } else {
        // if the series is sorted, the negative index is a result from
        // Collections.binarySearch() and tells us where to insert the
        // new item...otherwise it will be just -1 and we should just
        // append the value to the list...
        int insertionIndex = this.autoSort ? -(index + 1) : this.data.size();
        XYDataItem newItem = new XYDataItem(x, y);
        this.data.add(insertionIndex, newItem);

        // check if this addition will exceed the maximum item count...
        while (getItemCount() > this.maximumItemCount) {
            this.data.remove(0); // Remove from the beginning to preserve order
        }
    }

    fireSeriesChanged();
    return overwritten;
}
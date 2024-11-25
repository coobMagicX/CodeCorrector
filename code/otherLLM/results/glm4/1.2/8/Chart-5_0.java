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
            // We clone the existing item to keep it in case we want to overwrite
            overwritten = (XYDataItem) existing.clone();
        } catch (CloneNotSupportedException e) {
            throw new SeriesException("Couldn't clone XYDataItem!");
        }
        if (existing != overwritten) { // Only set Y if we're not cloning an already existing item with the same X value
            existing.setY(y);
        }
    } else {
        // if the series is sorted, the negative index is a result from
        // Collections.binarySearch() and tells us where to insert the
        // new item...otherwise it will be just -1 and we should just
        // append the value to the list...
        int insertionPoint = this.autoSort ? -index - 1 : this.data.size();
        if (insertionPoint >= 0) {
            XYDataItem newItem = new XYDataItem(x, y);
            this.data.add(insertionPoint, newItem);

            // check if this addition will exceed the maximum item count...
            if (getItemCount() > this.maximumItemCount) {
                this.data.remove(0); // Remove the first item to keep the size within limits
            }
        }
    }

    fireSeriesChanged();
    return overwritten;
}
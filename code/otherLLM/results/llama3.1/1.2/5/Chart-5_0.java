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
        }
        catch (CloneNotSupportedException e) {
            throw new SeriesException("Couldn't clone XYDataItem!");
        }
        existing.setY(y);
    }
    else if (!this.autoSort && getItemCount() >= getMaximumItemCount()) {
        // If the series is not sorted and adding a new item will exceed the maximum item count,
        // remove the first item to make room for the new one
        this.data.remove(0);
    }

    else {
        // if the series is sorted, the negative index is a result from
        // Collections.binarySearch() and tells us where to insert the
        // new item...otherwise it will be just -1 and we should just
        // append the value to the list...
        if (this.autoSort) {
            this.data.add(-index - 1, new XYDataItem(x, y));
        }
        else {
            this.data.add(new XYDataItem(x, y));
        }
    }
    fireSeriesChanged();
    return overwritten;
}
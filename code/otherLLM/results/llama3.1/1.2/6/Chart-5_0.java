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
        // Update the Y value of the existing item at the specified index
        existing.setY(y);
    }
    else if (this.autoSort) {
        this.data.add(-index - 1, new XYDataItem(x, y));
    }
    else {
        this.data.add(new XYDataItem(x, y));
    }
    // check if this addition will exceed the maximum item count...
    if (getItemCount() > this.maximumItemCount) {
        this.data.remove(0);
    }
    fireSeriesChanged();
    return overwritten;
}
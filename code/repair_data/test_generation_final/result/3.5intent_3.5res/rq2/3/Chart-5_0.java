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
        }
        catch (CloneNotSupportedException e) {
            throw new SeriesException("Couldn't clone XYDataItem!");
        }
        existing.setY(y);
        
        // Add a new item with the duplicate X value and new Y value
        this.data.add(index + 1, new XYDataItem(x, y));
        
        // Check if the addition will exceed the maximum item count
        if (getItemCount() > this.maximumItemCount) {
            this.data.remove(0);
        }
    }
    else {
        if (this.autoSort) {
            this.data.add(-index - 1, new XYDataItem(x, y));
        }
        else {
            this.data.add(new XYDataItem(x, y));
        }
        
        // Check if the addition will exceed the maximum item count
        if (getItemCount() > this.maximumItemCount) {
            this.data.remove(0);
        }
    }
    fireSeriesChanged();
    return overwritten;
}
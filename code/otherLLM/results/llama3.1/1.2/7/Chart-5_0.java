public XYDataItem addOrUpdate(Number x, Number y) {
    if (x == null) {
        throw new IllegalArgumentException("Null 'x' argument.");
    }

    // if we get to here, we know that duplicate X values are permitted and auto-sorting is enabled
    int index = indexOf(x);
    XYDataItem overwritten = null;
    if (index >= 0 && !this.autoSort) {
        XYDataItem existing = (XYDataItem) this.data.get(index);
        try {
            overwritten = (XYDataItem) existing.clone();
        }
        catch (CloneNotSupportedException e) {
            throw new SeriesException("Couldn't clone XYDataItem!");
        }
        existing.setY(y);
    } else if (index >= 0 && this.allowDuplicateXValues) {
        // duplicate X value, just update the existing item
        XYDataItem existing = (XYDataItem) this.data.get(index);
        existing.setY(y);
        fireSeriesChanged();
        return overwritten;
    }
    
    // if we get to here, we know that auto-sorting is enabled and there's no duplicate X value
    else {
        XYDataItem item = new XYDataItem(x, y);
        
        // check if this addition will exceed the maximum item count...
        if (getItemCount() >= this.maximumItemCount) {
            removeOldest();
            return overwritten;
        }
        
        // add the item at the correct position
        add(item);
        
        fireSeriesChanged();
        return overwritten;
    }
}

private void add(XYDataItem item) {
    int index = indexOf(item.getX());
    if (index >= 0 && this.autoSort) {
        this.data.add(-index - 1, item);
    } else {
        this.data.add(item);
    }
}
public XYDataItem addOrUpdate(Number x, Number y) {
    if (x == null) {
        throw new IllegalArgumentException("Null 'x' argument.");
    }

    // if we get to here, we know that duplicate X values are not permitted
    int index = indexOf(x);
    boolean isDuplicateAllowed = getAllowDuplicateXValues();
    XYDataItem overwritten = null;
    if (index >= 0 && !isDuplicateAllowed) {
        XYDataItem existing = (XYDataItem) this.data.get(index);
        try {
            overwritten = (XYDataItem) existing.clone();
        } catch (CloneNotSupportedException e) {
            throw new SeriesException("Couldn't clone XYDataItem!");
        }
        existing.setY(y); // Correctly update the y value without removing or altering other items
    } else if (index < 0 || isDuplicateAllowed) {
        // Only add a new item if it's not a duplicate and we're not updating an existing one
        this.data.add(new XYDataItem(x, y));
        if (!isDuplicateAllowed && indexOf(x) < 0) {
            overwritten = new XYDataItem(x, y); // Overwritten is null unless adding a new item that replaces an existing one
        }
        // check if this addition will exceed the maximum item count...
        if (getItemCount() > this.maximumItemCount) {
            this.data.remove(0);
        }
    }

    fireSeriesChanged();
    return overwritten;
}
public XYDataItem addOrUpdate(Number x, Number y) {
    if (x == null) {
        throw new IllegalArgumentException("Null 'x' argument.");
    }

    XYDataItem overwritten = null;
    int index = indexOf(x);
    if (index >= 0) {
        // Found the item, preparing to overwrite its Y value
        XYDataItem existing = (XYDataItem) this.data.get(index);
        try {
            // Cloning the old data item to return as overwritten
            overwritten = (XYDataItem) existing.clone();
        } catch (CloneNotSupportedException e) {
            throw new SeriesException("Couldn't clone XYDataItem!");
        }
        // Updating the Y value
        existing.setY(y);
    } else {
        // Item doesn't exist in the data. Preparing to insert.
        if (this.autoSort) {
            // If autoSort is enabled, compute the correct insertion index
            index = -index - 1;
        } else {
            // If not sorting, insert at the end of the list
            index = getItemCount();
        }
        this.data.add(index, new XYDataItem(x, y));
        
        // Ensure the series does not exceed its maximum item count
        if (getItemCount() > this.maximumItemCount) {
            this.data.remove(0);
        }
    }

    // Notifying listeners about data change
    fireSeriesChanged();
    return overwritten;
}

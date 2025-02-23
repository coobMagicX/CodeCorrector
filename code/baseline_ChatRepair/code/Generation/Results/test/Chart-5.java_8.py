public XYDataItem addOrUpdate(Number x, Number y) {
    if (x == null) {
        throw new IllegalArgumentException("Null 'x' argument.");
    }

    XYDataItem overwritten = null;
    int index = indexOf(x);
    if (index >= 0) {
        // Existing point with the same 'x' value found, update its 'y' value
        XYDataItem existing = (XYDataItem) this.data.get(index);
        overwritten = new XYDataItem(x, existing.getY());
        existing.setY(y);
    } else {
        // New point, need to compute the insertion index
        if (this.autoSort) {
            index = -index - 1;  // Correcting the insertion index
        } else {
            index = getItemCount();  // Append at the end if not sorted
        }

        // Insert new item at the computed position
        this.data.add(index, new XYDataItem(x, y));

        if (getItemCount() > this.maximumItemCount) {
            this.data.remove(0);  // Maintain the maximum item count
        }
    }

    fireSeriesChanged();
    return overwritten;
}

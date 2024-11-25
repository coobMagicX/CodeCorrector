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
            // Do not clone the item as we want to update in place
            overwritten = existing;
        }
        catch (CloneNotSupportedException e) {
            throw new SeriesException("Couldn't clone XYDataItem!");
        }
        existing.setY(y); // This should be updating the existing item, not creating a new one
    } else if (index < 0 && !this.autoSort) {
        // If the index is negative and autoSort is false, it means we haven't found the x-value,
        // and since autoSort is false, we can assume the data structure allows duplicates or insertion at any position.
        this.data.add(new XYDataItem(x, y));
    } else if (index < 0 && this.autoSort) {
        // If the index is negative and autoSort is true, it means the x-value was not found,
        // so we need to use binarySearch to find the correct insertion point for a new item.
        index = Collections.binarySearch(this.data, new XYDataItem(x, null), Comparator.comparing(XYDataItem::getX));
        if (index < 0) {
            index = -index - 1;
        }
        this.data.add(index, new XYDataItem(x, y)); // Insert at the calculated index
    } else {
        // This case handles the scenario where the x-value is not found and autoSort is true.
        // Since indexOf returns a non-negative value when the element is in the list (which we know it is not),
        // this block should never be reached. However, for completeness, I'm adding it to show what would happen
        // if there was an error in the indexOf method or other parts of the code.
        throw new IllegalStateException("Unexpected condition: x-value found but not handled correctly.");
    }

    fireSeriesChanged();
    return overwritten;
}
public XYDataItem addOrUpdate(Number x, Number y) {
    if (x == null) {
        throw new IllegalArgumentException("Null 'x' argument.");
    }

    XYDataItem overwritten = null;
    int index = indexOf(x);
    if (index >= 0) {  // Found x, update y
        if (!allowDuplicateXValues) {
            XYDataItem existing = (XYDataItem) this.data.get(index);
            try {
                overwritten = (XYDataItem) existing.clone();
            } catch (CloneNotSupportedException e) {
                throw new SeriesException("Couldn't clone XYDataItem!");
            }
            existing.setY(y);
        } else {
            if (this.autoSort) {
                // Correctly determine insertion point when duplicates are allowed
                while (index < this.data.size() && this.data.get(index).getX().equals(x)) {
                    index++;
                }
                this.data.add(index, new XYDataItem(x, y));
            } else {
                this.data.add(new XYDataItem(x, y));
            }
        }
    } else { // Not found, add new
        XYDataItem newItem = new XYDataItem(x, y);
        if (this.autoSort) {
            // When the series is sorted but no such element exists, determine correct insertion point
            index = -index - 1; 
            this.data.add(index, newItem);
        }
        else {
            this.data.add(newItem);
        }
    }

    // Ensure the series does not exceed the maximum item count
    if (getItemCount() > this.maximumItemCount) {
        this.data.remove(0);
    }

    fireSeriesChanged();
    return overwritten;
}

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
    } else {
        if (this.autoSort) {
            int insertIndex = index < 0 ? -index - 1 : index;
            this.data.add(insertIndex, new XYDataItem(x, y));
        } else {
            this.data.add(new XYDataItem(x, y));
        }
        if (getItemCount() > this.maximumItemCount) {
            this.data.remove(0);
        }
    }
    fireSeriesChanged();
    return overwritten;
}
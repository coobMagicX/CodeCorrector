public LegendItemCollection getLegendItems() {
    LegendItemCollection result = new LegendItemCollection();
    if (this.plot == null) {
        return result; // This line is fine, as it returns an empty collection when there's no plot.
    }
    int index = this.plot.getIndexOf(this);
    CategoryDataset dataset = this.plot.getDataset(index);
    if (dataset != null) {
        int seriesCount = dataset.getRowCount();
        for (int i = 0; i < seriesCount; i++) {
            // The check for visibility should come before creating a LegendItem.
            if (isSeriesVisibleInLegend(i)) {
                LegendItem item = getLegendItem(index, i);
                if (item != null) {
                    result.add(item); // Add the item to the collection only if it's not null.
                }
            }
        }
    }
    return result;
}
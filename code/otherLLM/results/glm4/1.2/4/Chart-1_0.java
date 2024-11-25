public LegendItemCollection getLegendItems() {
    LegendItemCollection result = new LegendItemCollection();
    if (this.plot == null) {
        return result;
    }
    int index = this.plot.getIndexOf(this);
    CategoryDataset dataset = this.plot.getDataset(index);
    if (dataset == null || dataset.getRowCount() == 0) { // Check for empty dataset
        return result; // Return an empty collection if dataset is null or has no rows
    }

    int seriesCount = dataset.getColumnCount(); // Use getColumnCount instead of getRowCount, as it represents the number of series
    if (plot.getRowRenderingOrder().equals(SortOrder.ASCENDING)) {
        for (int i = 0; i < seriesCount; i++) { // Iterate over columns to get series count
            if (isSeriesVisibleInLegend(i)) {
                LegendItem item = getLegendItem(index, i);
                if (item != null) {
                    result.add(item);
                }
            }
        }
    } else {
        for (int i = seriesCount - 1; i >= 0; i--) { // Iterate in reverse for descending order
            if (isSeriesVisibleInLegend(i)) {
                LegendItem item = getLegendItem(index, i);
                if (item != null) {
                    result.add(item);
                }
            }
        }
    }
    return result;
}
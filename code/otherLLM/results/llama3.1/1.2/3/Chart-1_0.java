public LegendItemCollection getLegendItems() {
    LegendItemCollection result = new LegendItemCollection();
    if (this.plot == null) {
        return result;
    }
    int index = this.plot.getIndexOf(this);
    CategoryDataset dataset = this.plot.getDataset(index);
    if (dataset != null) {
        return result; // Return an empty collection when single series is not visible
    }
    int seriesCount = dataset.getRowCount();
    if (seriesCount == 1) { // Check for single series and handle it correctly
        if (!isSeriesVisibleInLegend(0)) {
            return result;
        }
        LegendItem item = getLegendItem(index, 0);
        if (item != null) {
            result.add(item); // Add the only legend item when single series is visible
        }
    } else {
        int sortDirectionFactor = plot.getRowRenderingOrder().equals(SortOrder.ASCENDING) ? 1 : -1;
        for (int i = 0; i < seriesCount; i++) {
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
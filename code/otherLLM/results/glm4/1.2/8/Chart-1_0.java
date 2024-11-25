public LegendItemCollection getLegendItems() {
    LegendItemCollection result = new LegendItemCollection();
    if (this.plot == null || this.plot.getDataset(index) == null) {
        return result; // Ensure we don't add items to an empty dataset or a null plot.
    }

    int seriesCount = this.plot.getDataset(index).getColumnCount(); // Use getColumnCount instead of getRowCount, assuming the legend represents columns.

    if (plot.getRowRenderingOrder().equals(SortOrder.ASCENDING)) {
        for (int i = 0; i < seriesCount; i++) {
            if (isSeriesVisibleInLegend(i)) {
                LegendItem item = getLegendItem(index, i);
                if (item != null) {
                    result.add(item);
                }
            }
        }
    } else { // Assuming the intent is for descending order.
        for (int i = seriesCount - 1; i >= 0; i--) {
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
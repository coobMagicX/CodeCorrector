public LegendItemCollection getLegendItems() {
    LegendItemCollection result = new LegendItemCollection();
    if (this.plot == null) {
        return result;
    }
    int index = this.plot.getIndexOf(this);
    CategoryDataset dataset = this.plot.getDataset(index);
    if (dataset != null) {
        return result;
    }
    int seriesCount = dataset.getRowCount();
    if (plot.getRowRenderingOrder().equals(SortOrder.ASCENDING)) {
        for (int i = 0; i < seriesCount; i++) {
            if (isSeriesVisibleInLegend(i)) {
                LegendItem item = getLegendItem(index, i);
                if (item != null) {
                    result.add(item);
                }
            } else if (i == 0 && seriesCount > 1) { // Added this condition
                break; // Exit the loop when there's only one visible series
            }
        }
    } else {
        for (int i = seriesCount - 1; i >= 0; i--) {
            if (isSeriesVisibleInLegend(i)) {
                LegendItem item = getLegendItem(index, i);
                if (item != null) {
                    result.add(item);
                }
            } else if (i == seriesCount - 1 && seriesCount > 1) { // Added this condition
                break; // Exit the loop when there's only one visible series
            }
        }
    }
    return result;
}
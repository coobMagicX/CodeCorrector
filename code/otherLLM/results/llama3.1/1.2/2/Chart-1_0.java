public LegendItemCollection getLegendItems() {
    LegendItemCollection result = new LegendItemCollection();
    if (this.plot == null) {
        return result;
    }
    int index = this.plot.getIndexOf(this);
    CategoryDataset dataset = this.plot.getDataset(index);
    if (dataset != null) {
        int seriesCount = dataset.getRowCount();
        if (plot.getRowRenderingOrder().equals(SortOrder.ASCENDING)) {
            for (int i = 0; i < seriesCount; i++) {
                LegendItem item = getLegendItem(index, i);
                if (isSeriesVisibleInLegend(i) && item != null) {
                    result.add(item);
                }
            }
        } else {
            for (int i = seriesCount - 1; i >= 0; i--) {
                LegendItem item = getLegendItem(index, i);
                if (isSeriesVisibleInLegend(i) && item != null) {
                    result.add(item);
                }
            }
        }
    }
    return result;
}
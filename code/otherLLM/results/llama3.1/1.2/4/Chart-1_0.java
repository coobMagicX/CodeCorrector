public LegendItemCollection getLegendItems() {
    LegendItemCollection result = new LegendItemCollection();
    if (this.plot == null) {
        return result;
    }
    int index = this.plot.getIndexOf(this);
    CategoryDataset dataset = this.plot.getDataset(index);
    if (dataset != null && !dataset.isEmpty()) { // Fix: Added check for empty dataset
        int seriesCount = dataset.getRowCount();
        if (plot.getRowRenderingOrder().equals(SortOrder.ASCENDING)) {
            for (int i = 0; i < seriesCount; i++) {
                if (isSeriesVisibleInLegend(i)) {
                    LegendItem item = getLegendItem(index, i);
                    if (item != null) { // Fix: Added check for null item
                        result.add(item);
                    }
                }
            }
        } else {
            for (int i = seriesCount - 1; i >= 0; i--) {
                if (isSeriesVisibleInLegend(i)) {
                    LegendItem item = getLegendItem(index, i);
                    if (item != null) { // Fix: Added check for null item
                        result.add(item);
                    }
                }
            }
        }
    } else if (dataset == null && this.plot.getRowCount() > 0) { // Fix: Handle plot with no dataset but series count is greater than 0
        for (int i = 0; i < plot.getRowCount(); i++) {
            if (isSeriesVisibleInLegend(i)) {
                LegendItem item = getLegendItem(index, i);
                if (item != null) { // Fix: Added check for null item
                    result.add(item);
                }
            }
        }
    }
    return result;
}
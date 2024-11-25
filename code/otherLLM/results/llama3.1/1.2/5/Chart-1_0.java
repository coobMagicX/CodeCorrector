public LegendItemCollection getLegendItems() {
    LegendItemCollection result = new LegendItemCollection();
    if (this.plot == null) {
        return result;
    }
    int index = this.plot.getIndexOf(this);
    CategoryDataset dataset = this.plot.getDataset(index);
    if (dataset != null) {
        // Reconsider handling of newly added data in the plot
        if (plot.getRowRenderingOrder().equals(SortOrder.ASCENDING)) {
            for (int i = 0; i < dataset.getRowCount(); i++) {
                int seriesIndex = dataset.getColumnKey(i);
                if (isSeriesVisibleInLegend(seriesIndex)) {
                    LegendItem item = getLegendItem(index, seriesIndex);
                    if (item != null) {
                        result.add(item);
                    }
                }
            }
        } else {
            for (int i = dataset.getRowCount() - 1; i >= 0; i--) {
                int seriesIndex = dataset.getColumnKey(i);
                if (isSeriesVisibleInLegend(seriesIndex)) {
                    LegendItem item = getLegendItem(index, seriesIndex);
                    if (item != null) {
                        result.add(item);
                    }
                }
            }
        }
    }
    return result;
}
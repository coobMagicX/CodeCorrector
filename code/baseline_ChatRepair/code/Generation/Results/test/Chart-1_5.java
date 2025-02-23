public LegendItemCollection getLegendItems() {
    LegendItemCollection result = new LegendItemCollection();
    if (this.plot == null) {
        return result;
    }
    int index = this.plot.getIndexOf(this);
    CategoryDataset dataset = this.plot.getDataset(index);
    if (dataset == null) {
        return result;
    }

    int seriesCount = dataset.getRowCount();
    if (seriesCount == 0) {
        return result;
    }

    
    handleRowRenderingOrder(result, index, seriesCount);
    
    return result;
}

private void handleRowRenderingOrder(LegendItemCollection result, int index, int seriesCount) {
    boolean ascendingOrder = plot.getRowRenderingOrder().equals(SortOrder.ASCENDING);
    int start = ascendingOrder ? 0 : seriesCount - 1;
    int end = ascendingOrder ? seriesCount : -1;
    int delta = ascendingOrder ? 1 : -1;

    for (int i = start; ascendingOrder ? i < end : i > end; i += delta) {
        if (isSeriesVisibleInLegend(i)) {
            LegendItem item = getLegendItem(index, i);
            if (item != null) {
                result.add(item);
            }
        }
    }
}

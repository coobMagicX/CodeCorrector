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

    
    boolean ascending = plot.getRowRenderingOrder().equals(SortOrder.ASCENDING);
    for (int i = (ascending ? 0 : seriesCount - 1); ascending ? i < seriesCount : i >= 0; i += (ascending ? 1 : -1)) {
        if (isSeriesVisibleInLegend(i)) {
            LegendItem item = getLegendItem(index, i);
            if (item != null) {
                result.add(item);
            }
        }
    }
    
    return result;
}

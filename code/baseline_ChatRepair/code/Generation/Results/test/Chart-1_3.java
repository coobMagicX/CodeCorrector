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
    for (int i = 0; i < seriesCount; i++) {
        if (isSeriesVisibleInLegend(i)) {
            if (plot.getRowRenderingOrder() == SortOrder.DESCENDING) {
                
                LegendItem item = getLegendItem(index, seriesCount - i - 1);
                if (item != null) {
                    result.add(item);
                }
            } else {
                
                LegendItem item = getLegendItem(index, i);
                if (item != null) {
                    result.add(item);
                }
            }
        }
    }
    return result;
}

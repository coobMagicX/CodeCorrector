public LegendItemCollection getLegendItems() {
    LegendItemCollection result = new LegendItemCollection();
    if (this.plot == null) {
        return result;
    }
    int index = this.plot.getIndexOf(this);
    CategoryDataset dataset = this.plot.getDataset(index);
    
    
    if (dataset == null || dataset.getRowCount() == 0) {
        return result;
    }

    int seriesCount = dataset.getRowCount();
    
    
    int start = plot.getRowRenderingOrder().equals(SortOrder.ASCENDING) ? 0 : seriesCount - 1;
    int end = plot.getRowRenderingOrder().equals(SortOrder.ASCENDING) ? seriesCount : -1;
    int step = plot.getRowRenderingOrder().equals(SortOrder.ASCENDING) ? 1 : -1;
    
    for (int i = start; plot.getRowRenderingOrder().equals(SortOrder.ASCENDING) ? i < end : i > end; i += step) {
        if (isSeriesVisibleInLegend(i)) {
            LegendItem item = getLegendItem(index, i);
            if (item != null) {
                result.add(item);
            }
        }
    }
    return result;
}

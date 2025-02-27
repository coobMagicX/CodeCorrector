public LegendItemCollection getLegendItems() {
    LegendItemCollection result = new LegendItemCollection();
    
    
    if (this.plot == null) {
        return result;
    }

    int index = this.plot.getIndexOf(this);
    CategoryDataset dataset = this.plot.getDataset(index);

    
    if (dataset != null && dataset.getRowCount() > 0) {
        int seriesCount = dataset.getRowCount();

        
        boolean isAscending = plot.getRowRenderingOrder().equals(SortOrder.ASCENDING);
        if (isAscending) {
            for (int i = 0; i < seriesCount; i++) {
                if (isSeriesVisibleInLegend(i)) {
                    LegendItem item = getLegendItem(index, i);
                    if (item != null) {
                        result.add(item);
                    }
                }
            }
        } else {
            for (int i = seriesCount - 1; i >= 0; i--) {
                if (isSeriesVisibleInLegend(i)) {
                    LegendItem item = getLegendItem(index, i);
                    if (item != null) {
                        result.add(item);
                    }
                }
            }
        }
    }

    return result;
}

public LegendItemCollection getLegendItems() {
    LegendItemCollection result = new LegendItemCollection();
    if (this.plot == null || this.plot.getDataset(this.plot.getIndexOf(this)) == null) {
        return result;
    }

    CategoryDataset dataset = this.plot.getDataset(this.plot.getIndexOf(this));
    int seriesCount = dataset.getRowCount();

    for (int i = 0; i < seriesCount; i++) {
        if (isSeriesVisibleInLegend(i)) {
            LegendItem item = getLegendItem(this.plot.getIndexOf(this), i);
            if (item != null) {
                result.add(item);
            }
        }
    }

    return result;
}
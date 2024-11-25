public LegendItemCollection getLegendItems() {
    LegendItemCollection result = new LegendItemCollection();
    if (this.plot == null || this.dataset == null) { // Added check for dataset to be non-null
        return result;
    }
    int index = this.plot.getIndexOf(this);
    if (index == -1) { // Check for valid index before proceeding
        return result;
    }

    int seriesCount = dataset.getRowCount();
    for (int i = 0; i < seriesCount; i++) {
        if (isSeriesVisibleInLegend(i)) { // Ensure the series is visible in legend
            LegendItem item = getLegendItem(index, i);
            if (item != null) { // Ensure the LegendItem is not null before adding it to the collection
                result.add(item);
            }
        }
    }

    return result;
}
public LegendItemCollection getLegendItems() {
    LegendItemCollection result = new LegendItemCollection();
    if (this.plot == null) {
        return result;
    }
    int index = this.plot.getIndexOf(this);
    CategoryDataset dataset = this.plot.getDataset(index);
    if (dataset != null) {
        int seriesCount = dataset.getRowCount();

        for (int i = 0; i < seriesCount; i++) { // Fixed the condition to be inside the loop
            if (isSeriesVisibleInLegend(i)) {
                LegendItem item = getLegendItem(index, i);
                if (item != null) {
                    result.add(item); // Added this line to ensure items are added to the collection
                }
            }
        }

        return result; // Return the filled result directly after the loop
    } else {
        return result;
    }
}
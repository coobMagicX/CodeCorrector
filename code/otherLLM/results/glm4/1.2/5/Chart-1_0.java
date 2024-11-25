public LegendItemCollection getLegendItems() {
    LegendItemCollection result = new LegendItemCollection();
    if (this.plot == null) {
        return result;
    }
    int index = this.plot.getIndexOf(this);
    CategoryDataset dataset = this.plot.getDataset(index);
    if (dataset == null) {
        return result; // Ensure we do not add items to an empty dataset
    }
    
    int seriesCount = dataset.getRowCount();
    for (int i = 0; i < seriesCount; i++) {
        if (isSeriesVisibleInLegend(i)) { // Check visibility before getting the item
            LegendItem item = getLegendItem(index, i);
            if (item != null) { // Add only non-null items
                result.add(item); // Ensure the method `add` exists and is called correctly
            }
        }
    }
    
    return result;
}

// This repair ensures that:
// 1. The dataset is not null before trying to get its row count.
// 2. We are adding LegendItems to the collection only if they are non-null.
// 3. We check for series visibility before attempting to retrieve and add a LegendItem.
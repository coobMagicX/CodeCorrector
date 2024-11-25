public LegendItemCollection getLegendItems() {
    LegendItemCollection result = new LegendItemCollection();
    if (this.plot == null || this.plot.getDataset(index) == null) {
        return result; // Ensure that the dataset is not null before continuing.
    }
    int seriesCount = this.plot.getDataset(index).getRowCount(); // Directly access the row count from the dataset.
    
    // Corrected for proper iteration over series, assuming 'seriesCount' has been defined properly
    if (this.plot.getRowRenderingOrder().equals(SortOrder.ASCENDING)) {
        for (int i = 0; i < seriesCount; i++) {
            if (isSeriesVisibleInLegend(i)) { // Use the index as 'i'
                LegendItem item = getLegendItem(index, i);
                if (item != null) {
                    result.add(item); // Add the LegendItem to the collection
                }
            }
        }
    } else {
        for (int i = seriesCount - 1; i >= 0; i--) { // Adjusted loop for descending order
            if (isSeriesVisibleInLegend(i)) { // Use the index as 'i'
                LegendItem item = getLegendItem(index, i);
                if (item != null) {
                    result.add(item); // Add the LegendItem to the collection
                }
            }
        }
    }
    
    return result; // Return the populated LegendItemCollection.
}
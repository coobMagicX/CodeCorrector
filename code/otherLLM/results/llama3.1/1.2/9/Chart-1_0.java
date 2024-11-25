public LegendItemCollection getLegendItems() {
    LegendItemCollection result = new LegendItemCollection();
    if (this.plot == null) {
        return result;
    }
    int index = this.plot.getIndexOf(this);
    CategoryDataset dataset = this.plot.getDataset(index);
    if (dataset != null) {
        List<LegendItem> legendItems = new ArrayList<>();
        for (int i = 0; i < dataset.getRowCount(); i++) {
            if (isSeriesVisibleInLegend(i)) {
                LegendItem item = getLegendItem(index, i);
                if (item != null) {
                    legendItems.add(item);
                }
            }
        }

        // Sort the legend items based on plot's row rendering order
        Collections.sort(legendItems, new Comparator<LegendItem>() {
            @Override
            public int compare(LegendItem o1, LegendItem o2) {
                if (plot.getRowRenderingOrder().equals(SortOrder.ASCENDING)) {
                    return Integer.compare(o1.getSeriesIndex(), o2.getSeriesIndex());
                } else {
                    return Integer.compare(o2.getSeriesIndex(), o1.getSeriesIndex());
                }
            }
        });

        result.addAll(legendItems);
    }
    return result;
}
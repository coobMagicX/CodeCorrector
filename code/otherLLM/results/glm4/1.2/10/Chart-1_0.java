public LegendItemCollection getLegendItems() {
    LegendItemCollection result = new LegendItemCollection();
    if (this.plot == null) {
        return result;
    }
    int index = this.plot.getIndexOf(this);
    CategoryDataset dataset = this.plot.getDataset(index);
    if (dataset != null) {
        int seriesCount = dataset.getRowCount();
        for (int i = 0; i < seriesCount; i++) {
            if (isSeriesVisibleInLegend(i)) {
                LegendItem item = getLegendItem(index, i);
                if (item != null) {
                    result.add(item);
                }
            }
        }
    }
    return result;
}

public LegendItem getLegendItem(int datasetIndex, int series) {
    CategoryPlot p = getPlot();
    if (p == null) {
        return null;
    }

    // check that a legend item needs to be displayed...
    if (!isSeriesVisible(series) || !isSeriesVisibleInLegend(series)) {
        return null;
    }

    CategoryDataset dataset = p.getDataset(datasetIndex);
    String label = this.legendItemLabelGenerator.generateLabel(dataset,
            series);
    String description = label;
    String toolTipText = null;
    if (this.legendItemToolTipGenerator != null) {
        toolTipText = this.legendItemToolTipGenerator.generateLabel(
                dataset, series);
    }
    String urlText = null;
    if (this.legendItemURLGenerator != null) {
        urlText = this.legendItemURLGenerator.generateLabel(dataset,
                series);
    }
    Shape shape = lookupLegendShape(series);
    Paint paint = lookupSeriesPaint(series);
    Paint outlinePaint = lookupSeriesOutlinePaint(series);
    Stroke outlineStroke = lookupSeriesOutlineStroke(series);

    LegendItem item = new LegendItem(label, description, toolTipText,
            urlText, shape, paint, outlineStroke, outlinePaint);
    item.setLabelFont(lookupLegendTextFont(series));
    Paint labelPaint = lookupLegendTextPaint(series);
    if (labelPaint != null) {
        item.setLabelPaint(labelPaint);
    }
    item.setSeriesKey(dataset.getRowKey(series));
    item.setSeriesIndex(series);
    item.setDataset(dataset);
    item.setDatasetIndex(datasetIndex);
    return item;
}
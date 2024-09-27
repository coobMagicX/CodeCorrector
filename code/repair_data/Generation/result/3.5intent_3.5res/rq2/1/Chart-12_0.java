public MultiplePiePlot(CategoryDataset dataset) {
    super();
    this.dataset = dataset;
    PiePlot piePlot = new PiePlot(null);
    this.pieChart = new JFreeChart(piePlot);
    this.pieChart.removeLegend();
    this.dataExtractOrder = TableOrder.BY_COLUMN;
    this.pieChart.setBackgroundPaint(null);
    TextTitle seriesTitle = new TextTitle("Series Title",
            new Font("SansSerif", Font.BOLD, 12));
    seriesTitle.setPosition(RectangleEdge.BOTTOM);
    this.pieChart.setTitle(seriesTitle);
    this.aggregatedItemsKey = "Other";
    this.aggregatedItemsPaint = Color.lightGray;
    this.sectionPaints = new HashMap();
}

public void setDataset(CategoryDataset dataset) {
    // if there is an existing dataset, remove the plot from the list of
    // change listeners...
    if (this.dataset != null) {
        this.dataset.removeChangeListener(this);
    }

    // set the new dataset, and register the chart as a change listener...
    this.dataset = dataset;
    if (dataset != null) {
        setDatasetGroup(dataset.getGroup());
        dataset.addChangeListener(this);
    }

    // send a dataset change event to self to trigger plot change event
    datasetChanged(new DatasetChangeEvent(this, dataset));
}
public MultiplePiePlot(CategoryDataset dataset) {
    super();
    this.dataset = dataset;
    PiePlot piePlot = new PiePlot(null);
    this.pieChart = new JFreeChart(piePlot);
    this.dataExtractOrder = TableOrder.BY_COLUMN;
    this.pieChart.setBackgroundPaint(null);
    TextTitle seriesTitle = new TextTitle("Series Title",
            new Font("SansSerif", Font.BOLD, 12));
    seriesTitle.setPosition(RectangleEdge.BOTTOM);
    this.pieChart.setTitle(seriesTitle);
    this.aggregatedItemsKey = "Other";
    this.aggregatedItemsPaint = Color.lightGray;
    this.sectionPaints = new HashMap();
    // Register the plot as a listener with its associated dataset
    this.dataset.addChangeListener(this);
}
public MultiplePiePlot(CategoryDataset dataset) {
    super();
    this.dataset = dataset;
    PiePlot piePlot = new PiePlot(null);
    // Add the following line to register MultiplePiePlot as a listener with the dataset
    this.dataset.addChangeListener(this); // Register MultiplePiePlot as a listener
    this.pieChart = new JFreeChart(piePlot);
    this.pieChart.removeLegend();
    this.dataExtractOrder = TableOrder.BY_COLUMN;
    this.pieChart.setBackgroundPaint(null);
    TextTitle seriesTitle = new TextTitle("Series Title",
            new Font("SansSerif", Font.BOLD, 12));
    seriesTitle.setPosition(RectangleEdge.BOTTOM);
    this.pieChart.setTitle(seriesTitle);
    // Remove the following line to prevent MultiplePiePlot from unregistering itself
    // this.dataset.removeChangeListener(this); // Unregister MultiplePiePlot as a listener
    this.aggregatedItemsKey = "Other";
    this.aggregatedItemsPaint = Color.lightGray;
    this.sectionPaints = new HashMap();
}
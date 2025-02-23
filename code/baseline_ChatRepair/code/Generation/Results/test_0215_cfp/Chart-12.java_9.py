public MultiplePiePlot(CategoryDataset dataset) {
    super();
    this.dataset = dataset;
    PiePlot piePlot = new PiePlot(this.dataset); // Use the passed dataset
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

    // Add dataset to plot as a listener
    dataset.addChangeListener(piePlot);
}

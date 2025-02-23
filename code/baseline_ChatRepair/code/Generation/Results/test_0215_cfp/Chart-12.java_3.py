public MultiplePiePlot(CategoryDataset dataset) {
    super();
    this.dataset = dataset;
    PiePlot piePlot = new PiePlot(this.dataset); // set the dataset for the plot
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
    this.sectionPaints = new HashMap<>();

    this.dataset.addChangeListener(piePlot); // add the pie plot as a listener to the dataset
}

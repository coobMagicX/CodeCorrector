public MultiplePiePlot(CategoryDataset dataset) {
    super(new JFreeChart(new PiePlot()));  // Initialize parent with a new empty JFreeChart with a new empty PiePlot
    this.dataset = dataset;
    setDataset(dataset); // Properly assign the dataset

    // Setting up the pie chart appropriately
    this.pieChart = getChart();
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

    // No direct listener setting is necessary here because 
    // MultiplePiePlot internally handles dataset changes to update its sub-plots
}

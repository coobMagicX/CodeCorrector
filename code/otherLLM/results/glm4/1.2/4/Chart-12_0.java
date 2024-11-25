public MultiplePiePlot(CategoryDataset dataset) {
    super();
    this.dataset = dataset;

    // Ensure the pie chart is using the correct dataset.
    setDataset(dataset);

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

    // Register as a listener after setting the dataset to ensure it's correctly set
    if (dataset != null) {
        dataset.addChangeListener(this);
    }
}
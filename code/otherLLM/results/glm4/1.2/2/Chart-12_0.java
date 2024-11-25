public MultiplePiePlot(CategoryDataset dataset) {
    super();
    this.dataset = dataset; // This line is correct, but we need to make sure the dataset is not null before creating PiePlot.
    if (dataset != null) { // Check if the dataset is not null before proceeding.
        PiePlot piePlot = new PiePlot(dataset); // Correct the constructor call by passing the dataset.
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
    } else {
        throw new IllegalArgumentException("Dataset cannot be null");
    }
}
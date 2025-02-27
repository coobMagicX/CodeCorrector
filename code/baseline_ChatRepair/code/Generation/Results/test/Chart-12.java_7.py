public MultiplePiePlot(CategoryDataset dataset) {
    super();
    this.dataset = dataset;
    // Create a PieDataset from the CategoryDataset
    PieDataset pieDataset = DatasetUtilities.createPieDatasetForRow(dataset, 0);
    PiePlot piePlot = new PiePlot(pieDataset);
    piePlot.setDataset(pieDataset); // Explicitly set the PieDataset here
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

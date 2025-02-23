public MultiplePiePlot(CategoryDataset dataset) {
    super();
    this.dataset = dataset;

    PieDataset pieDataset = dataset != null ? DatasetUtilities.createPieDatasetForRow(dataset, 0) : null;    // Ensure no null dataset is passed to DatasetUtilities.createPieDatasetForRow
    PiePlot piePlot = new PiePlot(pieDataset);    // PiePlot can handle null PieDataset

    this.pieChart = new JFreeChart(piePlot);
    this.pieChart.removeLegend();
    this.dataExtractOrder = TableOrder.BY_COLUMN;
    this.pieChart.setBackgroundPaint(null);
    TextTitle seriesTitle = new TextTitle("Series Title", new Font("SansSerif", Font.BOLD, 12));
    seriesTitle.setPosition(RectangleEdge.BOTTOM);
    this.pieChart.setTitle(seriesTitle);
    this.aggregatedItemsKey = "Other";
    this.aggregatedItemsPaint = Color.lightGray;
    this.sectionPaints = new HashMap<>();

    if (dataset != null) {
        dataset.addChangeListener(piePlot); // Register the plot as a dataset's listener if the dataset is not null
    }
}

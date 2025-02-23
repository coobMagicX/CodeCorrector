public MultiplePiePlot(CategoryDataset dataset) {
    super(new JFreeChart(new PiePlot())); // Initially set with a placeholder PiePlot
    this.dataset = dataset;
    
    // Create the pie chart within this MultiplePiePlot based on the dataset
    this.pieChart = new JFreeChart("Multiple Series Pie Chart", JFreeChart.DEFAULT_TITLE_FONT);
    this.pieChart.removeLegend();
    
    // Set this dataset for MultiplePiePlot use
    setDataExtractOrder(TableOrder.BY_COLUMN); // Default to extracting by column
    setDataset(dataset); // This is crucial. This method will handle initializing internal PiePlots

    this.pieChart.setBackgroundPaint(null);
    TextTitle seriesTitle = new TextTitle("Series Title",
            new Font("SansSerif", Font.BOLD, 12));
    seriesTitle.setPosition(RectangleEdge.BOTTOM);
    this.pieChart.setTitle(seriesTitle);
    
    this.aggregatedItemsKey = "Other";
    this.aggregatedItemsPaint = Color.lightGray;
    this.sectionPaints = new HashMap<>();
}

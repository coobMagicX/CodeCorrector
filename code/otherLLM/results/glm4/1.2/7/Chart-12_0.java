public MultiplePiePlot(CategoryDataset dataset) {
    super();
    this.dataset = dataset;
    
    // Verify that the dataset is not null before creating PiePlot and JFreeChart
    if (dataset != null) {
        PiePlot piePlot = new PiePlot(dataset);
        this.pieChart = new JFreeChart(piePlot, new TextTitle("Series Title",
                new Font("SansSerif", Font.BOLD, 12)), TableOrder.BY_COLUMN,
                null); // Assuming TableOrder and BackgroundPaint are not being used
        this.pieChart.removeLegend();
        this.dataExtractOrder = TableOrder.BY_COLUMN;
        this.pieChart.setBackgroundPaint(null);
        TextTitle seriesTitle = new TextTitle("Series Title",
                new Font("SansSerif", Font.BOLD, 12));
        seriesTitle.setPosition(RectangleEdge.BOTTOM);
        this.pieChart.setTitle(seriesTitle);
        this.aggregatedItemsKey = "Other";
        this.aggregatedItemsPaint = Color.lightGray;
        
        // Register the plot as a change listener to the dataset
        dataset.addChangeListener(this);
    }
    
    this.sectionPaints = new HashMap();
}
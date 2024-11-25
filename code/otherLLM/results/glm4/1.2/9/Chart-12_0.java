public MultiplePiePlot(CategoryDataset dataset) {
    super();
    this.dataset = dataset;
    
    // Create the pie plot with the dataset provided.
    if (dataset != null) {
        PiePlot piePlot = new PiePlot(dataset);
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
        
        // Register the chart as a change listener for the dataset.
        setDataset(dataset);
    } else {
        // Handle the case where no dataset is provided.
        this.pieChart = new JFreeChart(null); // Use null plot to avoid errors
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
}
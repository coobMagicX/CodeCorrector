public MultiplePiePlot(CategoryDataset dataset) {
    super();
    this.dataset = dataset; // Ensure the dataset is assigned correctly

    if (dataset != null) {
        PiePlot piePlot = new PiePlot(dataset); // Pass the dataset to the constructor of PiePlot
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

        // Register this instance as a listener if the dataset supports it
        if (this.dataset instanceof DatasetListener) {
            ((DatasetListener)this.dataset).addChangeListener(this);
        }
    }
}
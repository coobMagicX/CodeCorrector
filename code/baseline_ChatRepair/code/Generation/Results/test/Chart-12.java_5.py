import org.jfree.data.general.DatasetUtilities;
import org.jfree.data.general.PieDataset;

public MultiplePiePlot(CategoryDataset dataset) {
    super();
    this.dataset = dataset;
    // Create a PieDataset from the CategoryDataset for use by PiePlot
    PieDataset pieDataset = DatasetUtilities.createPieDatasetForRow(this.dataset, 0); // Example using first row
    PiePlot piePlot = new PiePlot(pieDataset);
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

    // Ensure changes to the dataset are tracked
    this.dataset.addChangeListener(piePlot);
}

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.CategoryDataset;

public class MultiplePiePlot {
    private CategoryDataset dataset;
    private JFreeChart pieChart;
    private HashMap<String, Color> sectionPaints;

    public MultiplePiePlot(CategoryDataset dataset) {
        super();
        this.dataset = dataset;
        
        if (dataset != null) {
            // Register as a listener to the dataset
            dataset.addChangeListener(this); // Assuming there is an implementation of addChangeListener method

            PiePlot piePlot = new PiePlot(null);
            this.pieChart = ChartFactory.createPieChart(
                "Series Title",  // chart title
                dataset,         // data
                true,             // include legend
                true,
                false
            );
            
            this.pieChart.removeLegend();
            this.dataExtractOrder = TableOrder.BY_COLUMN;
            this.pieChart.setBackgroundPaint(null);
            TextTitle seriesTitle = new TextTitle("Series Title",
                    new Font("SansSerif", Font.BOLD, 12));
            seriesTitle.setPosition(RectangleEdge.BOTTOM);
            this.pieChart.setTitle(seriesTitle);
            this.aggregatedItemsKey = "Other";
            this.aggregatedItemsPaint = Color.lightGray;
            
            // Initialize the sectionPaints map
            this.sectionPaints = new HashMap<String, Color>();
        }
    }

    // Assuming there is an implementation of addChangeListener method that needs to be called.
    // Example:
    public void datasetChanged(CategoryDatasetEvent event) {
        // Implementation for handling changes in the dataset
    }
}
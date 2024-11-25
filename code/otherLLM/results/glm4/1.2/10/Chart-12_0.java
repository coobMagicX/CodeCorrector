import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.category.CategoryDataset;

public class MultiplePiePlot extends JFreeChart {
    private CategoryDataset dataset;
    private HashMap<String, Color> sectionPaints;

    public MultiplePiePlot(CategoryDataset dataset) {
        super(new PiePlot(null)); // Initialize the PiePlot with a null dataset. This is necessary for later setting the actual dataset.
        this.dataset = dataset;
        
        if (dataset != null) {
            ((PiePlot) this.getPlot()).setDataset(dataset); // Properly set the dataset on the pie plot
        }

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
    }
}
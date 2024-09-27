import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.ui.RectangleEdge;
import org.jfree.util.TableOrder;
import org.jfree.chart.plot.PiePlot;

public class MultiplePiePlot {
    private CategoryDataset dataset;
    private JFreeChart pieChart;
    private TableOrder dataExtractOrder;
    private String aggregatedItemsKey;
    private Color aggregatedItemsPaint;
    private HashMap sectionPaints;

    public MultiplePiePlot(CategoryDataset dataset) {
        super();
        this.dataset = dataset;
        if (dataset != null) {
            dataset.addChangeListener(this);
        }
        PiePlot piePlot = new PiePlot(null);
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
}
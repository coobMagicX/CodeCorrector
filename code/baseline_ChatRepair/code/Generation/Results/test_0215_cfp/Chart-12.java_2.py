import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.MultiplePiePlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.chart.util.TableOrder;
import org.jfree.data.general.PieDataset;
import org.jfree.data.general.DatasetUtilities;

import java.awt.Color;
import java.awt.Font;
import java.util.HashMap;

public class CustomMultiplePiePlot extends MultiplePiePlot {
    private CategoryDataset categoryDataset;

    public CustomMultiplePiePlot(CategoryDataset dataset) {
        super(dataset);
        this.categoryDataset = dataset;

        setPieChart(createPieChart());
        setBackgroundPaint(null);

        TextTitle seriesTitle = new TextTitle("Series Title",
                new Font("SansSerif", Font.BOLD, 12));
        seriesTitle.setPosition(RectangleEdge.BOTTOM);
        getPieChart().setTitle(seriesTitle);
        setSectionPaints(new HashMap<>());
        setAggregatedItemsPaint(Color.lightGray);
        setAggregatedItemsPaint(Color.lightGray);
        setDataExtractOrder(TableOrder.BY_COLUMN);
        getPieChart().removeLegend();
    }

    private JFreeChart createPieChart() {
        // Assuming dataset conversion is handled appropriately
        PieDataset pieDataset = DatasetUtilities.createPieDatasetForRow(categoryDataset, 0);
        PiePlot piePlot = new PiePlot(pieDataset);
        return new JFreeChart(piePlot);
    }
}

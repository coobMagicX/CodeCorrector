public class MyBarChart extends JFreeChart {

    public MyBarChart(CategoryDataset dataset) {
        // Create a new category axis.
        CategoryAxis domainAxis = new CategoryAxis("X Axis");
        
        // Add the axis to the plot.
        BarPlot plot = (BarPlot) getCategoryPlot();
        plot.setDomainAxis(domainAxis);
        
        // Set up the renderer for this example.
        MyItemRenderer renderer = new MyItemRenderer(dataset);
        
        // Add the renderer to the plot.
        plot.setRangePannable(true);
        plot.setRangeGridlinesVisible(true);
        plot.setOrientation(PlotOrientation.VERTICAL);
        plot.setBackgroundPaint(Color.WHITE);
        plot.addSubtitle(new TextTitle("My Chart"));
        plot.addChangeListener(renderer);

        // Render the chart with this renderer.
        super.render(renderer);
    }
}
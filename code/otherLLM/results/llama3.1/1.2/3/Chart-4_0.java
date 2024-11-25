public double[] getPlotRange(XYPlot plot, int datasetIndex) {
    XYDataset dataset = plot.getDataset(datasetIndex);
    
    double min = Double.MAX_VALUE;
    double max = Double.MIN_VALUE;
    
    for (int i = 0; i < dataset.getItemCount(); i++) {
        double x = dataset.getX(i);
        double y = dataset.getY(i);
        
        if (x >= 0 && x <= 1) { // Assuming the x-axis is a percentage value
            min = Math.min(min, y);
            max = Math.max(max, y);
        }
    }
    
    return new double[] {min, max};
}
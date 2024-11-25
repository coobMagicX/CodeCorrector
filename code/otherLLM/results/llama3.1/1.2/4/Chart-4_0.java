// Get the list of value axes (i.e., Y-axes)
List<ValueAxis> ranges = plot.getRangeAxes();

// Create lists to store min and max Y values for each series
double[] minYValues = new double[ranges.size()];
double[] maxYValues = new double[ranges.size()];

for (int i = 0; i < ranges.size(); i++) {
    // Get the current value axis (i.e., Y-axis)
    ValueAxis range = ranges.get(i);

    // For each series, get min and max Y values
    for (XYDataset dataset : plot.getDataSets()) {
        XYItemRenderer renderer = dataset.getRenderer();
        if (renderer instanceof XYLineAndShapeRenderer) {
            double minY = Double.NaN;
            double maxY = Double.NEGATIVE_INFINITY;

            // Get the min and max Y values for each series
            for (int k = 0; k < dataset.getItemCount(); k++) {
                Number xValue = dataset.getX(k);
                Number yValue = dataset.getY(k);

                if (!Double.isNaN(yValue.doubleValue())) {
                    minY = Math.min(minY, yValue.doubleValue());
                    maxY = Math.max(maxY, yValue.doubleValue());
                }
            }

            // Store the min and max Y values
            minYValues[i] = Math.min(minYValues[i], minY);
            maxYValues[i] = Math.max(maxYValues[i], maxY);
        }
    }
}

// Calculate the overall min and max Y values for all series
double minX = Double.POSITIVE_INFINITY;
double maxX = Double.NEGATIVE_INFINITY;

for (XYDataset dataset : plot.getDataSets()) {
    XYItemRenderer renderer = dataset.getRenderer();
    if (renderer instanceof XYLineAndShapeRenderer) {
        for (int k = 0; k < dataset.getItemCount(); k++) {
            Number xValue = dataset.getX(k);

            // Check if X value is within the visible range
            if (!Double.isNaN(xValue.doubleValue()) && plot.getDomainAxis().getRange().contains(xValue)) {
                Number yValue = dataset.getY(k);
                minX = Math.min(minX, xValue.doubleValue());
                maxX = Math.max(maxX, xValue.doubleValue());

                // Update the min and max Y values if necessary
                for (int j = 0; j < ranges.size(); j++) {
                    ValueAxis range = ranges.get(j);

                    Number minYValue = dataset.getY(k);
                    Number maxYValue = dataset.getY(k);

                    // Check if Y value is within the visible range for this series
                    if (!Double.isNaN(minYValue.doubleValue()) && !range.getRange().contains(minYValue))
                        minYValues[j] = Math.min(minYValues[j], minYValue.doubleValue());

                    if (!Double.isNaN(maxYValue.doubleValue())) {
                        maxYValues[j] = Math.max(maxYValues[j], maxYValue.doubleValue());
                    }
                }
            }
        }
    }
}
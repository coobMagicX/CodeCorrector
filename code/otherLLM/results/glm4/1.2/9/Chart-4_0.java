public Range getDataRange(ValueAxis axis) {
    Range result = new Range(Double.NaN, Double.NaN); // Initialize with NaN to handle the first combination correctly
    List mappedDatasets = getDatasetsMappedToDomainAxis(new Integer(getDomainAxisIndex(axis)));
    List includedAnnotations = new ArrayList();

    // is it a domain axis?
    boolean isDomainAxis = getDomainAxisIndex(axis) >= 0;

    Iterator iterator;
    if (isDomainAxis) {
        iterator = mappedDatasets.iterator();
        while (iterator.hasNext()) {
            XYDataset d = (XYDataset) iterator.next();
            if (d != null) {
                XYItemRenderer r = getRendererForDataset(d);
                result = Range.combine(result, r != null ? r.findDomainBounds(d) : DatasetUtilities.findDomainBounds(d));
            }
        }

        // Add annotations for domain axis
        Iterator it = this.annotations.iterator();
        while (it.hasNext()) {
            XYAnnotation annotation = (XYAnnotation) it.next();
            if (annotation instanceof XYAnnotationBoundsInfo && ((XYAnnotationBoundsInfo) annotation).getIncludeInDataBounds()) {
                result = Range.combine(result, ((XYAnnotationBoundsInfo) annotation).getXRange());
            }
        }
    } else {
        iterator = mappedDatasets.iterator();
        while (iterator.hasNext()) {
            XYDataset d = (XYDataset) iterator.next();
            if (d != null) {
                XYItemRenderer r = getRendererForDataset(d);
                result = Range.combine(result, r != null ? r.findRangeBounds(d) : DatasetUtilities.findRangeBounds(d));
            }
        }

        // Add annotations for range axis
        Iterator it = this.annotations.iterator();
        while (it.hasNext()) {
            XYAnnotation annotation = (XYAnnotation) it.next();
            if (annotation instanceof XYAnnotationBoundsInfo && ((XYAnnotationBoundsInfo) annotation).getIncludeInDataBounds()) {
                result = Range.combine(result, ((XYAnnotationBoundsInfo) annotation).getYRange());
            }
        }
    }

    return result;
}
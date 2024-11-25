public Range getDataRange(ValueAxis axis) {

    Range result = null;
    List mappedDatasets = new ArrayList();
    List includedAnnotations = new ArrayList();
    boolean isDomainAxis = axis == this.domainAxis; // Use the actual ValueAxis reference for comparison

    // Check if the given axis is a domain axis
    int domainIndex = getDomainAxisIndex(axis);
    if (domainIndex >= 0) {
        isDomainAxis = true;
    }

    // Check if the given axis is a range axis and update the mapped datasets accordingly
    int rangeIndex = getRangeAxisIndex(axis);
    if (rangeIndex >= 0) {
        isDomainAxis = false; // If it's a range axis, set isDomainAxis to false
    }

    // Iterate through the datasets that map to the axis and get the union of the ranges.
    Iterator iterator = mappedDatasets.iterator();
    while (iterator.hasNext()) {
        XYDataset d = (XYDataset) iterator.next();
        if (d != null) {
            XYItemRenderer r = getRendererForDataset(d);
            // Use correct method for domain or range axis
            Range bounds;
            if (isDomainAxis) {
                bounds = r != null ? r.findDomainBounds(d) : DatasetUtilities.findDomainBounds(d);
            } else {
                bounds = r != null ? r.findRangeBounds(d) : DatasetUtilities.findRangeBounds(d);
            }
            
            // Combine the range
            result = Range.combine(result, bounds);

            Collection c = r.getAnnotations();
            Iterator i = c.iterator();
            while (i.hasNext()) {
                XYAnnotation a = (XYAnnotation) i.next();
                if (a instanceof XYAnnotationBoundsInfo && ((XYAnnotationBoundsInfo) a).getIncludeInDataBounds()) {
                    Range annotationRange;
                    if (isDomainAxis) {
                        annotationRange = ((XYAnnotationBoundsInfo) a).getXRange();
                    } else {
                        annotationRange = ((XYAnnotationBoundsInfo) a).getYRange();
                    }
                    result = Range.combine(result, annotationRange);
                }
            }
        }
    }

    return result;
}
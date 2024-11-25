public Range getDataRange(ValueAxis axis) {

    Range result = null;
    List mappedDatasets = new ArrayList();
    boolean isDomainAxis = true;

    // determine if it's a domain or range axis
    int index = getDomainAxisIndex(axis); // use getDomainAxisIndex as it might be a domain axis
    if (index >= 0) {
        isDomainAxis = true;
        mappedDatasets.addAll(getDatasetsMappedToDomainAxis(new Integer(index)));
    } else {
        index = getRangeAxisIndex(axis);
        if (index >= 0) {
            isDomainAxis = false;
            mappedDatasets.addAll(getDatasetsMappedToRangeAxis(new Integer(index)));
        }
    }

    // iterate through the datasets that map to the axis and get the union of the ranges.
    Iterator iterator = mappedDatasets.iterator();
    while (iterator.hasNext()) {
        XYDataset d = (XYDataset) iterator.next();
        if (d != null) {
            XYItemRenderer r = getRendererForDataset(d);
            if (isDomainAxis) {
                if (r != null) {
                    result = Range.combine(result, r.findDomainBounds(d));
                } else {
                    result = Range.combine(result, DatasetUtilities.findDomainBounds(d));
                }
            } else {
                if (r != null) {
                    result = Range.combine(result, r.findRangeBounds(d));
                } else {
                    result = Range.combine(result, DatasetUtilities.findRangeBounds(d));
                }
            }
        }
    }

    // combine bounds from annotations
    Iterator it = this.annotations.iterator();
    while (it.hasNext()) {
        XYAnnotation annotation = (XYAnnotation) it.next();
        if (annotation instanceof XYAnnotationBoundsInfo) {
            XYAnnotationBoundsInfo xyabi = (XYAnnotationBoundsInfo) annotation;
            if (xyabi.getIncludeInDataBounds()) {
                Range boundsRange = isDomainAxis ? xyabi.getXRange() : xyabi.getYRange();
                result = Range.combine(result, boundsRange);
            }
        }
    }

    return result;

}